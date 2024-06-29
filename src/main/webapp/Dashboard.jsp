<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Dashboard - MangoEx</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f4f7f6;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
            margin: 0;
        }
        .content-wrapper {
            display: flex;
            flex: 1;
            padding-top: 56px; /* Adjust this value to match your header height */
        }
        .sidebar {
            width: 250px;
            background-color: #8f0b0b;
            color: #fff;
            padding-top: 20px;
            overflow-y: auto;
        }
        .sidebar .nav-link {
            color: #fff;
            padding: 12px 20px;
            transition: background-color 0.3s;
            font-size: 16px;
            display: flex;
            align-items: center;
        }
        .sidebar .nav-link:hover, .sidebar .nav-link.active {
            background-color: #b07b83;
        }
        .sidebar .nav-link i {
            font-size: 20px;
            margin-right: 10px;
            width: 30px;
            text-align: center;
        }
        .main-content {
            flex: 1;
            padding: 20px;
            overflow-y: auto;
        }
        .card {
            border: none;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s;
            height: 100%;
        }
        .card:hover {
            transform: translateY(-5px);
        }
        .star-rating {
            color: #ffc107;
            font-size: 24px;
        }
        .stat-card {
            text-align: center;
            padding: 20px;
        }
        .stat-card i {
            font-size: 48px;
            margin-bottom: 10px;
            color: #8f0b0b;
        }
        .stat-card .stat-value {
            font-size: 24px;
            font-weight: 600;
            margin-bottom: 5px;
        }
        .stat-card .stat-label {
            font-size: 14px;
            color: #6c757d;
        }
        footer {

            color: white;
            text-align: center;
            padding: 10px 0;
            position: relative;
            bottom: 0;
            width: 100%;
        }
    </style>
</head>
<body>
<jsp:include page="Header.jsp" />

<div class="content-wrapper">
    <div class="sidebar">
        <ul class="nav flex-column">
            <li class="nav-item">
                <a class="nav-link active" href="">
                    <i class="fas fa-tachometer-alt"></i>
                    <span>Dashboard</span>
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="UserItemsServlet">
                    <i class="fas fa-box-open mr-2"></i> My Items
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="AddItemServlet">
                    <i class="fas fa-plus-circle mr-2"></i> Add Item
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="">
                    <i class="fas fa-exchange-alt mr-2"></i> Exchanged Items
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="MessageServlet">
                    <i class="fas fa-envelope mr-2"></i> Messages
                </a>
            </li>
        </ul>
    </div>

    <div class="main-content">
        <div id="dashboard">
            <h2 class="mb-4">Welcome, ${sessionScope.user.username}!</h2>

            <div class="row mb-4">
                <div class="col-md-3 mb-4">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">Your Rating</h5>
                            <div class="star-rating">
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star-half-alt"></i>
                            </div>
                            <p class="card-text mt-2">4.5 out of 5 stars</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-3 mb-4">
                    <div class="card stat-card">
                        <i class="fas fa-list-ul"></i>
                        <div class="stat-value">12</div>
                        <div class="stat-label">Items Listed</div>
                    </div>
                </div>
                <div class="col-md-3 mb-4">
                    <div class="card stat-card">
                        <i class="fas fa-exchange-alt"></i>
                        <div class="stat-value">5</div>
                        <div class="stat-label">Exchanges Completed</div>
                    </div>
                </div>
                <div class="col-md-3 mb-4">
                    <div class="card stat-card">
                        <i class="fas fa-envelope"></i>
                        <div class="stat-value">3</div>
                        <div class="stat-label">Unread Messages</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="footer">
    <jsp:include page="Footer.jsp" />
</div>

<!-- Scripts -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

</body>
</html>