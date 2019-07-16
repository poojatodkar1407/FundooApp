package com.bridgelabz.fundoo.user.dto;

public class EmailDto {

	private String emailId;

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	@Override
	public String toString() {
		return "EmailDto [emailId=" + emailId + "]";
	}

}
