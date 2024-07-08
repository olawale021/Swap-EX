<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Items - MangoEx</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <style>
        /* CSS styles */
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f8f9fa;
            padding-top: 56px;
        }
        .wrapper {
            display: flex;
        }
        .sidebar {
            width: 250px;
            background-color: #8f0b0b;
            color: #fff;
            padding-top: 20px;
            min-height: calc(100vh - 56px);
            position: fixed;
            left: 0;
            top: 56px;
            z-index: 1000;
        }
        .sidebar .nav-link {
            color: #fff;
            padding: 12px 20px;
            transition: background-color 0.3s, transform 0.2s;
        }
        .sidebar .nav-link:hover, .sidebar .nav-link.active {
            background-color: #b07b83;
            transform: translateX(5px);
        }
        .sidebar .nav-link i {
            width: 25px;
            margin-right: 10px;
        }
        .content {
            flex: 1;
            padding: 20px;
            margin-left: 250px;
        }
        .container {
            max-width: 100%;
        }
        .card {
            transition: transform 0.3s, box-shadow 0.3s;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            border: none;
            border-radius: 12px;
            overflow: hidden;
        }
        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 15px rgba(0,0,0,0.2);
        }
        .card-img-wrapper {
            height: 200px;
            overflow: hidden;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .card-img-top {
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: transform 0.3s;
        }
        .card:hover .card-img-top {
            transform: scale(1.1);
        }
        .card-body {
            padding: 20px;
        }
        .card-title {
            font-size: 1.1rem;
            font-weight: 600;
            margin-bottom: 10px;
            color: #333;
        }
        .card-text {
            font-size: 0.9rem;
            color: #666;
            margin-bottom: 5px;
        }
        .card-footer {
            background-color: #fff;
            border-top: 1px solid #eee;
            padding: 15px;
        }
        .btn-sm {
            font-size: 0.85rem;
            padding: 0.375rem 0.75rem;
            border-radius: 50px;
        }
        .btn-primary, .btn-outline-secondary, .btn-outline-danger {
            transition: all 0.3s;
        }
        .btn-primary {
            background-color: #8f0b0b;
            border-color: #8f0b0b;
        }
        .btn-primary:hover {
            background-color: #6d0808;
            border-color: #6d0808;
            transform: translateY(-2px);
        }
        .btn-outline-secondary:hover, .btn-outline-danger:hover {
            transform: translateY(-2px);
        }
        .modal-content {
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
        }
        .modal-header {
            background-color: #8f0b0b;
            color: white;
            border-top-left-radius: 15px;
            border-top-right-radius: 15px;
        }
        .modal-body {
            padding: 30px;
        }
        .image-upload-container {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            margin-top: 15px;
        }
        .image-upload-item {
            position: relative;
            width: 100px;
            height: 100px;
            border: 2px dashed #ccc;
            border-radius: 8px;
            overflow: hidden;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
        }
        .image-upload-item img {
            max-width: 100%;
            max-height: 100%;
            object-fit: cover;
        }
        .image-upload-item .delete-image {
            position: absolute;
            top: 5px;
            right: 5px;
            background-color: rgba(255,255,255,0.7);
            border-radius: 50%;
            padding: 5px;
            cursor: pointer;
            z-index: 1;
        }
        #imageUpload {
            display: none;
        }
    </style>
</head>
<body>
<jsp:include page="Header.jsp" />

<div class="wrapper">
    <div class="sidebar">
        <nav class="nav flex-column">
            <a class="nav-link" href="UserDashboard.jsp"><i class="fas fa-home"></i> Dashboard</a>
            <a class="nav-link active" href="UserItems.jsp"><i class="fas fa-th-list"></i> My Items</a>
            <a class="nav-link" href="AddItem.jsp"><i class="fas fa-plus"></i> Add Item</a>
            <a class="nav-link" href="LogoutServlet"><i class="fas fa-sign-out-alt"></i> Logout</a>
        </nav>
    </div>

    <div class="content">
        <div class="container">
            <h1 class="mb-4">My Items</h1>

            <div class="row">
                <c:forEach var="item" items="${userItems}">
                    <div class="col-md-4 col-lg-3 mb-4">
                        <div class="card h-100">
                            <div class="card-img-wrapper">
                                <img src="${not empty item.photos ? item.photos[0] : 'path/to/placeholder-image.jpg'}" class="card-img-top" alt="${item.description}">
                            </div>
                            <div class="card-body">
                                <h5 class="card-title">${item.description}</h5>
                                <p class="card-text"><small><strong>Condition:</strong> ${item.condition}</small></p>
                                <p class="card-text"><small><strong>Category:</strong> ${item.categoryName}</small></p>
                            </div>
                            <div class="card-footer text-center">
                                <a href="ItemDetailsServlet?id=${item.id}" class="btn btn-primary btn-sm mr-1">View</a>
                                <button onclick="openEditModal(this)"
                                        class="btn btn-outline-secondary btn-sm mr-1"
                                        data-id="${item.id}"
                                        data-description="${item.description}"
                                        data-condition="${item.condition}"
                                        data-category="${item.categoryId}"
                                        data-features="${item.features}"
                                        data-photos='<c:out value="${item.photosJson}"/>'>
                                    Edit
                                </button>
                                <form action="DeleteItemServlet?id=${item.id}" method="post" onsubmit="return confirm('Are you sure you want to delete this item?');" style="display: inline;">
                                    <input type="hidden" name="itemId" value="${item.id}">
                                    <button type="submit" class="btn btn-outline-danger btn-sm">Delete</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

