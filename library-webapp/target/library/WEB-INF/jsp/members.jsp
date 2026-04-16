<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html lang="en">
<head>
    <title>Members | Library Desk</title>
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
            <p class="eyebrow">Members</p>
            <h1 class="page-title">Manage library members</h1>
            <p class="page-description">
                Register borrowers, inspect current loans, and review both live and historical fine totals.
            </p>
        </section>

        <section class="content-card split-layout">
            <div class="card-panel">
                <span class="feature-meta">Register member</span>
                <h2 class="panel-title">Add a borrower</h2>
                <p class="panel-copy">
                    Create members here before issuing books so all circulation and fine history stays attached to a profile.
                </p>

                <c:if test="${not empty message}">
                    <div class="alert alert-success">${message}</div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-error">${error}</div>
                </c:if>

                <form class="data-form" method="post" action="${pageContext.request.contextPath}/members">
                    <input type="hidden" name="action" value="create" />

                    <label class="field">
                        <span class="field-label">Member name</span>
                        <input type="text" name="name" placeholder="Enter full name" value="${formName}" required />
                        <span class="field-hint">Use the borrower's full display name for staff-facing records.</span>
                    </label>

                    <label class="field">
                        <span class="field-label">Email</span>
                        <input type="email" name="email" placeholder="Enter email address" value="${formEmail}" required />
                        <span class="field-hint">Email addresses should be unique for each member profile.</span>
                    </label>

                    <label class="field">
                        <span class="field-label">Phone number</span>
                        <input type="text" name="phoneNumber" placeholder="Enter contact number" value="${formPhoneNumber}" required />
                        <span class="field-hint">This helps staff follow up about overdue loans and returns.</span>
                    </label>

                    <button class="button button-primary" type="submit">Add Member</button>
                </form>
            </div>

            <div class="card-panel">
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
                                <p>Reference number used during issue and reporting flows.</p>
                            </div>
                            <div class="metric-card">
                                <span class="metric-label">Active loans</span>
                                <strong class="metric-value">${selectedMember.activeLoanCount}</strong>
                                <p>Open transactions still assigned to this member.</p>
                            </div>
                            <div class="metric-card">
                                <span class="metric-label">Outstanding fine</span>
                                <strong class="metric-value">${selectedMember.outstandingFine}</strong>
                                <p>Live overdue amount across all active loans as of today.</p>
                            </div>
                            <div class="metric-card">
                                <span class="metric-label">Lifetime fines</span>
                                <strong class="metric-value">${selectedMember.lifetimeFineTotal}</strong>
                                <p>Total fine amount stored on completed return transactions.</p>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state">
                            <h3>Select a member profile</h3>
                            <p>Use the directory below to open a borrower profile with active loans, fine exposure, and return history.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>

        <section class="content-card">
            <div class="panel-heading">
                <div>
                    <span class="feature-meta">Member directory</span>
                    <h2 class="panel-title">Registered borrowers</h2>
                </div>
                <p class="supporting-note">Open a profile to inspect current loans and historical fine totals.</p>
            </div>

            <c:choose>
                <c:when test="${not empty members}">
                    <div class="table-wrap">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Email</th>
                                    <th>Phone</th>
                                    <th>Active Loans</th>
                                    <th>Outstanding Fine</th>
                                    <th>Lifetime Fines</th>
                                    <th>Profile</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${members}" var="member">
                                    <tr>
                                        <td>${member.memberId}</td>
                                        <td>${member.name}</td>
                                        <td>${member.email}</td>
                                        <td>${member.phoneNumber}</td>
                                        <td>${member.activeLoanCount}</td>
                                        <td>${member.outstandingFine}</td>
                                        <td>${member.lifetimeFineTotal}</td>
                                        <td>
                                            <a class="table-action" href="${pageContext.request.contextPath}/members?memberId=${member.memberId}">
                                                Open Profile
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <h3>No members registered yet</h3>
                        <p>Add the first borrower above to start issuing books and tracking fines.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>

        <c:if test="${not empty selectedMember}">
            <section class="content-card">
                <div class="panel-heading">
                    <div>
                        <span class="feature-meta">Current loans</span>
                        <h2 class="panel-title">Open transactions for ${selectedMember.name}</h2>
                    </div>
                    <p class="supporting-note">Current fine values rise automatically if the due date has passed.</p>
                </div>

                <c:choose>
                    <c:when test="${not empty currentLoans}">
                        <div class="table-wrap">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Transaction ID</th>
                                        <th>Book ID</th>
                                        <th>Title</th>
                                        <th>Issue Date</th>
                                        <th>Due Date</th>
                                        <th>Status</th>
                                        <th>Current Fine</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${currentLoans}" var="txn">
                                        <tr>
                                            <td>${txn.transactionId}</td>
                                            <td>${txn.bookId}</td>
                                            <td>${txn.bookTitle}</td>
                                            <td>${txn.issueDate}</td>
                                            <td>${txn.dueDate}</td>
                                            <td><span class="status-chip status-open">Open</span></td>
                                            <td>${txn.fine}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state">
                            <h3>No active loans</h3>
                            <p>This member currently has no books checked out.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </section>

            <section class="content-card">
                <div class="panel-heading">
                    <div>
                        <span class="feature-meta">Loan history</span>
                        <h2 class="panel-title">Completed returns for ${selectedMember.name}</h2>
                    </div>
                    <p class="supporting-note">Final fine amounts are stored when a book is returned.</p>
                </div>

                <c:choose>
                    <c:when test="${not empty loanHistory}">
                        <div class="table-wrap">
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Transaction ID</th>
                                        <th>Book ID</th>
                                        <th>Title</th>
                                        <th>Issue Date</th>
                                        <th>Due Date</th>
                                        <th>Return Date</th>
                                        <th>Status</th>
                                        <th>Final Fine</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${loanHistory}" var="txn">
                                        <tr>
                                            <td>${txn.transactionId}</td>
                                            <td>${txn.bookId}</td>
                                            <td>${txn.bookTitle}</td>
                                            <td>${txn.issueDate}</td>
                                            <td>${txn.dueDate}</td>
                                            <td>${txn.returnDate}</td>
                                            <td><span class="status-chip status-closed">Returned</span></td>
                                            <td>${txn.fine}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state">
                            <h3>No completed returns yet</h3>
                            <p>Once this member returns books, the transaction history and stored fine totals will appear here.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </section>
        </c:if>
    </main>
</div>
</body>
</html>
