package com.neu.trash2treasure.beans;

/**
 * Login Bean
 * @author Vijet Badigannavar
 */
public class Login {
	/**
	 * Id associated with the user
	 */
	private Long id;
	/**
	 * NUId associated with the NEU student
	 */
	private Long nuID;
	/**
	 * Password associated with the User.
	 */
	private String password;

	public Long getNuID() {
		return nuID;
	}

	public void setNuID(Long nuID) {
		this.nuID = nuID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
