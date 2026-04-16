<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html lang="en">
<head>
    <title>Issued Books | Library Desk</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css" />
</head>
<body>
<div class="app-shell">
    <header class="app-header">
        <a class="brand" href="${pageContext.request.contextPath}/">
            <span class="brand-mark">L</span>
            <span class="brand-copy">
                <span class="brand-label">Library Desk</span>
                <span class="brand-subtitle">Operations Console</span>
            </span>
        </a>
        <nav class="app-nav">
            <a class="nav-link" href="${pageContext.request.contextPath}/books">Catalog</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/members">Members</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/issue">Issue</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/return">Return</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/issued">Member Activity</a>
        </nav>
    </header>

    <main class="page-stack">
        <section class="page-banner">
            <p class="eyebrow">Member activity</p>
            <h1 class="page-title">View issued books</h1>
            <p class="page-description">
                Look up the active loans for a member before issuing follow-up items or processing returns.
            </p>
        </section>

        <section class="content-card split-layout">
            <div class="card-panel">
                <span class="feature-meta">Lookup</span>
                <h2 class="panel-title">Search active loans by member</h2>
                <p class="panel-copy">
                    Enter a member ID to review all open transactions that have not been returned yet.
                </p>

                <form class="data-form" method="post" action="${pageContext.request.contextPath}/issued">
                    <label class="field">
                        <span class="field-label">Member ID</span>
                        <input type="number" name="memberId" placeholder="Enter a member number" required />
                        <span class="field-hint">Need more member context? Open the member directory for a full profile and loan history.</span>
                    </label>

                    <button class="button button-primary" type="submit">Show Issued Books</button>
                </form>
            </div>

            <div class="card-panel">
                <c:if test="${not empty error}">
                    <div class="alert alert-error">${error}</div>
                </c:if>

                <c:choose>
                    <c:when test="${not empty selectedMember}">
                        <span class="feature-meta">Selected member</span>
                        <h2 class="panel-title">${selectedMember.name}</h2>
                        <p class="panel-copy">
                            ${selectedMember.email} | ${selectedMember.phoneNumber}
                        </p>
                        <div class="summary-grid">
                            <div class="metric-card">
                                <span class="metric-label">Member ID</span>
                                <strong class="metric-value">${selectedMember.memberId}</strong>
                                <p>Circulation record reference for this borrower.</p>
                            </div>
                            <div class="metric-card">
                                <span class="metric-label">Active loans</span>
                                <strong class="metric-value">${selectedMember.activeLoanCount}</strong>
                                <p>Open transactions that still need to be returned.</p>
                            </div>
                            <div class="metric-card">
                                <span class="metric-label">Current fine exposure</span>
                                <strong class="metric-value">${selectedMember.outstandingFine}</strong>
                                <p>Live overdue amount across active loans as of today.</p>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state">
                            <h3>No member selected yet</h3>
                            <p>Enter a member ID to review their active loans and due dates.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>

        <c:if test="${not empty selectedMember}">
            <section class="content-card">
                <div class="panel-heading">
                    <div>
                        <span class="feature-meta">Open transactions</span>
                        <h2 class="panel-title">Issued books for ${selectedMember.name}</h2>
                    </div>
                    <p class="supporting-note">Live fine values update from the current due date when a loan is still open.</p>
                </div>

                <c:choose>
                    <c:when test="${not empty transactions}">
                        <div class="table-wrap">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Transaction ID</th>
                                        <th>Book ID</th>
                                        <th>Title</th>
                                        <th>Issue Date</th>
                                        <th>Due Date</th>
                                        <th>Current Fine</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${transactions}" var="txn">
                                        <tr>
                                            <td>${txn.transactionId}</td>
                                            <td>${txn.bookId}</td>
                                            <td>${txn.bookTitle}</td>
                                            <td>${txn.issueDate}</td>
                                            <td>${txn.dueDate}</td>
                                            <td>${txn.fine}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state">
                            <h3>No books currently issued</h3>
                            <p>This member does not have any active loans at the moment.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </section>
        </c:if>
    </main>
</div>
</body>
</html>
