<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>All Items - MangoEx</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f8f9fa;
            padding-top: 70px;
        }
        .card {
            transition: transform 0.3s;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        .card:hover {
            transform: translateY(-5px);
        }
        .card-img-wrapper {
            height: 200px; /* Set a fixed height */
            overflow: hidden; /* Hide overflow */
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .card-img-top {
            width: 100%;
            height: 100%;
            object-fit: cover; /* Cover the area without stretching */
        }
        .filter-section {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }
        .filter-section h4 {
            margin-bottom: 15px;
            color: #333;
            font-size: 1.2rem;
        }
        .btn-filter {
            background-color: #8f0b0b !important;
            color: white !important;
            padding: 8px 15px;
            border: none;
            border-radius: 5px;
            transition: background-color 0.3s;
            font-size: 0.9rem;
        }
        .btn-filter:hover {
            background-color: #6d0808 !important;
        }
        .form-group label {
            font-weight: 500;
            color: #555;
            font-size: 0.9rem;
        }
        .form-control {
            border-radius: 5px;
            font-size: 0.9rem;
        }
        .card-title {
            font-size: 1rem;
            margin-bottom: 0.5rem;
        }
        .card-text {
            font-size: 0.85rem;
            margin-bottom: 0.3rem;
        }
        .card-text i {
            margin-right: 5px;
            color: #8f0b0b;
        }
        .btn-view-details {
            background-color: #8f0b0b !important;
            color: white !important;
            border: none;
            padding: 5px 10px;
            font-size: 0.85rem;
            border-radius: 3px;
            transition: background-color 0.3s;
        }
        .btn-view-details:hover {
            background-color: #6d0808 !important;
            color: white !important;
        }
    </style>
</head>
<body>
<jsp:include page="Header.jsp" />

<div class="container">
    <div class="filter-section">
        <h4><i class="fas fa-filter"></i> Filter Items</h4>
        <form id="itemFilterForm">
            <div class="row">
                <div class="col-md-5 form-group">
                    <label for="categoryFilter">Category</label>
                    <select class="form-control" id="categoryFilter" name="category">
                        <option value="">All Categories</option>
                        <c:forEach var="category" items="${categories}">
                            <option value="${category.id}">${category.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-5 form-group">
                    <label for="conditionFilter">Condition</label>
                    <select class="form-control" id="conditionFilter" name="condition">
                        <option value="">All Conditions</option>
                        <option value="new">New</option>
                        <option value="like-new">Like New</option>
                        <option value="good">Good</option>
                        <option value="fair">Fair</option>
                        <option value="poor">Poor</option>
                    </select>
                </div>
                <div class="col-md-2 form-group d-flex align-items-end">
                    <button type="submit" class="btn btn-filter btn-block">Apply Filters</button>
                </div>
            </div>
        </form>
    </div>

    <div class="row" id="itemsContainer">
        <c:forEach var="item" items="${items}">
            <div class="col-md-3 col-sm-6 mb-4 item-card" data-category="${item.categoryId}" data-condition="${item.condition}">
                <div class="card h-100">
                    <div class="card-img-wrapper">
                        <img src="${not empty item.photos ? item.photos[0] : 'path/to/placeholder-image.jpg'}" class="card-img-top" alt="">
                    </div>
                    <div class="card-body">
                        <h5 class="card-title">${item.description}</h5>
                        <p class="card-text">
                            <i class="fas fa-star"></i>
                            <strong>Condition:</strong> ${item.condition}
                        </p>
                        <p class="card-text">
                            <i class="fas fa-tags"></i>
                            <strong>Category:</strong> ${item.categoryName}
                        </p>
                    </div>
                    <div class="card-footer bg-white text-center">
                        <a href="ItemDetailsServlet?id=${item.id}" class="btn btn-view-details">View Details</a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<jsp:include page="Footer.jsp" />

<!-- Bootstrap JS and dependencies -->
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script>
    $(document).ready(function() {
        $('#itemFilterForm').on('submit', function(e) {
            e.preventDefault();
            var selectedCategory = $('#categoryFilter').val();
            var selectedCondition = $('#conditionFilter').val();

            $('.item-card').each(function() {
                var categoryMatch = selectedCategory === "" || $(this).data('category') == selectedCategory;
                var conditionMatch = selectedCondition === "" || $(this).data('condition').toLowerCase() === selectedCondition;

                if (categoryMatch && conditionMatch) {
                    $(this).show();
                } else {
                    $(this).hide();
                }
            });
        });
    });
</script>
</body>
</html>
