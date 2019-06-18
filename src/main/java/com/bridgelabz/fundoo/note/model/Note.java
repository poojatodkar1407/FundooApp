package com.bridgelabz.fundoo.note.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import com.bridgelabz.fundoo.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Component
@Entity
public class Note implements Serializable {

	private static final long serialVersionUID = 3891749725421598901L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long noteId;
	private long userId;
	@NotNull
	@NotEmpty(message = "title should not be empty")
	private String title;
	@NotNull
	@NotEmpty(message = "description should not be empty")
	private String description;
	private String colour;
	private LocalDateTime created;
	private LocalDateTime modified;
	private boolean isPin;
	private boolean isArchieve;
	private LocalDateTime remainder;
	private String noteImage;

	public String getNoteImage() {
		return noteImage;
	}

	public void setNoteImage(String noteImage) {
		this.noteImage = noteImage;
	}

	public LocalDateTime getRemainder() {
		return remainder;
	}

	public void setRemainder(LocalDateTime remainder) {
		this.remainder = remainder;
	}

	public boolean isArchieve() {
		return isArchieve;
	}

	public void setArchieve(boolean isArchieve) {
		this.isArchieve = isArchieve;
	}

	public boolean isPin() {
		return isPin;
	}

	public void setPin(boolean isPin) {
		this.isPin = isPin;
	}

	private boolean isTrash;

	public boolean isTrash() {
		return isTrash;
	}

	public void setTrash(boolean isTrash) {
		this.isTrash = isTrash;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getModified() {
		return modified;
	}

	public void setModified(LocalDateTime modified) {
		this.modified = modified;
	}

	@Override
	public String toString() {
		return "Note [noteId=" + noteId + ", userId=" + userId + ", title=" + title + ", description=" + description
				+ ", colour=" + colour + ", created=" + created + ", modified=" + modified + "]";
	}

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Label> listLabel;

	public List<Label> getListLabel() {
		return listLabel;
	}

	public void setListLabel(List<Label> listLabel) {
		this.listLabel = listLabel;
	}

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	private Set<User>collaboratedUser;

	public Set<User> getCollaboratedUser() {
		return collaboratedUser;
	}

	public void setCollaboratedUser(Set<User> collaboratedUser) {
		this.collaboratedUser = collaboratedUser;
	}

}
