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

@WebServlet("/members")
public class MembersServlet extends HttpServlet {
    private final LibraryService service = new LibraryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            loadMembers(req);
            loadSelectedMember(req);
        } catch (DatabaseException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/jsp/members.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("create".equalsIgnoreCase(action)) {
                Member member = new Member();
                member.setName(req.getParameter("name"));
                member.setEmail(req.getParameter("email"));
                member.setPhoneNumber(req.getParameter("phoneNumber"));

                int newMemberId = service.addMember(member);
                req.setAttribute("message", "Member added successfully.");
                req.setAttribute("memberId", newMemberId);
            }

            loadMembers(req);
            loadSelectedMember(req);
        } catch (DatabaseException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("formName", req.getParameter("name"));
            req.setAttribute("formEmail", req.getParameter("email"));
            req.setAttribute("formPhoneNumber", req.getParameter("phoneNumber"));
            try {
                loadMembers(req);
                loadSelectedMember(req);
            } catch (DatabaseException ignored) {
                // Preserve the original error shown to the user.
            }
        }

        req.getRequestDispatcher("/WEB-INF/jsp/members.jsp").forward(req, resp);
    }

    private void loadMembers(HttpServletRequest req) throws DatabaseException {
        req.setAttribute("members", service.getAllMembers());
    }

    private void loadSelectedMember(HttpServletRequest req) throws DatabaseException {
        String memberIdText = req.getParameter("memberId");
        Object selectedFromPost = req.getAttribute("memberId");
        if ((memberIdText == null || memberIdText.isBlank()) && selectedFromPost != null) {
            memberIdText = String.valueOf(selectedFromPost);
        }

        if (memberIdText == null || memberIdText.isBlank()) {
            return;
        }

        int memberId;
        try {
            memberId = Integer.parseInt(memberIdText);
        } catch (NumberFormatException e) {
            throw new DatabaseException("Member ID must be a valid number.");
        }

        Member selectedMember = service.getMember(memberId);
        List<Transaction> currentLoans = service.getIssuedBooks(memberId);
        List<Transaction> loanHistory = service.getReturnedTransactionsForMember(memberId);

        req.setAttribute("memberId", memberId);
        req.setAttribute("selectedMember", selectedMember);
        req.setAttribute("currentLoans", currentLoans);
        req.setAttribute("loanHistory", loanHistory);
    }
}
