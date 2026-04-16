<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <title>Library Desk</title>
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
        <section class="hero-panel">
            <div class="hero-copy">
                <p class="eyebrow">Modern circulation workspace</p>
                <h1 class="hero-title">Run daily library operations from one polished control center.</h1>
                <p class="hero-text">
                    Review inventory, register members, issue titles, process returns, and track fine exposure
                    through a single professional workspace built for library staff.
                </p>
                <div class="action-row">
                    <a class="button button-primary" href="${pageContext.request.contextPath}/members">Open Member Hub</a>
                    <a class="button button-secondary" href="${pageContext.request.contextPath}/books">Browse Catalog</a>
                </div>
            </div>
            <div class="hero-sidebar">
                <div class="stat-card">
                    <span class="stat-label">Loan policy</span>
                    <strong>14 day cycle</strong>
                    <p>New issues automatically receive a due date using the current server-side lending policy.</p>
                </div>
                <div class="stat-card">
                    <span class="stat-label">Core workflows</span>
                    <strong>5 staff tools</strong>
                    <p>Catalog lookup, member management, issue, return, and loan-history tracking in one place.</p>
                </div>
            </div>
        </section>

        <section class="feature-grid">
            <a class="feature-card" href="${pageContext.request.contextPath}/books">
                <span class="feature-meta">Catalog</span>
                <h2>Available Books</h2>
                <p>See lendable inventory and current copy availability in a clean, readable table view.</p>
            </a>
            <a class="feature-card" href="${pageContext.request.contextPath}/members">
                <span class="feature-meta">Members</span>
                <h2>Member Directory</h2>
                <p>Register borrowers, review current loans, and monitor outstanding fine exposure by member.</p>
            </a>
            <a class="feature-card" href="${pageContext.request.contextPath}/issue">
                <span class="feature-meta">Circulation</span>
                <h2>Issue A Book</h2>
                <p>Assign titles to members with quick numeric entry and immediate validation feedback.</p>
            </a>
            <a class="feature-card" href="${pageContext.request.contextPath}/return">
                <span class="feature-meta">Returns</span>
                <h2>Process Returns</h2>
                <p>Close transactions clearly and surface any overdue fine on the same screen.</p>
            </a>
            <a class="feature-card" href="${pageContext.request.contextPath}/issued">
                <span class="feature-meta">Member View</span>
                <h2>Issued Books</h2>
                <p>Check what a member currently has on loan before follow-up or return processing.</p>
            </a>
        </section>
    </main>
</div>
</body>
</html>
