<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Item Category Predictor</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            color: #333;
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
            background-color: #f4f4f4;
        }
        h2 {
            color: #8f0b0b;
            text-align: center;
        }
        form {
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        input[type="text"], textarea {
            width: 100%;
            padding: 8px;
            margin-bottom: 15px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        textarea {
            height: 100px;
            resize: vertical;
        }
        input[type="submit"] {
            background-color: #8f0b0b;
            color: #fff;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
        input[type="submit"]:hover {
            background-color: #6d0808;
        }
        h3 {
            margin-top: 20px;
            text-align: center;
            color: #8f0b0b;
        }
        .error {
            color: #d8000c;
            background-color: #ffd2d2;
            padding: 10px;
            margin-top: 20px;
            border-radius: 4px;
        }
    </style>
</head>
<body>
<h2>Item Category Predictor</h2>
<form action="predict" method="post">
    <label for="itemName">Item Name:</label>
    <input type="text" id="itemName" name="itemName" required>

    <label for="itemFeatures">Item Features:</label>
    <textarea id="itemFeatures" name="itemFeatures" required></textarea>

    <input type="submit" value="Predict">
</form>

<% if (request.getAttribute("prediction") != null) { %>
<h3>Prediction: <%= request.getAttribute("prediction") %></h3>
<% } %>

<% if (request.getAttribute("error") != null) { %>
<div class="error"><%= request.getAttribute("error") %></div>
<% } %>
</body>
</html>