<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - SwapEX</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f4f7f6;
        }
        .login-container {
            min-height: calc(60vh - 30px);
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }
        .login-card {
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            width: 100%;
            max-width: 900px;
            display: flex;
        }
        .login-image {
            flex: 1;
            background: url('https://res.cloudinary.com/dtvclnkeo/image/upload/v1719371344/swap_red_ay6raa.jpg') no-repeat center center;
            background-size: cover;
            min-height: 400px;
        }
        .login-form {
            flex: 1;
            padding: 40px;
            display: flex;
            flex-direction: column;
            justify-content: center;
        }
        .login-form h1 {
            margin-bottom: 30px;
            font-size: 28px;
            font-weight: 600;
            color: #333;
        }
        .form-group label {
            font-weight: 500;
            color: #555;
        }
        .form-control {
            border-radius: 8px;
            padding: 12px;
            border: 1px solid #ddd;
        }
        .btn-primary {
            background-color: #8f0b0b !important;
            border: none;
            border-radius: 8px;
            padding: 12px;
            font-weight: 500;
            transition: background-color 0.3s ease;
        }
        .btn-primary:hover {
            background-color: #b07b83 !important;
        }
        .error-message {
            color: #dc3545;
            font-size: 14px;
            margin-top: 15px;
        }
        @media (max-width: 768px) {
            .login-card {
                flex-direction: column;
            }
            .login-image {
                min-height: 200px;
            }
        }
    </style>
</head>
<body>
<header>
    <jsp:include page="Header.jsp" />
</header>
<div class="login-container">
    <div class="login-card">
        <div class="login-image"></div>
        <div class="login-form">
            <h1>Welcome Back</h1>
            <form action="LoginServlet" method="post">
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" class="form-control" id="username" name="username" required>
                </div>
                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                </div>
                <button type="submit" class="btn btn-primary btn-block">Log In</button>
            </form>
            <div class="error-message">
                <c:if test="${not empty errorMessage}">
                    <p>${errorMessage}</p>
                </c:if>
            </div>
        </div>
    </div>
</div>

<section>
    <jsp:include page="Footer.jsp" />
</section>
<!-- Bootstrap JS and dependencies -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>