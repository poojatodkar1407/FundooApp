package com.bridgelabz.fundoo.note.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Entity
@Component
@Table(name = "Collaborator_table")
public class Collaborator {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long collaboratorId;
	private String emailId;
	private long noteId;
	private long userId;
	private LocalDateTime createdAt;

	public long getCollaboratorId() {
		return collaboratorId;
	}

	public void setCollaboratorId(long collaboratorId) {
		this.collaboratorId = collaboratorId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public long getNoteId() {
		return noteId;
	}

	public void setNoteId(long noteId) {
		this.noteId = noteId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Collaborator [collaboratorId=" + collaboratorId + ", emailId=" + emailId + ", noteId=" + noteId
				+ ", userId=" + userId + ", createdAt=" + createdAt + "]";
	}

}
