<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <title>Your Exchanges</title>
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f9f9f9;
            color: #333;
        }

        .header-wrapper {
            position: relative;
            z-index: 1000;
        }

        .content-wrapper {
            display: flex;
            min-height: 100vh;
            margin-top: 60px;
        }

        .sidebar {
            width: 250px;
            background-color: #8f0b0b;
            color: #fff !important;
            padding: 20px;
            position: fixed;
            top: 60px;
            bottom: 0;
            left: 0;
            overflow-y: auto;
        }
        .nav-link{
            list-style: none;
            color: #cacfdd;!important;
        }

        .main-content {
            margin-left: 250px;
            padding: 30px;
            flex-grow: 1;
            background-color: #f9f9f9;
        }

        h1 {
            color: #8f0b0b;
            margin-bottom: 20px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background-color: #fff;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        table th, table td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #e0e0e0;
        }

        table th {
            background-color: #f2e6e7;
            color: #8f0b0b;
            font-weight: bold;
        }

        table tr:nth-child(even) {
            background-color: #faf6f6;
        }

        .no-exchanges {
            font-size: 18px;
            color: #666;
            margin-top: 20px;
        }

        .view-link {
            text-decoration: none;
            color: #8f0b0b;
            font-weight: bold;
            transition: color 0.3s;
        }

        .view-link:hover {
            color: #b07b83;
        }
    </style>
</head>
<body>

<div class="header-wrapper">
    <jsp:include page="Header.jsp" />
</div>

<div class="content-wrapper">
    <div class="sidebar">
        <ul class="nav flex-column">
            <li class="nav-item">
                <a class="nav-link" href="UserDashboard.jsp">
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
                <a class="nav-link active" href="#">
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
        <h1>Your Exchanges</h1>

        <c:choose>
            <c:when test="${empty userExchanges}">
                <p class="no-exchanges">You have no exchanges at the moment.</p>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                    <tr>
                        <th>Item Title</th>
                        <th>Owner</th>
                        <th>Interested User</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="exchange" items="${userExchanges}">
                        <tr>
                            <td>${exchange.itemTitle}</td>
                            <td>${exchange.ownerUsername}</td>
                            <td>${exchange.interestedUserUsername}</td>
                            <td>${exchange.status}</td>
                            <td>
                                <a class="view-link" href="MessageServlet?exchangeId=${exchange.id}">View Conversation</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
