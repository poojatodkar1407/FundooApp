package com.bridgelabz.fundoo.note.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoo.note.dto.NoteDTO;
import com.bridgelabz.fundoo.note.model.Note;
import com.bridgelabz.fundoo.response.Response;

public interface NoteService {

	public Response createNote(NoteDTO notedto, String token);
	public Response updateNote(NoteDTO notedto, String token, long noteId);	
	public Response retrieveNote(String token, long noteId);
	public Response deleteNote(String token, long noteId);
	public Response deleteNotePermenantly(String token, long noteId);
	public Response checkPinOrNot(String token, long noteId);
	public List<Note> restoreTrashNotes(String token);
	public Response checkArchieveOrNot(String token, long noteId);
	public Response setColour(String token, long noteId, String color);
	public List<Note> getPinnedNote(String token);
	public List<Note> getArchievedNote(String token);
	public Note findNoteFromUser(String title, String description);
	public Response setReminder(String token, long noteId, String time);
	public Response deleteReminder(String token, long noteId);
	//public Response deleteCollaboratorFromNote(String token, long noteId);
	//Response addCollaboratorToNote(String token, String email, long noteId);
	public Response deleteCollaboratorToNote(String token, long noteId, String emailId);
	public Response uploadImageToNote(String token, long noteId, MultipartFile imageFile);
	public Response addCollaborator(String token, String email, long noteId);
	List<Note> getAllNote(String token);
	List<Note> getUnpinnedNote(String token);
	Response restoreMainNote(String token,long noteId);
	String getToken(String emailId);
	
}