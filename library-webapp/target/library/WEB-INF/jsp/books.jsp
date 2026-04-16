<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html lang="en">
<head>
    <title>Available Books | Library Desk</title>
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
            <p class="eyebrow">Catalog</p>
            <h1 class="page-title">Available books</h1>
            <p class="page-description">
                A live view of lendable inventory and copy availability across the library.
            </p>
        </section>

        <section class="content-card">
            <div class="panel-heading">
                <div>
                    <span class="feature-meta">Inventory table</span>
                    <h2 class="panel-title">Shelf-ready titles</h2>
                </div>
                <p class="supporting-note">Only books with at least one available copy appear here.</p>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-error">${error}</div>
            </c:if>

            <c:choose>
                <c:when test="${not empty books}">
                    <div class="table-wrap">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Title</th>
                                    <th>Author</th>
                                    <th>ISBN</th>
                                    <th>Availability</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${books}" var="book">
                                    <tr>
                                        <td>${book.bookId}</td>
                                        <td>${book.title}</td>
                                        <td>${book.author}</td>
                                        <td>${book.isbn}</td>
                                        <td><span class="availability-pill">${book.availableCopies} of ${book.totalCopies} available</span></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <h3>No books available right now</h3>
                        <p>Once inventory has lendable copies, it will appear here automatically.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>
    </main>
</div>
</body>
</html>
