<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Item Category Predictor</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">
    <style>
        :root {
            --primary-color: #8f0b0b;
            --secondary-color: #f4f4f4;
            --text-color: #333;
            --border-color: #ddd;
        }
        .main-content {
            font-family: 'Poppins', sans-serif;
            line-height: 1.6;
            color: var(--text-color);
            background-color: var(--secondary-color);
            padding: 40px 0;
            margin-top: 80px;
        }

        .main-content .container {
            max-width: 800px;
            margin: 0 auto;
            padding: 0 20px;
        }
        .main-content h1, .main-content h2, .main-content h3 {
            color: var(--primary-color);
        }
        .main-content .card {
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            padding: 30px;
            margin-bottom: 30px;
        }
        .main-content form {
            display: grid;
            gap: 20px;
        }
        .main-content label {
            font-weight: 600;
            margin-bottom: 5px;
            display: block;
        }
        .main-content input[type="text"], .main-content textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid var(--border-color);
            border-radius: 4px;
            font-size: 16px;
            transition: border-color 0.3s ease;
        }
        .main-content input[type="text"]:focus, .main-content textarea:focus {
            outline: none;
            border-color: var(--primary-color);
        }
        .main-content input[type="submit"] {
            background-color: var(--primary-color);
            color: white;
            padding: 12px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            font-weight: 600;
            transition: background-color 0.3s ease;
        }
        .main-content input[type="submit"]:hover {
            background-color: #6d0808;
        }
        .main-content .prediction-result {
            background-color: #fde8e8;
            border-left: 5px solid var(--primary-color);
            padding: 20px;
            margin-top: 30px;
            border-radius: 4px;
        }
        header {
            z-index: 1000;
            position: relative;
        }
        .main-content .back-link {
            display: inline-block;
            margin-top: 20px;
            color: var(--primary-color);
            text-decoration: none;
            font-weight: 600;
            transition: color 0.3s ease;
        }
        .main-content .back-link:hover {
            color: #6d0808;
        }
        .main-content .error {
            color: #d8000c;
            background-color: #ffd2d2;
            padding: 15px;
            margin-top: 20px;
            border-radius: 4px;
            font-weight: 600;
        }
    </style>
</head>
<body>
<div class="header">
    <jsp:include page="Header.jsp" />
</div>

<div class="main-content">
    <div class="container">
        <h1>Item Category Predictor</h1>
        <div class="card">
            <form action="predict" method="post">
                <div>
                    <label for="itemName">Item Name:</label>
                    <input type="text" id="itemName" name="itemName" required>
                </div>
                <div>
                    <label for="itemFeatures">Item Features:</label>
                    <textarea id="itemFeatures" name="itemFeatures" rows="4" required></textarea>
                </div>
                <input type="submit" value="Predict">
            </form>
        </div>

        <c:if test="${not empty prediction}">
            <div class="prediction-result">
                <h2>Prediction Result</h2>
                <p>Predicted Category: <strong>${prediction}</strong></p>
            </div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <a href="index.jsp" class="back-link">&#8592; Back to Home</a>
    </div>
</div>
<jsp:include page="Footer.jsp" />
</body>
</html>