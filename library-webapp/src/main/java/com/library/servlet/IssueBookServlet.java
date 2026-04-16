package com.library.servlet;

import com.library.exception.BookNotAvailableException;
import com.library.exception.DatabaseException;
import com.library.service.LibraryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/issue")
public class IssueBookServlet extends HttpServlet {
    private final LibraryService service = new LibraryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/issueForm.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int bookId = Integer.parseInt(req.getParameter("bookId"));
            int memberId = Integer.parseInt(req.getParameter("memberId"));
            service.issueBook(bookId, memberId);
            req.setAttribute("message", "Book issued successfully!");
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Book ID and Member ID must be valid numbers.");
        } catch (BookNotAvailableException | DatabaseException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/jsp/issueForm.jsp").forward(req, resp);
    }
}
