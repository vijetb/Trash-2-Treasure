<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${userIdentification != 'verified'}">
   <c:redirect url="/Trash2Treasure/index.jsp"/>
</c:if>

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
<h2>${msg}</h2>
<p align="center">
<a href="/Trash2Treasure/UploadItem/${userInfo}">Upload an Item</a><br>
<a href="/Trash2Treasure/ModifyItem/${userInfo}">Modify an Existing Item</a><br>
<!-- <a href="/Trash2Treasure/UpdateItem">Update an Existing Item</a><br>  -->
<a href="/Trash2Treasure/ViewMyItems/${userInfo}">View My Items</a><br>
<a href="/Trash2Treasure/ViewAllItems/${userInfo}">View All Items</a><br>
<a href="/Trash2Treasure/UpdateProfile/${userInfo}">Update My Profile</a><br>
</p>
<input type="hidden" name="userInfo" value="${userInfo}">


Summary:
<table>
<tr>
<td>Amount you are owed:</td>
<td> ${amountOwed} </td>
</tr>

</table>

</body>
</html>