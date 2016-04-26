<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${userIdentification != 'verified'}">
   <c:redirect url="/Trash2Treasure/index.jsp"/>
</c:if>

<html>
<head>
<script type="text/javascript">
	function validateItemUpload() {
		var label = document.getElementById(label).value;
		var category = document.getElementById(category).value;
		var price = document.getElementById(price).value;

		if (label.length == 0) {
			alert("Item lable cannot be blank..!!");
			return false;
		} else if (category == "Please select item category") {
			alert("Please select an item category..!!");
			return false;
		} else if (price == "Please select item price") {
			alert("Please select price for this item..!!");
			return false;
		}
		return true;

	}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body bgcolor="yellow">
<marquee behavior="scroll"><font size="14">Trash-2-Treasure</font></marquee>
<br>
<br>
<br>
<br>

<p align="right"><a href="/Trash2Treasure/Logout/${userInfo}">Logout</a></p>
<a href="/Trash2Treasure/Home/${userInfo}">Home</a>
<br>
<br>
	<form name="sellerUploadItemForm" enctype="multipart/form-data"
		action="/Trash2Treasure/SellerUploadItem"
		onsubmit="return validateItemUpload()" method="post">

		<table align="center">
		<tr>
				<td><input type="hidden" value= "${userInfo}" name="userInfo"></td>
			</tr>
			<tr>
				<td>Label</td>
				<td><input type="text" name="label" id="label" /></td>
			</tr>
			<tr>
				<td>Category</td>
				<td><select name="category" id="category" size="1">
						<option value="Please select item category">Please select
							item category</option>
						<option value="FURNITURE">FURNITURE</option>
						<option value="APPARELS">APPARELS</option>
						<option value="ELECTRONICS">ELECTRONICS</option>
						<option value="FOOTWEAR">FOOTWEAR</option>
						<option value="KITCHEN_AND_DINING">KITCHEN_AND_DINING</option>
						<option value="STATIONARY">STATIONARY</option>
						<option value="OTHERS">OTHERS</option>

				</select></td>
			</tr>
			<tr>
				<td>Price</td>
				<td><select name="price" id="price" size="1">
						<option value="Please select item price">Please select
							item price</option>
						<option value="USD_1">USD 1</option>
						<option value="USD_5">USD 5</option>
						<option value="USD_10">USD 10</option>
						<option value="USD_15">USD 15</option>
						<option value="FREE">Free</option>

				</select></td>
			</tr>
			<tr>
				<td>Item Image</td>
				<td><input type="file" name=image id="image"></td>
			</tr>
			<tr>
				<td>Item Description</td>
				<td><textarea name="description" id="description"></textarea></td>
			</tr>
			
			<tr>
				<td colspan="2"><input type="submit" value="Upload Item"></td>
			</tr>
			
		</table>
	</form>
</body>
</html>