<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	function validateSortItems() {

		var firstName = document.getElementById("firstName").value;
		var lastName = document.getElementById("lastName").value;
		var emailId = document.getElementById("emailId").value;
		var phoneNumber = document.getElementById("phoneNumber").value;
		
		if (firstName.length == 0) {
			alert("First name cannot be blank..!!");
			return false;
		} else if (lastName.length == 0) {
			alert("Last name cannot be blank..!!");
			return false;
		} else if (emaidId.length == 0) {
			alert("Email id cannot be blank..!!");
			return false;
		}else if (Number(phoneNumber) != phoneNumber) {
			alert("Invalid Phone number input..!! It has to be a number");
			return false;
		} else if (phoneNumber.length != 10) {
			alert("Phone number has to be exactly 10 digits long..!!");
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
	<form name="userProfileUpdateForm" action="/Trash2Treasure/UpdateUserInformation" onsubmit="return validateSortItems()" method="post">
		<table align="center">
			<tr>
				<td>Firstname</td>
				<td><input type="text" name="firstName" id="firstName"
					value="${userBean.firstName}"></td>
			</tr>

			<tr>
				<td>Lastname</td>
				<td><input type="text" name="lastName" id="lastName"
					value="${userBean.lastName}"></td>
			</tr>
			<tr>
				<td>Email ID</td>
				<td><input type="text" name="emailId" id="emailId"
					value="${userBean.emailId}"></td>
			</tr>
			<tr>
				<td>Phone Number</td>
				<td><input type="text" name="phoneNumber" id="phoneNumber"
					value="${userBean.phoneNumber}"></td>
			</tr>
			<tr>
				<td><input type="hidden" value="${userInfo}" name="userInfo"></td>
			</tr>
			<tr>
				<td colspan="2"> <input type="submit" value="Update Profile"></td>
			</tr>


		</table>


	</form>

</body>
</html>