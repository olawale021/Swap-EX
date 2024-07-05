<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Footer</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <style>
        .footer {
            background-color: #f8f9fa;
            color: #333;
            font-family: 'Poppins', sans-serif;
            padding: 60px 0 30px;
            border-top: 5px solid #8f0b0b;
        }
        .footer-logo {
            font-size: 28px;
            font-weight: 600;
            color: #8f0b0b;
            margin-bottom: 20px;
        }
        .footer h5 {
            color: #8f0b0b;
            font-weight: 600;
            margin-bottom: 20px;
            font-size: 18px;
        }
        .footer-links {
            list-style: none;
            padding: 0;
        }
        .footer-links li {
            margin-bottom: 10px;
        }
        .footer-links a {
            color: #555;
            text-decoration: none;
            transition: color 0.3s ease;
        }
        .footer-links a:hover {
            color: #8f0b0b;
        }
        .social-icons {
            margin-top: 20px;
        }
        .social-icons a {
            color: #8f0b0b;
            margin-right: 15px;
            font-size: 20px;
            transition: color 0.3s ease;
        }
        .social-icons a:hover {
            color: #b07b83;
        }
        .footer-bottom {
            background-color: #f1f1f1;
            color: #666;
            padding: 20px 0;
            font-size: 14px;
            margin-top: 40px;
        }
        .footer-bottom a {
            color: #8f0b0b;
            text-decoration: none;
        }
        .footer-bottom a:hover {
            text-decoration: underline;
        }
        .footer-logo {
            font-size: 28px;
            font-weight: 600;
            color: #8f0b0b;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
        }
        .footer-logo img {
            height: 24px;
            width: auto;
            margin: 0 5px;
            vertical-align: middle;
        }
    </style>
</head>
<body>
<footer class="footer">
    <div class="container">
        <div class="row">
            <div class="col-lg-4 col-md-6 mb-4 mb-md-0">
                <div class="footer-logo">Swap<img src="https://res.cloudinary.com/dtvclnkeo/image/upload/v1719366219/swap-svgrepo-com_ac15sp.svg" alt="Logo">Ex</div>
                <p>Your trusted platform for exchanging electronics, gadgets, clothing, vehicles, and more. Experience seamless item swaps with MangoEx.</p>
                <div class="social-icons">
                    <a href="#"><i class="fab fa-facebook-f"></i></a>
                    <a href="#"><i class="fab fa-twitter"></i></a>
                    <a href="#"><i class="fab fa-linkedin-in"></i></a>
                    <a href="#"><i class="fab fa-instagram"></i></a>
                </div>
            </div>
            <div class="col-lg-2 col-md-6 mb-4 mb-md-0">
                <h5>Exchange Categories</h5>
                <ul class="footer-links">
                    <li><a href="#">Electronics</a></li>
                    <li><a href="#">Gadgets</a></li>
                    <li><a href="#">Clothing</a></li>
                    <li><a href="#">Vehicles</a></li>
                </ul>
            </div>
            <div class="col-lg-2 col-md-6 mb-4 mb-md-0">
                <h5>Company</h5>
                <ul class="footer-links">
                    <li><a href="#">About Us</a></li>
                    <li><a href="#">How It Works</a></li>
                    <li><a href="#">News & Updates</a></li>
                    <li><a href="#">Partnerships</a></li>
                </ul>
            </div>
            <div class="col-lg-2 col-md-6 mb-4 mb-md-0">
                <h5>Support</h5>
                <ul class="footer-links">
                    <li><a href="#">Help Center</a></li>
                    <li><a href="#">Contact Us</a></li>
                    <li><a href="#">FAQs</a></li>
                    <li><a href="#">Safety Tips</a></li>
                </ul>
            </div>
            <div class="col-lg-2 col-md-6">
                <h5>Legal</h5>
                <ul class="footer-links">
                    <li><a href="#">Privacy Policy</a></li>
                    <li><a href="#">Terms of Service</a></li>
                    <li><a href="#">User Agreement</a></li>
                    <li><a href="#">Cookie Policy</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="footer-bottom">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-md-6">
                    <p class="mb-0">&copy; 2024 SwapEx. All rights reserved.</p>
                </div>
                <div class="col-md-6 text-md-right">
                    <a href="#">Sitemap</a> | <a href="#">Accessibility</a> | <a href="#">Do Not Sell My Personal Information</a>
                </div>
            </div>
        </div>
    </div>
</footer>

<!-- Bootstrap JS and dependencies -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>