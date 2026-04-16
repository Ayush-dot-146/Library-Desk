package com.library.servlet;

import com.library.exception.DatabaseException;
import com.library.model.Member;
import com.library.model.Transaction;
import com.library.service.LibraryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/issued")
public class IssuedBooksServlet extends HttpServlet {
    private final LibraryService service = new LibraryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/issuedBooks.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int memberId = Integer.parseInt(req.getParameter("memberId"));
            Member member = service.getMember(memberId);
            List<Transaction> transactions = service.getIssuedBooks(memberId);
            req.setAttribute("selectedMember", member);
            req.setAttribute("transactions", transactions);
            req.setAttribute("memberId", memberId);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Member ID must be a valid number.");
        } catch (DatabaseException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/jsp/issuedBooks.jsp").forward(req, resp);
    }
}
