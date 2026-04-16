package com.library.servlet;

import com.library.exception.DatabaseException;
import com.library.exception.InvalidTransactionException;
import com.library.service.LibraryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/return")
public class ReturnBookServlet extends HttpServlet {
    private final LibraryService service = new LibraryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/returnForm.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        try {
            int transactionId = Integer.parseInt(req.getParameter("transactionId"));
            BigDecimal fine = service.returnBook(transactionId);
            req.setAttribute("message", "Book returned successfully!");
            if (fine.compareTo(BigDecimal.ZERO) > 0) {
                req.setAttribute("fine", fine);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Transaction ID must be a valid number.");
        } catch (InvalidTransactionException | DatabaseException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/jsp/returnForm.jsp").forward(req, resp);
    }
}
