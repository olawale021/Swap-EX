<!DOCTYPE html>
<html>
<head>
    <title>Item Category Prediction</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            color: #333;
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
            background-color: #f9f9f9;
        }
        h2, h3 {
            color: #8f0b0b;
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
            color: #8f0b0b;
        }
        select {
            width: 100%;
            padding: 8px;
            margin-bottom: 15px;
            border: 1px solid #b07b83;
            border-radius: 4px;
            background-color: #fff;
        }
        select:focus {
            outline: none;
            border-color: #8f0b0b;
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
            background-color: #b07b83;
        }
        .prediction-result {
            margin-top: 20px;
            padding: 15px;
            background-color: #f0e6e7;
            border-left: 5px solid #8f0b0b;
            border-radius: 4px;
        }
    </style>
</head>
<body>
<h2>Item Category Prediction</h2>
<form action="PredictServlet" method="post">
    <label for="brand">Brand:</label>
    <select name="brand" id="brand">
        <option value="None" <%= request.getParameter("brand") == null || "None".equals(request.getParameter("brand")) ? "selected" : "" %>>None</option>
        <option value="IKEA" <%= "IKEA".equals(request.getParameter("brand")) ? "selected" : "" %>>IKEA</option>
        <option value="HP" <%= "HP".equals(request.getParameter("brand")) ? "selected" : "" %>>HP</option>
        <option value="LG" <%= "LG".equals(request.getParameter("brand")) ? "selected" : "" %>>LG</option>
        <option value="River_Island" <%= "River_Island".equals(request.getParameter("brand")) ? "selected" : "" %>>River Island</option>
        <option value="DELL" <%= "DELL".equals(request.getParameter("brand")) ? "selected" : "" %>>DELL</option>
        <option value="Sony" <%= "Sony".equals(request.getParameter("brand")) ? "selected" : "" %>>Sony</option>
        <option value="Apple" <%= "Apple".equals(request.getParameter("brand")) ? "selected" : "" %>>Apple</option>
        <option value="ASUS" <%= "ASUS".equals(request.getParameter("brand")) ? "selected" : "" %>>ASUS</option>
        <option value="Samsung" <%= "Samsung".equals(request.getParameter("brand")) ? "selected" : "" %>>Samsung</option>
        <option value="Crayola" <%= "Crayola".equals(request.getParameter("brand")) ? "selected" : "" %>>Crayola</option>
        <option value="Nike" <%= "Nike".equals(request.getParameter("brand")) ? "selected" : "" %>>Nike</option>
        <option value="Whirlpool" <%= "Whirlpool".equals(request.getParameter("brand")) ? "selected" : "" %>>Whirlpool</option>
        <option value="Adidas" <%= "Adidas".equals(request.getParameter("brand")) ? "selected" : "" %>>Adidas</option>
    </select><br>

    <label for="model">Model:</label>
    <select name="model" id="model">
        <option value="None" <%= request.getParameter("model") == null || "None".equals(request.getParameter("model")) ? "selected" : "" %>>None</option>
        <option value="Galaxy_S21" <%= "Galaxy_S21".equals(request.getParameter("model")) ? "selected" : "" %>>Galaxy S21</option>
        <option value="G5_15" <%= "G5_15".equals(request.getParameter("model")) ? "selected" : "" %>>G5 15</option>
        <option value="iPhone_13_Pro_Max" <%= "iPhone_13_Pro_Max".equals(request.getParameter("model")) ? "selected" : "" %>>iPhone 13 Pro Max</option>
        <option value="WH-1000XM4" <%= "WH-1000XM4".equals(request.getParameter("model")) ? "selected" : "" %>>WH-1000XM4</option>
        <option value="MacBook_Air" <%= "MacBook_Air".equals(request.getParameter("model")) ? "selected" : "" %>>MacBook Air</option>
        <option value="Spectre_x360" <%= "Spectre_x360".equals(request.getParameter("model")) ? "selected" : "" %>>Spectre x360</option>
        <option value="ROG_Strix" <%= "ROG_Strix".equals(request.getParameter("model")) ? "selected" : "" %>>ROG Strix</option>
        <option value="Ultra_One" <%= "Ultra_One".equals(request.getParameter("model")) ? "selected" : "" %>>Ultra One</option>
    </select><br>

    <label for="type">Type:</label>
    <select name="type" id="type">
        <option value="None" <%= request.getParameter("type") == null || "None".equals(request.getParameter("type")) ? "selected" : "" %>>None</option>
        <option value="Wardrobe" <%= "Wardrobe".equals(request.getParameter("type")) ? "selected" : "" %>>Wardrobe</option>
        <option value="Monitor" <%= "Monitor".equals(request.getParameter("type")) ? "selected" : "" %>>Monitor</option>
        <option value="Laptop" <%= "Laptop".equals(request.getParameter("type")) ? "selected" : "" %>>Laptop</option>
        <option value="Table" <%= "Table".equals(request.getParameter("type")) ? "selected" : "" %>>Table</option>
        <option value="Jumper_Dress" <%= "Jumper_Dress".equals(request.getParameter("type")) ? "selected" : "" %>>Jumper Dress</option>
        <option value="Bookshelf" <%= "Bookshelf".equals(request.getParameter("type")) ? "selected" : "" %>>Bookshelf</option>
        <option value="Top" <%= "Top".equals(request.getParameter("type")) ? "selected" : "" %>>Top</option>
        <option value="Washer" <%= "Washer".equals(request.getParameter("type")) ? "selected" : "" %>>Washer</option>
        <option value="Dryer" <%= "Dryer".equals(request.getParameter("type")) ? "selected" : "" %>>Dryer</option>
        <option value="Phone" <%= "Phone".equals(request.getParameter("type")) ? "selected" : "" %>>Phone</option>
        <option value="Camera" <%= "Camera".equals(request.getParameter("type")) ? "selected" : "" %>>Camera</option>
        <option value="Vacuum_Cleaner" <%= "Vacuum_Cleaner".equals(request.getParameter("type")) ? "selected" : "" %>>Vacuum Cleaner</option>
    </select><br>

    <label for="colour">Colour:</label>
    <select name="colour" id="colour">
        <option value="None" <%= request.getParameter("colour") == null || "None".equals(request.getParameter("colour")) ? "selected" : "" %>>None</option>
        <option value="Black" <%= "Black".equals(request.getParameter("colour")) ? "selected" : "" %>>Black</option>
        <option value="Grey" <%= "Grey".equals(request.getParameter("colour")) ? "selected" : "" %>>Grey</option>
        <option value="White" <%= "White".equals(request.getParameter("colour")) ? "selected" : "" %>>White</option>
        <option value="Blue" <%= "Blue".equals(request.getParameter("colour")) ? "selected" : "" %>>Blue</option>
        <option value="Red" <%= "Red".equals(request.getParameter("colour")) ? "selected" : "" %>>Red</option>
        <option value="Brown" <%= "Brown".equals(request.getParameter("colour")) ? "selected" : "" %>>Brown</option>
        <option value="Pink" <%= "Pink".equals(request.getParameter("colour")) ? "selected" : "" %>>Pink</option>
        <option value="Yellow" <%= "Yellow".equals(request.getParameter("colour")) ? "selected" : "" %>>Yellow</option>
        <option value="Silver" <%= "Silver".equals(request.getParameter("colour")) ? "selected" : "" %>>Silver</option>
    </select><br>

    <label for="ram">RAM:</label>
    <select name="ram" id="ram">
        <option value="None" <%= request.getParameter("ram") == null || "None".equals(request.getParameter("ram")) ? "selected" : "" %>>None</option>
        <option value="8GB" <%= "8GB".equals(request.getParameter("ram")) ? "selected" : "" %>>8GB</option>
        <option value="16GB" <%= "16GB".equals(request.getParameter("ram")) ? "selected" : "" %>>16GB</option>
        <option value="32GB" <%= "32GB".equals(request.getParameter("ram")) ? "selected" : "" %>>32GB</option>
    </select><br>

    <label for="size">Size:</label>
    <select name="size" id="size">
        <option value="None" <%= request.getParameter("size") == null || "None".equals(request.getParameter("size")) ? "selected" : "" %>>None</option>
        <option value="S" <%= "S".equals(request.getParameter("size")) ? "selected" : "" %>>S</option>
        <option value="M" <%= "M".equals(request.getParameter("size")) ? "selected" : "" %>>M</option>
        <option value="L" <%= "L".equals(request.getParameter("size")) ? "selected" : "" %>>L</option>
        <option value="XL" <%= "XL".equals(request.getParameter("size")) ? "selected" : "" %>>XL</option>
    </select><br>

    <input type="submit" value="Predict">
</form>

<h3>Prediction Result: <%= request.getAttribute("prediction") %></h3>

</body>
</html>
