<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html lang="en">
<head>
    <title>Return Book | Library Desk</title>
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
            <p class="eyebrow">Returns</p>
            <h1 class="page-title">Return a book</h1>
            <p class="page-description">
                Close an existing transaction and calculate any overdue fine automatically.
            </p>
        </section>

        <section class="content-card split-layout">
            <div class="card-panel">
                <span class="feature-meta">Return flow</span>
                <h2 class="panel-title">Complete the transaction cleanly</h2>
                <p class="panel-copy">
                    Submit a transaction ID to mark the item as returned, store the final fine amount,
                    and increase the available copy count.
                </p>
                <div class="stack-list">
                    <div>
                        <strong>Transaction validation</strong>
                        <p>The system blocks missing or already completed returns.</p>
                    </div>
                    <div>
                        <strong>Inventory update</strong>
                        <p>The linked book becomes available again as soon as the return is accepted.</p>
                    </div>
                    <div>
                        <strong>Fine persistence</strong>
                        <p>Any overdue fee is stored with the transaction so member financial history stays visible.</p>
                    </div>
                </div>
            </div>

            <div class="card-panel">
                <c:if test="${not empty message}">
                    <div class="alert alert-success">${message}</div>
                </c:if>
                <c:if test="${not empty fine}">
                    <div class="alert alert-info">Overdue fine calculated: ${fine}</div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-error">${error}</div>
                </c:if>

                <form class="data-form" method="post" action="${pageContext.request.contextPath}/return">
                    <label class="field">
                        <span class="field-label">Transaction ID</span>
                        <input type="number" name="transactionId" placeholder="Enter a transaction number" required />
                        <span class="field-hint">Use the member activity or member directory pages if you need to look up active loans first.</span>
                    </label>

                    <button class="button button-primary" type="submit">Process Return</button>
                </form>
            </div>
        </section>
    </main>
</div>
</body>
</html>