<!-- Edit Item Modal -->
<div class="modal fade" id="editItemModal" tabindex="-1" role="dialog" aria-labelledby="editItemModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editItemModalLabel">Edit Item</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form id="editItemForm" action="UpdateItemServlet" method="post" enctype="multipart/form-data">
                    <input type="hidden" id="editItemId" name="itemId">
                    <div class="form-group">
                        <label for="editDescription">Description</label>
                        <input type="text" class="form-control" id="editDescription" name="description" required>
                    </div>
                    <div class="form-group">
                        <label for="editCondition">Condition</label>
                        <select class="form-control" id="editCondition" name="condition" required>
                            <option value="New">New</option>
                            <option value="Like New">Like New</option>
                            <option value="Good">Good</option>
                            <option value="Fair">Fair</option>
                            <option value="Poor">Poor</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="editCategory">Category</label>
                        <select class="form-control" id="editCategory" name="category" required>
                            <c:forEach var="category" items="${categories}">
                                <option value="${category.id}">${category.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="editFeatures">Features</label>
                        <textarea class="form-control" id="editFeatures" name="features" rows="3" required></textarea>
                    </div>
                    <div class="form-group">
                        <label>Images</label>
                        <div class="image-upload-container" id="imageContainer">
                            <!-- Existing images will be added here dynamically -->
                            <div class="image-upload-item" onclick="document.getElementById('imageUpload').click();">
                                <i class="fas fa-plus"></i>
                            </div>
                        </div>
                        <input type="file" id="imageUpload" name="photos" accept="image/*" multiple onchange="handleImageUpload(this)">
                    </div>
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="Footer.jsp" />

<!-- Bootstrap JS and dependencies -->
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script>
    function openEditModal(button) {
        const itemId = $(button).data('id');
        const description = $(button).data('description');
        const condition = $(button).data('condition');
        const categoryId = $(button).data('category');
        const features = $(button).data('features');
        const photosString = $(button).attr('data-photos');

        let photos = [];
        try {
            photos = JSON.parse(photosString);
            console.log("Parsed photos:", photos); // Log the photos array
        } catch (e) {
            console.error("Failed to parse photos JSON:", e);
        }

        $('#editItemId').val(itemId);
        $('#editDescription').val(description);
        $('#editCondition').val(condition);
        $('#editCategory').val(categoryId);
        $('#editFeatures').val(features);

        // Clear previous images
        $('#imageContainer').empty();

        // Add existing images with hidden inputs
        if (photos && photos.length > 0) {
            photos.forEach(photo => {
                console.log("Photo URL:", photo); // Log the photo URL directly

                let imageItemHTML = `
                    <div class="image-upload-item">
                        <img src="${photo}" alt="Item Image" style="width: 100%; height: 100%; object-fit: cover;">
                        <span class="delete-image">&times;</span>
                        <input type="hidden" name="existingPhotos" value="${photo}">
                    </div>
                `;

                console.log("Generated Image Item HTML:", imageItemHTML); // Log the HTML being generated

                let imageItem = $(imageItemHTML);
                $('#imageContainer').append(imageItem);
            });
        }

        // Add the "add image" button back
        $('#imageContainer').append('<div class="image-upload-item" onclick="document.getElementById(\'imageUpload\').click();"><i class="fas fa-plus"></i></div>');

        $('#editItemModal').modal('show');
    }

    function handleImageUpload(input) {
        if (input.files && input.files.length > 0) {
            for (let i = 0; i < input.files.length; i++) {
                let reader = new FileReader();
                reader.onload = function(e) {
                    let imageItem = $('<div class="image-upload-item"><img src="' + e.target.result + '" alt="Uploaded Image" style="width: 100%; height: 100%; object-fit: cover;"><span class="delete-image">&times;</span></div>');
                    imageItem.insertBefore($('#imageContainer .image-upload-item:last'));
                }
                reader.readAsDataURL(input.files[i]);
            }
        }
    }

    $(document).on('click', '.delete-image', function() {
        $(this).parent().remove();
    });

    $('#editItemModal').on('hide.bs.modal', function (e) {
        if ($('#editItemForm').data('submitting')) {
            e.preventDefault();
        }
    });

    $('#editItemForm').on('submit', function(e) {
        $(this).data('submitting', true);
        // Do not prevent default
    });
</script>
</body>
</html>
