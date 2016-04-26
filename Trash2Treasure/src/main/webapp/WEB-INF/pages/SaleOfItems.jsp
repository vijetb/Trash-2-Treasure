<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <c:if test="${userIdentification != 'verified'}">
   <c:redirect url="/Trash2Treasure/index.jsp"/>
</c:if>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
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
<h2>${msg}</h2>

<table align="center">
		<c:if test="${not empty items}">

				<c:forEach var="item" items="${items}">
					<tr>
						<td>Label</td>
						<td><input type="text" name="label" id="label" value="${item.label}" readonly="readonly"></td>
						
					</tr>
					<tr>
						<td>Category</td>
						<td><input type="text" name="category" id="category" value="${item.category}" readonly="readonly"></td>
						
					</tr>
					<tr>
						<td>Price</td>
						<td><input type="text" name="price" id="price" value="${item.price}" readonly="readonly"></td>
						
					</tr>
					<tr>
						<td>Image</td>
						<td><img src="${item.image}" width="100" height="100"></td>
						
					</tr>
					<tr>
						<td>Description</td>
						<td><textarea name="description" id="description" readonly="readonly"><c:out value="${item.description}"/></textarea></td>
						
					</tr>
					<tr>
						<td><a href="/Trash2Treasure/AdminSaleAnItem/${userInfo}/${item.id}">Mark as sold</a></td>
					</tr>
					<tr>
					<td><input type="hidden" value= "${userInfo}" name="userInfo"></td>
					</tr>
				</c:forEach>

		</c:if>


	</table>


</body>
</html>