package com.bridgelabz.fundoo.note.dto;

public class CollaboratorDTO {

	private String emailId;

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	@Override
	public String toString() {
		return "CollaboratorDTO [emailId=" + emailId + "]";
	}
	
}
