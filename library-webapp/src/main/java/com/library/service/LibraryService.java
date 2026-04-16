package com.library.service;

import com.library.db.DatabaseConnection;
import com.library.exception.BookNotAvailableException;
import com.library.exception.DatabaseException;
import com.library.exception.InvalidTransactionException;
import com.library.model.Book;
import com.library.model.Member;
import com.library.model.Transaction;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class LibraryService {
    private static final int LOAN_PERIOD_DAYS = 14;
    private static final BigDecimal DAILY_FINE = BigDecimal.valueOf(5);

    public List<Book> getAvailableBooks() throws DatabaseException {
        String sql = """
                SELECT book_id, title, author, isbn, total_copies, available_copies
                FROM books
                WHERE available_copies > 0
                ORDER BY title, book_id
                """;

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            List<Book> books = new ArrayList<>();
            while (resultSet.next()) {
                books.add(mapBook(resultSet));
            }
            return books;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch available books.", e);
        }
    }

    public List<Member> getAllMembers() throws DatabaseException {
        String sql = """
                SELECT m.member_id,
                       m.name,
                       m.email,
                       m.phone_number,
                       COALESCE(SUM(CASE
                           WHEN t.transaction_id IS NOT NULL AND t.return_date IS NULL THEN 1
                           ELSE 0
                       END), 0) AS active_loan_count,
                       COALESCE(SUM(CASE
                           WHEN t.transaction_id IS NOT NULL
                                AND t.return_date IS NULL
                                AND t.due_date < CURDATE()
                           THEN DATEDIFF(CURDATE(), t.due_date) * ?
                           ELSE 0
                       END), 0) AS outstanding_fine,
                       COALESCE(SUM(COALESCE(t.fine_amount, 0)), 0) AS lifetime_fine_total
                FROM members m
                LEFT JOIN transactions t ON t.member_id = m.member_id
                GROUP BY m.member_id, m.name, m.email, m.phone_number
                ORDER BY m.name, m.member_id
                """;

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, DAILY_FINE);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Member> members = new ArrayList<>();
                while (resultSet.next()) {
                    members.add(mapMember(resultSet));
                }
                return members;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch members.", e);
        }
    }

    public Member getMember(int memberId) throws DatabaseException {
        if (memberId <= 0) {
            throw new DatabaseException("Member ID must be a positive number.");
        }

        String sql = """
                SELECT m.member_id,
                       m.name,
                       m.email,
                       m.phone_number,
                       COALESCE(SUM(CASE
                           WHEN t.transaction_id IS NOT NULL AND t.return_date IS NULL THEN 1
                           ELSE 0
                       END), 0) AS active_loan_count,
                       COALESCE(SUM(CASE
                           WHEN t.transaction_id IS NOT NULL
                                AND t.return_date IS NULL
                                AND t.due_date < CURDATE()
                           THEN DATEDIFF(CURDATE(), t.due_date) * ?
                           ELSE 0
                       END), 0) AS outstanding_fine,
                       COALESCE(SUM(COALESCE(t.fine_amount, 0)), 0) AS lifetime_fine_total
                FROM members m
                LEFT JOIN transactions t ON t.member_id = m.member_id
                WHERE m.member_id = ?
                GROUP BY m.member_id, m.name, m.email, m.phone_number
                """;

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, DAILY_FINE);
            statement.setInt(2, memberId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapMember(resultSet);
                }
                throw new DatabaseException("Member not found.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to load member details.", e);
        }
    }

    public int addMember(Member member) throws DatabaseException {
        if (member == null) {
            throw new DatabaseException("Member details are required.");
        }

        String name = sanitize(member.getName());
        String email = sanitize(member.getEmail());
        String phoneNumber = sanitize(member.getPhoneNumber());

        if (name.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
            throw new DatabaseException("Name, email, and phone number are required.");
        }

        String sql = """
                INSERT INTO members (name, email, phone_number)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, phoneNumber);
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

            throw new DatabaseException("Member was created, but the new member ID could not be read.");
        } catch (SQLException e) {
            if (isDuplicateEntry(e)) {
                throw new DatabaseException("A member with this email already exists.", e);
            }
            throw new DatabaseException("Failed to create member.", e);
        }
    }

    public void issueBook(int bookId, int memberId) throws BookNotAvailableException, DatabaseException {
        if (bookId <= 0 || memberId <= 0) {
            throw new DatabaseException("Book ID and Member ID must be positive numbers.");
        }

        String availabilitySql = "SELECT available_copies FROM books WHERE book_id = ? FOR UPDATE";
        String updateBookSql = "UPDATE books SET available_copies = available_copies - 1 WHERE book_id = ?";
        String insertTransactionSql = """
                INSERT INTO transactions (book_id, member_id, issue_date, due_date, fine_amount)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement availabilityStatement = connection.prepareStatement(availabilitySql);
                    PreparedStatement updateBookStatement = connection.prepareStatement(updateBookSql);
                    PreparedStatement insertTransactionStatement = connection.prepareStatement(insertTransactionSql)) {
                ensureMemberExists(connection, memberId);

                availabilityStatement.setInt(1, bookId);

                try (ResultSet resultSet = availabilityStatement.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new BookNotAvailableException("Book not found.");
                    }

                    if (resultSet.getInt("available_copies") <= 0) {
                        throw new BookNotAvailableException("No copies are currently available for this book.");
                    }
                }

                updateBookStatement.setInt(1, bookId);
                updateBookStatement.executeUpdate();

                LocalDate issueDate = LocalDate.now();
                LocalDate dueDate = issueDate.plusDays(LOAN_PERIOD_DAYS);

                insertTransactionStatement.setInt(1, bookId);
                insertTransactionStatement.setInt(2, memberId);
                insertTransactionStatement.setDate(3, Date.valueOf(issueDate));
                insertTransactionStatement.setDate(4, Date.valueOf(dueDate));
                insertTransactionStatement.setBigDecimal(5, BigDecimal.ZERO);
                insertTransactionStatement.executeUpdate();

                connection.commit();
            } catch (SQLException | BookNotAvailableException | DatabaseException e) {
                rollbackQuietly(connection);
                if (e instanceof BookNotAvailableException bookNotAvailableException) {
                    throw bookNotAvailableException;
                }
                if (e instanceof DatabaseException databaseException) {
                    throw databaseException;
                }
                throw new DatabaseException("Failed to issue the book.", e);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to issue the book.", e);
        }
    }

    public List<Transaction> getIssuedBooks(int memberId) throws DatabaseException {
        if (memberId <= 0) {
            throw new DatabaseException("Member ID must be a positive number.");
        }

        String sql = """
                SELECT t.transaction_id,
                       t.book_id,
                       t.member_id,
                       b.title AS book_title,
                       m.name AS member_name,
                       t.issue_date,
                       t.due_date,
                       t.return_date,
                       COALESCE(t.fine_amount, 0) AS fine_amount
                FROM transactions t
                JOIN books b ON b.book_id = t.book_id
                JOIN members m ON m.member_id = t.member_id
                WHERE t.member_id = ? AND t.return_date IS NULL
                ORDER BY t.due_date ASC, t.transaction_id DESC
                """;

        try (Connection connection = DatabaseConnection.getConnection()) {
            ensureMemberExists(connection, memberId);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, memberId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    List<Transaction> transactions = new ArrayList<>();
                    while (resultSet.next()) {
                        transactions.add(mapLoanTransaction(resultSet));
                    }
                    return transactions;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch issued books.", e);
        }
    }

    public List<Transaction> getReturnedTransactionsForMember(int memberId) throws DatabaseException {
        if (memberId <= 0) {
            throw new DatabaseException("Member ID must be a positive number.");
        }

        String sql = """
                SELECT t.transaction_id,
                       t.book_id,
                       t.member_id,
                       b.title AS book_title,
                       m.name AS member_name,
                       t.issue_date,
                       t.due_date,
                       t.return_date,
                       COALESCE(t.fine_amount, 0) AS fine_amount
                FROM transactions t
                JOIN books b ON b.book_id = t.book_id
                JOIN members m ON m.member_id = t.member_id
                WHERE t.member_id = ? AND t.return_date IS NOT NULL
                ORDER BY t.return_date DESC, t.transaction_id DESC
                """;

        try (Connection connection = DatabaseConnection.getConnection()) {
            ensureMemberExists(connection, memberId);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, memberId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    List<Transaction> transactions = new ArrayList<>();
                    while (resultSet.next()) {
                        transactions.add(mapLoanTransaction(resultSet));
                    }
                    return transactions;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch member loan history.", e);
        }
    }

    public BigDecimal returnBook(int transactionId) throws InvalidTransactionException, DatabaseException {
        if (transactionId <= 0) {
            throw new InvalidTransactionException("Transaction ID must be a positive number.");
        }

        String transactionSql = """
                SELECT book_id, due_date, return_date
                FROM transactions
                WHERE transaction_id = ? FOR UPDATE
                """;
        String updateTransactionSql = """
                UPDATE transactions
                SET return_date = ?, fine_amount = ?
                WHERE transaction_id = ?
                """;
        String updateBookSql = "UPDATE books SET available_copies = available_copies + 1 WHERE book_id = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement transactionStatement = connection.prepareStatement(transactionSql);
                    PreparedStatement updateTransactionStatement = connection.prepareStatement(updateTransactionSql);
                    PreparedStatement updateBookStatement = connection.prepareStatement(updateBookSql)) {
                transactionStatement.setInt(1, transactionId);

                int bookId;
                LocalDate dueDate;

                try (ResultSet resultSet = transactionStatement.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new InvalidTransactionException("Transaction not found.");
                    }

                    if (resultSet.getDate("return_date") != null) {
                        throw new InvalidTransactionException("This transaction has already been completed.");
                    }

                    bookId = resultSet.getInt("book_id");
                    dueDate = toLocalDate(resultSet.getDate("due_date"));
                }

                LocalDate returnedOn = LocalDate.now();
                BigDecimal fine = calculateFine(dueDate, returnedOn);

                updateTransactionStatement.setDate(1, Date.valueOf(returnedOn));
                updateTransactionStatement.setBigDecimal(2, fine);
                updateTransactionStatement.setInt(3, transactionId);
                updateTransactionStatement.executeUpdate();

                updateBookStatement.setInt(1, bookId);
                updateBookStatement.executeUpdate();

                connection.commit();
                return fine;
            } catch (SQLException | InvalidTransactionException e) {
                rollbackQuietly(connection);
                if (e instanceof InvalidTransactionException invalidTransactionException) {
                    throw invalidTransactionException;
                }
                throw new DatabaseException("Failed to return the book.", e);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to return the book.", e);
        }
    }

    private void ensureMemberExists(Connection connection, int memberId) throws SQLException, DatabaseException {
        String sql = "SELECT member_id FROM members WHERE member_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, memberId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new DatabaseException("Member not found. Create the member before issuing or viewing loans.");
                }
            }
        }
    }

    private Book mapBook(ResultSet resultSet) throws SQLException {
        return new Book(
                resultSet.getInt("book_id"),
                resultSet.getString("title"),
                resultSet.getString("author"),
                resultSet.getString("isbn"),
                resultSet.getInt("total_copies"),
                resultSet.getInt("available_copies"));
    }

    private Member mapMember(ResultSet resultSet) throws SQLException {
        return new Member(
                resultSet.getInt("member_id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("phone_number"),
                resultSet.getInt("active_loan_count"),
                defaultBigDecimal(resultSet.getBigDecimal("outstanding_fine")),
                defaultBigDecimal(resultSet.getBigDecimal("lifetime_fine_total")));
    }

    private Transaction mapLoanTransaction(ResultSet resultSet) throws SQLException {
        LocalDate dueDate = toLocalDate(resultSet.getDate("due_date"));
        LocalDate returnDate = toLocalDate(resultSet.getDate("return_date"));
        BigDecimal storedFine = defaultBigDecimal(resultSet.getBigDecimal("fine_amount"));
        BigDecimal resolvedFine =
                returnDate == null ? calculateFine(dueDate, LocalDate.now()) : storedFine;

        Transaction transaction = new Transaction(
                resultSet.getInt("transaction_id"),
                resultSet.getInt("book_id"),
                resultSet.getInt("member_id"),
                toLocalDate(resultSet.getDate("issue_date")),
                dueDate,
                returnDate,
                resolvedFine);
        transaction.setBookTitle(resultSet.getString("book_title"));
        transaction.setMemberName(resultSet.getString("member_name"));
        return transaction;
    }

    private BigDecimal calculateFine(LocalDate dueDate, LocalDate comparisonDate) {
        if (dueDate == null || comparisonDate == null || !comparisonDate.isAfter(dueDate)) {
            return BigDecimal.ZERO;
        }

        long lateDays = ChronoUnit.DAYS.between(dueDate, comparisonDate);
        return DAILY_FINE.multiply(BigDecimal.valueOf(lateDays));
    }

    private BigDecimal defaultBigDecimal(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private String sanitize(String value) {
        return value == null ? "" : value.trim();
    }

    private boolean isDuplicateEntry(SQLException exception) {
        return exception.getErrorCode() == 1062;
    }

    private LocalDate toLocalDate(Date date) {
        return date == null ? null : date.toLocalDate();
    }

    private void rollbackQuietly(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException ignored) {
            // Preserve the original exception.
        }
    }
}
