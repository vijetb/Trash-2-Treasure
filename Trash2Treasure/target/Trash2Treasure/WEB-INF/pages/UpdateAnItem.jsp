<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${userIdentification != 'verified'}">
	<c:redirect url="/Trash2Treasure/index.jsp" />
</c:if>

<html>
<head>
<script type="text/javascript">
	function validateItemUpdate() {
		var label = document.getElementById(label).value;

		if (label.length == 0) {
			alert("Item lable cannot be blank..!!");
			return false;
		}
		return true;

	}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<form name="updateItemForm" action="/Trash2Treasure/UpdateEditedItem"
		onsubmit="return validateItemUpdate()" method="post">
		<table>
			<tr>
				<td>Item ID</td>
				<td><input type="text" name="id" id="id"
					value="${item.id}" readonly="readonly"></td>

			</tr>
			<tr>
				<td>Label</td>
				<td><input type="text" name="label" id="label"
					value="${item.label}"></td>

			</tr>
			<tr>
				<td>Category</td>
				<td><select name="category" id="category" size="1">
						<option value="${item.category}">${item.category}</option>
						<option value="FURNITURE">FURNITURE</option>
						<option value="APPARELS">APPARELS</option>
						<option value="ELECTRONICS">ELECTRONICS</option>
						<option value="FOOTWEAR">FOOTWEAR</option>
						<option value="KITCHEN_AND_DINING">KITCHEN_AND_DINING</option>
						<option value="STATIONARY">Stationary</option>
						<option value="OTHERS">Others</option>

				</select></td>
			</tr>
			<tr>
				<td>Price</td>
				<td><select name="price" id="price" size="1">
						<option value="${item.price}">${item.price}</option>
						<option value="USD_1">USD 1</option>
						<option value="USD_5">USD 5</option>
						<option value="USD_10">USD 10</option>
						<option value="USD_15">USD 15</option>
						<option value="FREE">Free</option>

				</select></td>

			</tr>
			<tr>
				<td>Image</td>
				<td><img src="${item.image}" height="10" width="10"></td>

			</tr>
			<tr>
				<td>Description</td>
				<td><textarea name="description" id="description"
						>${item.description}</textarea></td>

			</tr>
			<tr>
				<td><input type="hidden" value="${userInfo}" name="userInfo"></td>
			</tr>
			<tr>
				<td><input type="submit" value="Update Item"></td>
			</tr>

		</table>
	</form>
</body>
</html>