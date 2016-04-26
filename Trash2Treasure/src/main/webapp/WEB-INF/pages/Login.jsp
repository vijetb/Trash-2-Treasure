<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
function validateLogin(){
	var userName = document.getElementById("userName").value;
	var password = document.getElementById("password").value;
	
	if(userName.length == 0){
		alert("Username cannot be blank..!!");
		return false;
	} else if(password.length == 0){
		alert("password cannot be blank..!!");
		return false;
	}
	return true;
	
}

</script>

</head>
<body bgcolor="yellow">
<marquee behavior="scroll"><font size="14">Trash-2-Treasure</font></marquee>
<br>
<br>
<br>
<br>

<h2>${loginFailure}</h2>

	<form name="loginForm" action="/Trash2Treasure/loginUser"
		onsubmit="return validateLogin()" method="post">
		<table align="center">
			<tr>
				<td>NUID</td>
				<td><input type="text" name="nuID" id="nuID"></td>
			</tr>
			
			<tr>
				<td>Password</td>
				<td><input type="password" name="password" id="password"></td>
			</tr>
			
			<tr>
				<td colspan="2" align="center"><input type="submit" value="Login"></td>
			</tr>
		</table>
		</form>
<p align="center">
Not a member? &nbsp;<a href="/Trash2Treasure/userRegister">Register</a>
<br>
<a href="/Trash2Treasure/viewItemsBuyer">View Items Available</a>
</p>
</body>
</html>