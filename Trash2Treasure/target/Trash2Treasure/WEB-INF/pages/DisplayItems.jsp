<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<script type="text/javascript">
	function validateSortItems() {

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
<body>
	Filter By:

	<form name="sortItemsForm" action="/Trash2Treasure/SortedItems"
		onsubmit="return validateSortItems()" method="post">
		<table>
			<tr>
				<td><input type="radio" name="sort" id="categorySort"
					value="categoryRadio"> Category</td>
				<td>
					<div id="categoryDiv" style="display: none">
						<select name="category" id="category" size="1">
							<option value="FURNITURE">FURNITURE</option>
							<option value="APPARELS">APPARELS</option>
							<option value="ELECTRONICS">ELECTRONICS</option>
							<option value="FOOTWEAR">FOOTWEAR</option>
							<option value="KITCHEN_AND_DINING">KITCHEN_AND_DINING</option>
							<option value="STATIONARY">Stationary</option>
							<option value="OTHERS">Others</option>

						</select>
					</div>

				</td>
			</tr>

			<tr>
				<td><input type="radio" name="sort" id="priceSort"
					value="priceRadio"> Price</td>
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

	<table>
		<c:if test="${not empty items}">

			<c:forEach var="item" items="${items}">
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
					<td><img src="/images/1614288_1icon.png" height="500" width="500"> </td>

				</tr>
				<tr>
					<td>Description</td>
					<td><textarea name="description" id="description"
							readonly="readonly"><c:out value="${item.description}" /></textarea></td>

				</tr>
				<tr>
					<td><input type="hidden" value= "${userInfo}" name="userInfo"></td>
				</tr>
			</c:forEach>

		</c:if>


	</table>
</body>
</html>