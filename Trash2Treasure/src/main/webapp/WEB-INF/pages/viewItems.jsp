<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<script type="text/javascript">
	function showDiv() {

		var categorySort = document.getElementById("categorySort");
		var priceSort = document.getElementById("priceSort");

		var categoryDiv = document.getElementById("categoryDiv");
		var priceDiv = document.getElementById("priceDiv");

		categoryDiv.style.display = categorySort.checked ? "block" : "none";
		priceDiv.style.display = priceSort.checked ? "block" : "none";
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

<c:if test="${empty userInfo}">
<a href="/Trash2Treasure/userRegister">Register</a>
<br>
<a href="/Trash2Treasure/login">Login</a>
</c:if>


<c:if test="${not empty userInfo}">
<p align="right"><a href="/Trash2Treasure/Logout/${userInfo}">Logout</a></p>
<a href="/Trash2Treasure/Home/${userInfo}">Home</a>
<br>
<br>
<br>
</c:if>
<br>
<br>
<p align="center">
	Filter By:
</p>
	<form name="sortItemsForm" action="/Trash2Treasure/SortedItemsBuyer"
	      method="post">
		<table align="center">
			<tr>
				<td><input type="radio" name="sort" id="categorySort"
					value="categoryRadio" onclick="showDiv()"> Category</td>
				<td>
					<div id="categoryDiv" style="display: none">
						<select name="category" id="category" size="1">
							<option value="FURNITURE">FURNITURE</option>
							<option value="APPARELS">APPARELS</option>
							<option value="ELECTRONICS">ELECTRONICS</option>
							<option value="FOOTWEAR">FOOTWEAR</option>
							<option value="KITCHEN_AND_DINING">KITCHEN_AND_DINING</option>
							<option value="STATIONARY">STATIONARY</option>
							<option value="OTHERS">OTHERS</option>

						</select>
					</div>

				</td>
			</tr>

			<tr>
				<td><input type="radio" name="sort" id="priceSort"
					value="priceRadio" onclick="showDiv()"> Price</td>
				<td>
					<div id="priceDiv" style="display: none">
						<select name="price" id="price" size="1">
							<option value="USD_1">USD 1</option>
							<option value="USD_5">USD 5</option>
							<option value="USD_10">USD 10</option>
							<option value="USD_15">USD 15</option>
							<option value="FREE">Free</option>

						</select>


					</div>
				</td>
			</tr>

			<tr>
				<td><input type="radio" name="sort" id="viewAll"
					value="allRadio" checked="checked"> View All</td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="Sort Items"></td>
			</tr>

			<tr>
				<td><input type="hidden" value= "${userInfo}" name="userInfo"></td>
			</tr>
		</table>
	</form>

	<h2>${msg}</h2>
	
	
		<c:if test="${not empty items}">
			
			<c:forEach var="item" items="${items}">
			
			<table border="1" align="center">
				<tr>
					<td>Label</td>
					<td><input type="text" name="label" id="label"
						value="${item.label}" readonly="readonly"></td>

				</tr>
				<tr>
					<td>Category</td>
					<td><input type="text" name="category" id="category"
						value="${item.category}" readonly="readonly"></td>

				</tr>
				<tr>
					<td>Price</td>
					<td><input type="text" name="price" id="price"
						value="${item.price}" readonly="readonly"></td>

				</tr>
				<tr>
					<td>Image</td>
					<td><img src="imgs/${item.image}" height="100" width="100"> </td>

				</tr>
				<tr>
					<td>Description</td>
					<td><textarea name="description" id="description"
							readonly="readonly"><c:out value="${item.description}" /></textarea></td>

				</tr>
				<tr>
					<td colspan="2"><input type="hidden" value= "${userInfo}" name="userInfo"></td>
				</tr>
				</table>
				<br/>
				<br/>
				<br/>
			</c:forEach>

		</c:if>


	
</body>
</html>