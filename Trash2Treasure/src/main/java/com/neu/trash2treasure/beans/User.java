package com.neu.trash2treasure.beans;

/**
 * User Bean
 * @author Vijet Badigannavar
 **/
public class User {
	/**
	 * Id associated with the User
	 */
	private Long id;
	/**
	 * First name associated with User
	 */	
	private String firstName;
	/**
	 * Last name associated with User
	 */	
	private String lastName;
	/**
	 * EmailId of User
	 */	
	private String emailId;
	/**
	 * NUId of the user
	 */
	private Long nuID;
	/**
	 * Password associated with the User
	 */
	private String password;
	/**
	 * Phone number of the user
	 */
	private String phoneNumber;
	/**
	 * Boolean value of the admin
	 */
	private Boolean isAdmin;
	/**
	 * Code of the admin
	 */
	private String adminCode;
	/**
	 * SecurityQuestions and Answers.
	 */
	private String securityQuestion1;
	private String securityAnswer1;
	private String securityQuestion2;
	private String securityAnswer2;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Long getNuID() {
		return nuID;
	}

	public void setNuID(Long nuID) {
		this.nuID = nuID;
	}

	public String getAdminCode() {
		return adminCode;
	}

	public void setAdminCode(String adminCode) {
		this.adminCode = adminCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getSecurityQuestion1() {
		return securityQuestion1;
	}

	public void setSecurityQuestion1(String securityQuestion1) {
		this.securityQuestion1 = securityQuestion1;
	}

	public String getSecurityAnswer1() {
		return securityAnswer1;
	}

	public void setSecurityAnswer1(String securityAnswer1) {
		this.securityAnswer1 = securityAnswer1;
	}

	public String getSecurityQuestion2() {
		return securityQuestion2;
	}

	public void setSecurityQuestion2(String securityQuestion2) {
		this.securityQuestion2 = securityQuestion2;
	}

	public String getSecurityAnswer2() {
		return securityAnswer2;
	}

	public void setSecurityAnswer2(String securityAnswer2) {
		this.securityAnswer2 = securityAnswer2;
	}

}
