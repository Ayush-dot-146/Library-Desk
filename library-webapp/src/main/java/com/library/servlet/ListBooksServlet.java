package com.library.servlet;

import com.library.exception.DatabaseException;
import com.library.model.Book;
import com.library.service.LibraryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/books")
public class ListBooksServlet extends HttpServlet {
    private final LibraryService service = new LibraryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        try {
            List<Book> books = service.getAvailableBooks();
            req.setAttribute("books", books);
        } catch (DatabaseException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/jsp/books.jsp").forward(req, resp);
    }
}
