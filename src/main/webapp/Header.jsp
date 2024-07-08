<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Header</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">

</head>
<body>
<nav class="navbar navbar-expand-lg fixed-top">
    <div class="container">
        <a class="navbar-brand" href="index.jsp">SWAP <img src="https://res.cloudinary.com/dtvclnkeo/image/upload/v1719366219/swap-svgrepo-com_ac15sp.svg" alt="Logo"> EX</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item"><a class="nav-link" href="index.jsp">Home</a></li>
                <li class="nav-item"><a class="nav-link" href="AllItemsServlet">All Items</a></li>
                <li class="nav-item"><a class="nav-link" href="PredictServlet">Predict Category</a></li>
                <li class="nav-item"><a class="nav-link" href="#">How It Works</a></li>
                <li class="nav-item"><a class="nav-link" href="#">Contact</a></li>
                <c:choose>
                    <c:when test="${not empty user}">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <c:out value="${user.username}" />
                            </a>
                            <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                                <a class="dropdown-item" href="<c:url value='/settings.jsp' />">Settings</a>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item" href="<c:url value='/Dashboard.jsp' />">Dashboard</a>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item" href="<c:url value='/api/users/logout' />">Logout</a>
                            </div>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item"><a class="nav-link" href="Login.jsp">Login</a></li>
                        <li class="nav-item"><a class="nav-link btn btn-primary text-white" href="Register.jsp">Get Started</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

<!-- Bootstrap JS and dependencies -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
