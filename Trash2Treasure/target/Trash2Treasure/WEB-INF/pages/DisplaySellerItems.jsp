<%@page import="java.util.Base64"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="org.apache.commons.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
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
				${item.image}
				<%
					//String url = "{item.image}";
				%>
				<tr>
					<td>Image</td>
					<td><img src="{item.image}" height="100" width="100"></td>

				</tr>
				<tr>
					<td>Description</td>
					<td><input type="text" name="description" id="description"
						readonly="readonly" value="${item.description}" /></td>

				</tr>

			</c:forEach>

		</c:if>
		<tr>
			<td><input type="hidden" value="${userInfo}" name="userInfo"></td>
		</tr>

	</table>

</body>
</html>