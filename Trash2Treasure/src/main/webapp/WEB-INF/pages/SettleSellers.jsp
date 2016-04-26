<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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

	<p align="right">
		<a href="/Trash2Treasure/Logout/${userInfo}">Logout</a>
	</p>
	<a href="/Trash2Treasure/Home/${userInfo}">Home</a>
	<br>
	<br>
	<h2>${msg}</h2>
	<h2>${settleSuccess}</h2>
<p align="center">
	Settlement Pending for the following Sellers:
	</p>
	<table border="1" align="center">
		<tr>
			<td>NUID</td>
			<td>First Name</td>
			<td>Amount Owed</td>
			<td>Action</td>
		</tr>
		<c:if test="${not empty users}">

			<c:forEach var="user" items="${users}">
				<tr>

					<td>${user.getNuID()}</td>
					<td>${user.getFirstName()}</td>
					<td>$&nbsp;${user.amountOwed}</td>
					<td><a href="/Trash2Treasure/AdminSettleSeller/${userInfo}/${user.getNuID()}">Settle</a></td>
				</tr>

			</c:forEach>
		</c:if>
	</table>
</body>
</html>