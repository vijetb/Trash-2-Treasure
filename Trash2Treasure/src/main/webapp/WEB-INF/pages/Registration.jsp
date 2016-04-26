<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<script type="text/javascript">

	function validateRegistration() {
		var firstName = document.getElementById("firstName").value;
		var lastName = document.getElementById("lastName").value;
		var emailId = document.getElementById("emailId").value;
		var nuID = document.getElementById("nuID").value;
		//var userName = document.getElementById("userName").value;
		var password = document.getElementById("password").value;
		var confirmPassword = document.getElementById("confirmPassword").value;
		var phoneNumber = document.getElementById("phoneNumber").value;
		var isAdmin = document.getElementById("admin");
		var securityQuestion1 = document.getElementById("securityQuestion1");
		var question1 = securityQuestion1[securityQuestion1.selectedIndex].value;
		var securityAnswer1 = document.getElementById("securityAnswer1").value;
		var securityQuestion2 = document.getElementById("securityQuestion2");
		var question2 = securityQuestion2[securityQuestion2.selectedIndex].value;
		var securityAnswer2 = document.getElementById("securityAnswer2").value;
		
		if (firstName.length == 0) {
			alert("First name cannot be blank..!!");
			return false;
		} else if (lastName.length == 0) {
			alert("Last name cannot be blank..!!");
			return false;
		} else if (emaidId.length == 0) {
			alert("Email id cannot be blank..!!");
			return false;
		} else if (nuID.length == 0) {
			alert("NU ID cannot be blank..!!");
			return false;
		} else if (Number(nuID) != nuID) {
			alert("Inside NUID Check")
			alert("Invalid NU ID input..!! It has to be a number");
			return false;
		} /* else if (userName.length == 0) {
			alert("Username cannot be blank..!!");
			return false;
		} */ else if (password.length == 0) {
			alert("Password cannot be blank..!!");
			return false;
		} else if (confirmPassword.length == 0) {
			alert("Confirm Password cannot be blank..!!");
			return false;
		} else if (password != confirmPassword) {
			alert("Passwords do not match..!!");
			return false;
		}else if (Number(phoneNumber) != phoneNumber) {
			alert("Invalid Phone number input..!! It has to be a number");
			return false;
		} else if (phoneNumber.length != 10) {
			alert("Phone number has to be exactly 10 digits long..!!");
			return false;
		} else if(isAdmin.checked){
			var adminCode = document.getElementById("adminCode").value;
			if (adminCode.length==0){
				alert("Admin passcode cannot be blank..!!");
				return false;
			}
		} else if (question1 == "Please select a question"){
			alert("Please select Security Question 1..!!");
			return false;
		} else if (securityAnswer1.length == 0) {
			alert("Security Answer 1 cannot be blank..!!");
			return false;
		} else if(question2 == "Please select a question"){
			alert("Please select Security Question 2..!!");
			return false;
		} else if (securityAnswer2.length == 0) {
			alert("Security Answer 2 cannot be blank..!!");
			return false;
		} 
		
		//return true;
		
		
	}
	
	function ShowHideDiv() {
		var admin = document.getElementById("admin");
		var adminCodeDiv = document.getElementById("adminCodeDiv");
		adminCodeDiv.style.display = admin.checked ? "block" : "none";
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


<h2>${msg} </h2>

	<form name="registrationForm"
		action="/Trash2Treasure/registrationSubmission"
		onsubmit="return validateRegistration()" method="post">
		<table align="center">
			<tr>
				<td>First Name</td>
				<td><input type="text" name="firstName" id="firstName" /></td>
			</tr>

			<tr>
				<td>Last Name</td>
				<td><input type="text" name="lastName" id="lastName" /></td>
			</tr>

			<tr>
				<td>Email ID</td>
				<td><input type="text" name="emailId" id="emailId" /></td>
			</tr>

			<tr>
				<td>NU ID</td>
				<td><input type="text" name="nuID" id="nuID" /></td>
			</tr>

			<!-- <tr>
				<td>Username</td>
				<td><input type="text" name="userName" id="userName" /></td>
			</tr>  -->

			<tr>
				<td>Password</td>
				<td><input type="password" name="password" id="password" /></td>
			</tr>

			<tr>
				<td>Confirm Password</td>
				<td><input type="password" name="confirmPassword"
					id="confirmPassword" /></td>
			</tr>

			<tr>
				<td>Phone Number</td>
				<td><input type="text" name="phoneNumber" id="phoneNumber" /></td>
			</tr>
			<tr>
				<td colspan="2">Are you a Seller or Admin?</td>
			</tr>
			<tr>
				<td><input type="radio" name="isAdmin" id="admin" value="true"
					onclick="ShowHideDiv()" />Admin</td>
				<td><input type="radio" name="isAdmin" id="seller" value="false"
					 checked="checked" onclick="ShowHideDiv()" /> Seller</td>
			</tr>

			<tr>
				<td colspan="2">

					<div id="adminCodeDiv" style="display: none">
						Please enter the Admin code: <input type="text" name="adminCode"
							id="adminCode"/>
					</div>
				</td>
			</tr>

			<tr>
				<td colspan="2">Security Question 1 <select
					name="securityQuestion1" id="securityQuestion1" size="1">
						<option value="Please select a question">Please select a
							question</option>
						<option value="What is your pet's name?">What is your
							pet's name?</option>
						<option value="Where were you born?">Where were you born?</option>
						<option value="What is your school name?">What is your
							school name?</option>
				</select></td>

			</tr>

			<tr>
				<td colspan="2">Security Answer 1 <input type="text"
					name="securityAnswer1" id="securityAnswer1" /></td>
			</tr>

			<tr>
				<td colspan="2">Security Question 2 <select
					name="securityQuestion2" id="securityQuestion2" size="1">
						<option value="Please select a question">Please select a
							question</option>
						<option value="What is your dream travel destination?">What
							is your dream travel destination?</option>
						<option value="What is your favorite sport's team?">What
							is your favorite sport's team?</option>
						<option value="What cuisine do you like the most?">What
							cuisine do you like the most?</option>
				</select></td>

			</tr>

			<tr>
				<td colspan="2">Security Answer 2 <input type="text"
					name="securityAnswer2" id="securityAnswer2" /></td>
			</tr>

			<tr>
				<td colspan="2" align="center"><input type="submit" value="Register" /></td>

			</tr>
			<tr>
					<td><input type="hidden" value= userInfo name="userInfo"></td>
					</tr>
					
		</table>
	</form>
	<p align="center"> Already a member?&nbsp;&nbsp;<a href="/Trash2Treasure/login">Login</a>
<br>
<br>
<a href="/Trash2Treasure/viewItemsBuyer">View Items Available</a></p>
</body>
</html>