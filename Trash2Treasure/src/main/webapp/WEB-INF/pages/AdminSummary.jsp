<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
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

	<table border="1" align="center">
		<tr>
			<td>Number of <br>Items Unsold</td>
			<td>Number of <br>Items Sold</td>
			<td>Number of <br>Items Pending Approval</td>
		</tr>

		<tr>
			<td>${adminSummary.noItemsUnsold}</td>
			<td>${adminSummary.noItemsSold}</td>
			<td>${adminSummary.noItemsPending}</td>
		</tr>
		<tr>
		<td colspan="3" align="center"> <a href="/Trash2Treasure/AdminSettleItems/${userInfo}/">Settle amount for Sellers</a></td>
		</tr>
	</table>

</body>
</html>