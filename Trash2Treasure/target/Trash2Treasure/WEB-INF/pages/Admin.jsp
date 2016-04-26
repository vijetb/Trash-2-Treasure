<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<c:if test="${userIdentification != 'verified'}">
   <c:redirect url="index.jsp"/>
</c:if>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<h2>${msg}</h2>

<a href="/Trash2Treasure/AdminApprovePendingItems/${userInfo}">Approve an Item</a><br>
<a href="/Trash2Treasure/AdminDeleteItem/${userInfo}">Delete an Existing Item</a><br>
<a href="/Trash2Treasure/AdminViewAllItems/${userInfo}">View All Items</a><br>
<a href="/Trash2Treasure/UpdateProfile/${userInfo}">Update My Profile</a><br>


<input type="hidden" value= "${userInfo}" name="userInfo">
</body>
</html>