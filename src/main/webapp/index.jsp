<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MangoEx - Your Item Exchange Platform</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
        }
        .hero {
            background: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url('https://res.cloudinary.com/dtvclnkeo/image/upload/v1719371344/swap_red_ay6raa.jpg') no-repeat center center;
            background-size: cover;
            height: 70vh; /* Changed from 70vh to 100vh */
            display: flex;
            align-items: center;
            justify-content: center; /* Added to center content horizontally */
            color: white;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.5);
            position: relative; /* Added to allow absolute positioning of child elements if needed */
        }

        .hero .container {
            z-index: 1; /* Ensure the content is above the background */
        }

        .features {
            padding: 4rem 0;
            background-color: #f8f9fa;
        }
        .feature-icon {
            font-size: 3rem;
            margin-bottom: 1rem;
            color: #8f0b0b;
        }
        .btn-primary {
            background-color: #8f0b0b !important;
            border-color: #8f0b0b !important;
        }
        .btn-primary:hover {
            background-color: #b07b83 !important;
            border-color: #b07b83 !important;
        }
    </style>
</head>
<body>
<jsp:include page="Header.jsp" />

<main>
    <section class="hero">
        <div class="container text-center">
            <h1 class="display-4">Swap Smarter with SwapEx</h1>
            <p class="lead">Your trusted platform for exchanging electronics, gadgets, clothing, vehicles, and more.</p>
            <a href="Register.jsp" class="btn btn-primary btn-lg mt-3">Start Swapping</a>
        </div>
    </section>

    <section class="features">
        <div class="container">
            <h2 class="text-center mb-5">Why Choose MangoEx?</h2>
            <div class="row">
                <div class="col-md-4 text-center">
                    <div class="feature-icon">
                        <i class="fas fa-sync-alt"></i>
                    </div>
                    <h3>Easy Exchanges</h3>
                    <p>Our intuitive platform makes finding and swapping items a breeze.</p>
                </div>
                <div class="col-md-4 text-center">
                    <div class="feature-icon">
                        <i class="fas fa-shield-alt"></i>
                    </div>
                    <h3>Secure Transactions</h3>
                    <p>Advanced verification ensures safe and trustworthy exchanges.</p>
                </div>
                <div class="col-md-4 text-center">
                    <div class="feature-icon">
                        <i class="fas fa-globe"></i>
                    </div>
                    <h3>Diverse Selection</h3>
                    <p>From electronics to vehicles, find exactly what you're looking for.</p>
                </div>
            </div>
        </div>
    </section>

    <section class="cta bg-light py-5">
        <div class="container text-center">
            <h2>Ready to Transform Your Unused Items?</h2>
            <p class="lead">Join thousands of happy swappers in the MangoEx community.</p>
            <a href="all-items" class="btn btn-primary btn-lg mt-3">Explore Available Items</a>
        </div>
    </section>
</main>

<jsp:include page="Footer.jsp" />

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

</body>
</html>