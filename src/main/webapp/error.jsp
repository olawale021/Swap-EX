<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - MangoEx</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <div class="alert alert-danger mt-5" role="alert">
        <h4 class="alert-heading">Error!</h4>
        <p>${errorMessage}</p>
        <hr>
        <a href="index.jsp" class="btn btn-primary">Go to Home</a>
    </div>
</div>
</body>
</html>
