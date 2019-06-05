package com.bridgelabz.fundoo.note.service;

import com.bridgelabz.fundoo.note.dto.NoteDTO;
import com.bridgelabz.fundoo.response.Response;

public interface NoteService {

	public Response createNote(NoteDTO notedto, String token);
	public Response updateNote(NoteDTO notedto, String token, long noteId);
	
	public Response retrieveNote(String token, long noteId);
	public Response deleteNote(String token, long noteId);
	public Response deleteNotePermenantly(String token , long noteId);
}
