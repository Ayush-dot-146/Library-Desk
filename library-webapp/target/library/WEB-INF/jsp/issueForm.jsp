<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html lang="en">
<head>
    <title>Issue Book | Library Desk</title>
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
            <p class="eyebrow">Circulation</p>
            <h1 class="page-title">Issue a book</h1>
            <p class="page-description">
                Create a new lending transaction by assigning an available book to an existing member.
            </p>
        </section>

        <section class="content-card split-layout">
            <div class="card-panel">
                <span class="feature-meta">How it works</span>
                <h2 class="panel-title">Create a new loan transaction</h2>
                <p class="panel-copy">
                    Enter a book ID and member ID. The system checks availability, validates the member,
                    and generates the due date automatically.
                </p>
                <div class="stack-list">
                    <div>
                        <strong>Availability check</strong>
                        <p>Issues are blocked if the requested book has no copies ready to lend.</p>
                    </div>
                    <div>
                        <strong>Member validation</strong>
                        <p>Only registered members can borrow titles, which keeps circulation records consistent.</p>
                    </div>
                    <div>
                        <strong>Automatic due date</strong>
                        <p>New loans follow the current loan window configured on the server.</p>
                    </div>
                </div>
            </div>

            <div class="card-panel">
                <c:if test="${not empty message}">
                    <div class="alert alert-success">${message}</div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-error">${error}</div>
                </c:if>

                <form class="data-form" method="post" action="${pageContext.request.contextPath}/issue">
                    <label class="field">
                        <span class="field-label">Book ID</span>
                        <input type="number" name="bookId" placeholder="Enter a book number" required />
                        <span class="field-hint">Use the catalog page if you need to look up available titles first.</span>
                    </label>

                    <label class="field">
                        <span class="field-label">Member ID</span>
                        <input type="number" name="memberId" placeholder="Enter a member number" required />
                        <span class="field-hint">Need a new borrower? Create them first in the member directory.</span>
                    </label>

                    <button class="button button-primary" type="submit">Issue Book</button>
                </form>
            </div>
        </section>
    </main>
</div>
</body>
</html>
