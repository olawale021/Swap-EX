<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Item Details - MangoEx</title>
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
            max-width: 400px; /* Further reduced container width */
        }
        .card {
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            border: none;
            border-radius: 15px;
            overflow: hidden;
        }
        .card-img-wrapper {
            height: 250px; /* Further reduced image height */
            overflow: hidden;
            position: relative;
        }
        .card-img-top {
            width: 100%;
            height: 100%;
            object-fit: contain;
            transition: transform 0.3s ease;
        }
        .card-img-wrapper:hover .card-img-top {
            transform: scale(1.05);
        }
        .item-details {
            background-color: #fff;
            padding: 15px;
            border-radius: 0 0 15px 15px;
        }
        .item-title {
            font-size: 1.3rem;
            font-weight: 600;
            margin-bottom: 10px;
            color: #8f0b0b;
        }
        .item-info {
            font-size: 0.9rem;
            margin-bottom: 8px;
            display: flex;
            align-items: flex-start;
        }
        .item-info i {
            width: 18px;
            color: #8f0b0b;
            margin-right: 5px;
            margin-top: 4px;
        }
        .item-description {
            font-size: 0.85rem;
            line-height: 1.4;
            margin-bottom: 15px;
        }
        .btn-message {
            background-color: #28a745 !important;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 50px;
            transition: all 0.3s ease;
            font-weight: 500;
            font-size: 0.9rem;
            display: inline-block;
            margin-top: 15px;
            text-decoration: none;
        }
        .btn-message:hover {
            background-color: #8f0b0b !important;
            color: white !important;
            transform: translateY(-2px);
            box-shadow: 0 4px 10px rgba(40, 167, 69, 0.3) ;
            text-decoration: none;
        }
        .btn-message i {
            margin-right: 5px;
        }
        .action-buttons {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 20px;
        }
        .additional-photos {
            margin-top: 15px;
        }
        .additional-photos h3 {
            font-size: 1.1rem;
            margin-bottom: 10px;
        }
        .additional-photos img {
            border-radius: 8px;
            margin-bottom: 10px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            transition: transform 0.3s ease;
            max-height: 100px;
            width: 100%;
            object-fit: cover;
        }
        .additional-photos img:hover {
            transform: scale(1.05);
        }
    </style>
</head>
<body>
<jsp:include page="Header.jsp" />

<div class="main-content">
    <div class="container">
        <c:choose>
            <c:when test="${not empty item}">
                <div class="card mb-3">
                    <div class="card-img-wrapper">
                        <img src="${not empty item.photos ? item.photos[0] : 'path/to/placeholder-image.jpg'}" class="card-img-top" alt="${item.description}">
                    </div>
                    <div class="item-details">
                        <h1 class="item-title">${item.description}</h1>
                        <p class="item-info"><i class="fas fa-star"></i> <strong>Condition:</strong> ${item.condition}</p>
                        <p class="item-info"><i class="fas fa-tags"></i> <strong>Category:</strong> ${item.categoryName}</p>
                        <p class="item-info">
                            <i class="fas fa-info-circle"></i>
                            <strong>Description:</strong> ${item.title}
                        </p>
                        <div class="action-buttons">
                            <a href="AllItemsServlet" class="btn btn-back">
                                <i class="fas fa-arrow-left mr-2"></i>Back
                            </a>
                            <form action="ExchangeServlet" method="post">
                                <input type="hidden" name="itemId" value="${item.id}">
                                <input type="hidden" name="interestedUserId" value="${sessionScope.userId}">
                                <input type="hidden" name="ownerId" value="${item.userId}">
                                <input type="hidden" name="ownerUsername" value="${owner.username}">
                                <input type="hidden" name="interestedUserUsername" value="${sessionScope.username}">
                                <input type="hidden" name="itemTitle" value="${item.description}">
                                <textarea name="content" placeholder="Enter your message"></textarea>
                                <button type="submit">Send</button>
                            </form>



                        </div>
                    </div>
                </div>

                <c:if test="${item.photos.size() > 1}">
                    <div class="additional-photos">
                        <h3>Additional Photos</h3>
                        <div class="row">
                            <c:forEach var="photo" items="${item.photos}" begin="1">
                                <div class="col-4">
                                    <img src="${photo}" class="img-fluid" alt="Additional Item Photo">
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
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