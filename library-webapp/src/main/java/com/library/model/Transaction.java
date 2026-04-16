package com.library.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {
    private int transactionId;
    private int bookId;
    private int memberId;
    private String bookTitle;
    private String memberName;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private BigDecimal fine = BigDecimal.ZERO;

    public Transaction() {
    }

    public Transaction(
            int transactionId,
            int bookId,
            int memberId,
            LocalDate issueDate,
            LocalDate dueDate,
            LocalDate returnDate,
            BigDecimal fine) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.fine = fine == null ? BigDecimal.ZERO : fine;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public BigDecimal getFine() {
        return fine;
    }

    public void setFine(BigDecimal fine) {
        this.fine = fine == null ? BigDecimal.ZERO : fine;
    }

    public boolean isReturned() {
        return returnDate != null;
    }
}
