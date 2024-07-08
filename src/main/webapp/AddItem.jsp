<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Item - SwapEx</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f4f7f6;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
            margin: 0;
        }
        .content-wrapper {
            display: flex;
            flex: 1;
            padding-top: 56px; /* Adjust this value to match your header height */
        }
        .sidebar {
            width: 250px;
            background-color: #8f0b0b;
            color: #fff;
            padding-top: 20px;
            overflow-y: auto;
        }
        .sidebar .nav-link {
            color: #fff;
            padding: 12px 20px;
            transition: background-color 0.3s;
            font-size: 16px; /* Increased font size */
            display: flex;
            align-items: center;
        }
        .sidebar .nav-link:hover, .sidebar .nav-link.active {
            background-color: #b07b83;
        }
        .sidebar .nav-link i {
            font-size: 20px; /* Increased icon size */
            margin-right: 10px;
            width: 30px; /* Fixed width for alignment */
            text-align: center;
        }
        .main-content {
            flex: 1;
            padding: 20px;
            overflow-y: auto;
        }
        .card {
            border: none;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s;
        }
        .card:hover {
            transform: translateY(-5px);
        }
        footer {
            background-color: #8f0b0b;
            color: white;
            text-align: center;
            padding: 10px 0;
            position: relative;
            bottom: 0;
            width: 100%;
        }
    </style>
</head>
<body>
<jsp:include page="Header.jsp" />

<div class="content-wrapper">
    <div class="sidebar">
        <ul class="nav flex-column">
            <li class="nav-item">
                <a class="nav-link" href="Dashboard.jsp">
                    <i class="fas fa-tachometer-alt"></i>
                    <span>Dashboard</span>
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="MyItems.jsp">
                    <i class="fas fa-box-open mr-2"></i> My Items
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link active" href="AddItem.jsp">
                    <i class="fas fa-plus-circle mr-2"></i> Add Item
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="ExchangedItems.jsp">
                    <i class="fas fa-exchange-alt mr-2"></i> Exchanged Items
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="Messages.jsp">
                    <i class="fas fa-envelope mr-2"></i> Messages
                </a>
            </li>
        </ul>
    </div>

    <div class="main-content">
        <div id="add-item">
            <h2 class="mb-4">Add New Item</h2>

            <form action="AddItemServlet" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="title">Item Name</label>
                    <textarea class="form-control" id="title" name="title" required></textarea>
                </div>
                <div class="form-group">
                    <label for="categoryId">Category</label>
                    <select class="form-control" id="categoryId" name="categoryId" required>
                        <option value="">Select a category</option>
                        <c:forEach var="category" items="${categories}">
                            <option value="${category.id}">${category.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea class="form-control" id="description" name="description" required></textarea>
                </div>
                <div class="form-group">
                    <label for="condition">Condition</label>
                    <select class="form-control" id="condition" name="condition" required>
                        <option value="">Select condition</option>
                        <option value="New">New</option>
                        <option value="Like-New">Like New</option>
                        <option value="Good">Good</option>
                        <option value="Refurbished">Refurbished</option>
                        <option value="fair">Fair</option>
                        <option value="poor">Poor</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="features">Features</label>
                    <textarea class="form-control" id="features" name="features" required></textarea>
                </div>
                <div class="form-group">
                    <label for="photos">Photos</label>
                    <input type="file" class="form-control-file" id="photos" name="photos" multiple accept="image/*">
                </div>
                <button type="submit" class="btn btn-primary">Add Item</button>
            </form>
        </div>
    </div>
</div>

<jsp:include page="Footer.jsp" />

<!-- Scripts -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

</body>
</html>
