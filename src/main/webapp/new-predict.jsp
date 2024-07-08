<!DOCTYPE html>
<html>
<head>
    <title>Item Category Prediction</title>
</head>
<body>
<h2>Item Category Prediction</h2>
<form action="PredictServlet" method="post">
    <label for="brand">Brand:</label>
    <select name="brand" id="brand">
        <option value="IKEA">IKEA</option>
        <option value="HP">HP</option>
        <option value="LG">LG</option>
        <option value="River_Island">River Island</option>
        <option value="DELL">DELL</option>
        <option value="Sony">Sony</option>
        <option value="Apple">Apple</option>
        <option value="ASUS">ASUS</option>
        <option value="Samsung">Samsung</option>
        <option value="Crayola">Crayola</option>
        <option value="Nike">Nike</option>
        <option value="Whirlpool">Whirlpool</option>
        <option value="Adidas">Adidas</option>
        <option value="None">None</option>
    </select><br>

    <label for="model">Model:</label>
    <select name="model" id="model">
        <option value="Galaxy_S21">Galaxy S21</option>
        <option value="G5_15">G5 15</option>
        <option value="iPhone_13_Pro_Max">iPhone 13 Pro Max</option>
        <option value="WH-1000XM4">WH-1000XM4</option>
        <option value="MacBook_Air">MacBook Air</option>
        <option value="Spectre_x360">Spectre x360</option>
        <option value="ROG_Strix">ROG Strix</option>
        <option value="Ultra_One">Ultra One</option>
        <option value="None">None</option>
    </select><br>

    <label for="type">Type:</label>
    <select name="type" id="type">
        <option value="Wardrobe">Wardrobe</option>
        <option value="Monitor">Monitor</option>
        <option value="Laptop">Laptop</option>
        <option value="Table">Table</option>
        <option value="Jumper_Dress">Jumper Dress</option>
        <option value="Bookshelf">Bookshelf</option>
        <option value="Top">Top</option>
        <option value="Washer">Washer</option>
        <option value="Dryer">Dryer</option>
        <option value="Phone">Phone</option>
        <option value="Camera">Camera</option>
        <option value="Vacuum_Cleaner">Vacuum Cleaner</option>
        <option value="None">None</option>
    </select><br>

    <label for="colour">Colour:</label>
    <select name="colour" id="colour">
        <option value="Black">Black</option>
        <option value="Grey">Grey</option>
        <option value="White">White</option>
        <option value="Blue">Blue</option>
        <option value="Red">Red</option>
        <option value="Brown">Brown</option>
        <option value="Pink">Pink</option>
        <option value="Yellow">Yellow</option>
        <option value="Silver">Silver</option>
        <option value="None">None</option>
    </select><br>

    <label for="ram">RAM:</label>
    <select name="ram" id="ram">
        <option value="8GB">8GB</option>
        <option value="16GB">16GB</option>
        <option value="32GB">32GB</option>
        <option value="None">None</option>
    </select><br>

    <label for="size">Size:</label>
    <select name="size" id="size">
        <option value="S">S</option>
        <option value="M">M</option>
        <option value="L">L</option>
        <option value="XL">XL</option>
        <option value="None">None</option>
    </select><br>

    <input type="submit" value="Predict">
</form>

<h3>Prediction Result: ${prediction}</h3>
</body>
</html>
