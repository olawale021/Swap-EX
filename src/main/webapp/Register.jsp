<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - SwapEx</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f4f7f6;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }
        .register-container {
            min-height: calc(80vh - 30px);
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }
        .register-card {
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            width: 100%;
            max-width: 900px;
            display: flex;
        }
        .register-image {
            flex: 1;
            background: url('https://res.cloudinary.com/dtvclnkeo/image/upload/v1719371344/swap_red_ay6raa.jpg') no-repeat center center;
            background-size: cover;
            min-height: 400px;
        }
        .register-form {
            flex: 1;
            padding: 40px;
            display: flex;
            flex-direction: column;
            justify-content: center;
        }
        .register-form h1 {
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
            .register-card {
                flex-direction: column;
            }
            .register-image {
                min-height: 200px;
            }
        }
    </style>
</head>
<body>
<header>
    <jsp:include page="Header.jsp" />
</header>
<div class="register-container">
    <div class="register-card">
        <div class="register-image"></div>
        <div class="register-form content">
            <h1>Create Account</h1>
            <form action="RegisterServlet" method="post">
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" class="form-control" id="username" name="username" required>
                </div>
                <div class="form-group">
                    <label for="phoneNumber">Phone Number</label>
                    <input type="number" class="form-control" id="phoneNumber" name="phoneNumber" required>
                </div>
                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                </div>
                <div class="form-group">
                    <label for="confirmPassword">Confirm Password</label>
                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                </div>
                <div class="form-group">
                    <label for="street">Street Address</label>
                    <input type="text" class="form-control" id="street" name="street" required>
                </div>
                <div class="form-group">
                    <label for="city">City</label>
                    <input type="text" class="form-control" id="city" name="city" required>
                </div>
                <div class="form-group">
                    <label for="state">State/Province</label>
                    <input type="text" class="form-control" id="state" name="state" required>
                </div>
                <div class="form-group">
                    <label for="zip">ZIP/Postal Code</label>
                    <input type="text" class="form-control" id="zip" name="zip" required>
                </div>
                <button type="submit" class="btn btn-primary btn-block">Register</button>
            </form>
            <div class="error-message">
                <c:if test="${not empty errorMessage}">
                    <p>${errorMessage}</p>
                </c:if>
            </div>
        </div>
    </div>
</div>

<jsp:include page="Footer.jsp" />

<!-- Bootstrap JS and dependencies -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script>
    // Client-side validation
    document.querySelector('form').addEventListener('submit', function(e) {
        var password = document.getElementById('password');
        var confirmPassword = document.getElementById('confirmPassword');
        if (password.value !== confirmPassword.value) {
            e.preventDefault();
            alert('Passwords do not match');
        }
    });
</script>
</body>
</html>