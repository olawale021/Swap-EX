<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Item Details - SwapEx</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f8f9fa;
            color: #333;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }
        .main-content {
            flex: 1 0 auto;
            padding-top: 80px;
            padding-bottom: 40px;
        }
        .container {
            max-width: 800px;
        }
        .item-card {
            background-color: #fff;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            overflow: hidden;
            margin-bottom: 30px;
        }
        .item-image-container {
            position: relative;
            height: 300px; /* Reduced height */
            overflow: hidden;
        }
        .item-image {
            width: 100%;
            height: 100%;
            object-fit: contain;
            transition: transform 0.3s ease;
        }
        .item-image:hover {
            transform: scale(1.05);
        }
        .item-details {
            padding: 20px;
        }
        .item-title {
            font-size: 1.5rem;
            font-weight: 600;
            color: #8f0b0b;
            margin-bottom: 15px;
        }
        .item-info {
            font-size: 0.9rem;
            margin-bottom: 10px;
            display: flex;
            align-items: center;
        }
        .item-info i {
            color: #8f0b0b;
            margin-right: 10px;
            font-size: 1rem;
        }
        .item-description {
            font-size: 0.9rem;
            line-height: 1.6;
            margin-bottom: 20px;
            color: #555;
        }
        .action-buttons {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .btn-back, .btn-message {
            padding: 8px 16px;
            border-radius: 50px;
            font-weight: 500;
            transition: all 0.3s ease;
            text-decoration: none;
            font-size: 0.9rem;
        }
        .btn-back {
            background-color: #6c757d;
            color: white;
        }
        .btn-back:hover {
            background-color: #5a6268;
            color: white;
        }
        .btn-message {
            background-color: #8f0b0b;
            color: white;
        }
        .btn-message:hover {
            background-color: #8f0b0b;
            color: white;
            transform: translateY(-2px);
            box-shadow: 0 4px 10px rgba(40, 167, 69, 0.3);
        }
        .additional-photos {
            margin-top: 20px;
        }
        .additional-photos h3 {
            font-size: 1.2rem;
            margin-bottom: 15px;
            color: #8f0b0b;
        }
        .photo-gallery {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
            gap: 10px;
        }
        .photo-item {
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
            transition: transform 0.3s ease;
        }
        .photo-item:hover {
            transform: scale(1.05);
        }
        .photo-item img {
            width: 100%;
            height: 100px;
            object-fit: cover;
        }
        .exchange-form {
            margin-top: 20px;
            background-color: #f1f3f5;
            padding: 20px;
            border-radius: 10px;
        }
        .exchange-form textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ced4da;
            border-radius: 5px;
            resize: vertical;
            min-height: 80px;
        }
        .exchange-form button {
            background-color: #8f0b0b;
            color: white;
            border: none;
            padding: 8px 16px;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s ease;
            margin-top: 10px;
        }
        .exchange-form button:hover {
            background-color: #6d0808;
        }
    </style>
</head>
<body>
<jsp:include page="Header.jsp" />

<div class="main-content">
    <div class="container">
        <c:choose>
            <c:when test="${not empty item}">
                <div class="item-card">
                    <div class="item-image-container">
                        <img src="${not empty item.photos ? item.photos[0] : 'path/to/placeholder-image.jpg'}" class="item-image" alt="${item.description}">
                    </div>
                    <div class="item-details">
                        <h1 class="item-title">${item.description}</h1>
                        <p class="item-info"><i class="fas fa-star"></i> <strong>Condition:</strong> ${item.condition}</p>
                        <p class="item-info"><i class="fas fa-tags"></i> <strong>Category:</strong> ${item.categoryName}</p>
                        <p class="item-description">
                            <i class="fas fa-info-circle"></i>
                            <strong>Description:</strong> ${item.title}
                        </p>
                        <div class="action-buttons">
                            <a href="AllItemsServlet" class="btn-back">
                                <i class="fas fa-arrow-left mr-2"></i>Back
                            </a>
                            <a href="#exchange-form" class="btn-message">
                                <i class="fas fa-exchange-alt mr-2"></i>Propose Exchange
                            </a>
                        </div>
                    </div>
                </div>

                <c:if test="${item.photos.size() > 1}">
                    <div class="additional-photos">
                        <h3>Additional Photos</h3>
                        <div class="photo-gallery">
                            <c:forEach var="photo" items="${item.photos}" begin="1">
                                <div class="photo-item">
                                    <img src="${photo}" alt="Additional Item Photo">
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>

                <div id="exchange-form" class="exchange-form">
                    <h3>Propose an Exchange</h3>
                    <form action="ExchangeServlet" method="post">
                        <input type="hidden" name="itemId" value="${item.id}">
                        <input type="hidden" name="interestedUserId" value="${sessionScope.userId}">
                        <input type="hidden" name="ownerId" value="${item.userId}">
                        <input type="hidden" name="ownerUsername" value="${owner.username}">
                        <input type="hidden" name="interestedUserUsername" value="${sessionScope.username}">
                        <input type="hidden" name="itemTitle" value="${item.description}">
                        <textarea name="content" placeholder="Describe your exchange proposal or ask a question about this item" required></textarea>
                        <button type="submit">Send Proposal</button>
                    </form>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-danger" role="alert">
                    <i class="fas fa-exclamation-triangle mr-2"></i> Item not found.
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="Footer.jsp" />

<!-- Bootstrap JS and dependencies -->
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>