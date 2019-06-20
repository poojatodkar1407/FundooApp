package com.bridgelabz.fundoo.note.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoo.note.dto.NoteDTO;
import com.bridgelabz.fundoo.note.model.Note;
import com.bridgelabz.fundoo.note.service.NoteService;
import com.bridgelabz.fundoo.response.Response;

@RestController
@RequestMapping("/user")
@CrossOrigin(allowedHeaders = "*", origins = "*")
@PropertySource("classpath:message.properties")
public class NoteController {

	@Autowired
	private NoteService noteService;

	@PostMapping("/createNote")
	public ResponseEntity<Response> creatingNote(@RequestBody NoteDTO noteDto, @RequestHeader String token) {
		System.out.println("NotesController.creatingNote()");
		Response responseStatus = noteService.createNote(noteDto, token);
		return new ResponseEntity<Response>(responseStatus, HttpStatus.OK);
	}

	@PutMapping("/updateNote")
	public ResponseEntity<Response> updatingNote(@RequestBody NoteDTO noteDto, @RequestHeader String token,
			@RequestParam long noteId) {

		Response responseStatus = noteService.updateNote(noteDto, token, noteId);
		return new ResponseEntity<Response>(responseStatus, HttpStatus.ACCEPTED);
	}

	@PutMapping("/retrieveNote")
	public ResponseEntity<Response> retrievingNote(@RequestHeader String token, @RequestParam long noteId) {

		Response responseStatus = noteService.retrieveNote(token, noteId);
		return new ResponseEntity<Response>(responseStatus, HttpStatus.OK);
	}

	@PutMapping("/deleteNote")
	public ResponseEntity<Response> deletingNote(@RequestHeader String token, @RequestParam long noteId) {
		Response responseStatus = noteService.deleteNote(token, noteId);
		return new ResponseEntity<Response>(responseStatus, HttpStatus.OK);
	}

	@PutMapping("/permenantlyDeleted")
	public ResponseEntity<Response> permenantdeletingNote(@RequestHeader String token, @RequestParam long noteId) {
		Response responsestatus = noteService.deleteNotePermenantly(token, noteId);
		return new ResponseEntity<Response>(responsestatus, HttpStatus.OK);

	}
	
	@PutMapping("/pinned")
	public ResponseEntity<Response> pinnedOrNot(@RequestHeader String token ,@RequestParam long noteId)
	{
		Response responseStatus = noteService.checkPinOrNot(token, noteId);
		return new ResponseEntity<Response>(responseStatus,HttpStatus.OK);	
	}
	
	@PutMapping("/archieved")
	public ResponseEntity<Response> archievedOrNot(@RequestHeader String token, @RequestParam long noteId)
	{
		Response responseStatus = noteService.checkArchieveOrNot(token, noteId);
		return new ResponseEntity<Response>(responseStatus,HttpStatus.OK);	
	}
	
	@PutMapping("/color")
	public ResponseEntity<Response> colorSet(@RequestHeader String token, @RequestParam long noteId, @RequestParam String color)
	{
		Response responseStatus = noteService.setColour(token, noteId, color);
		return new ResponseEntity<Response>(responseStatus,HttpStatus.OK);
		
	}
	
	@GetMapping("/getTrash")
	public List<Note> getTrashNotes(@RequestHeader String token) {
		List<Note> listnotes = noteService.restoreTrashNotes(token);
		return listnotes;
	}
	

	@GetMapping("/getPin")
	public List<Note> getPinnedNotes(@RequestHeader String token) {
		List<Note> listnotes = noteService.getPinnedNote(token);
		return listnotes;
	}
	
	@GetMapping("/getArchieve")
	public List<Note> getArchieveNotes(@RequestHeader String token) {
		List<Note> listnotes = noteService.getArchievedNote(token);
		return listnotes;
	}
	
	@GetMapping("/findNote")
	public Note getNote(@RequestHeader String title, @RequestHeader String description)
	{
		Note note = noteService.findNoteFromUser(title, description);	
		return note;
		
	}
	
	@PutMapping("/setRemainder")
	public ResponseEntity<Response> setRemainderToNote(@RequestHeader String token ,@RequestParam long noteId,@RequestParam String time)
	{
		Response response = noteService.setReminder(token, noteId, time);
		
		return new ResponseEntity<Response>(response,HttpStatus.OK);
		
	}
	
	@PutMapping("/deleteReminder")
	public ResponseEntity<Response> deleteRemainderToNote(@RequestHeader String token, @RequestParam long noteId)
	{
		Response response = noteService.deleteReminder(token, noteId);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	@PutMapping("/addCollaborator")
	public ResponseEntity<Response> addCollaborator(@RequestHeader String token, @RequestParam long noteId , @RequestParam String emailId)
	{
		Response response = noteService.addCollaborator(token,emailId, noteId);
		return new ResponseEntity<Response>(response,HttpStatus.OK);		
	}
	
	@PutMapping("/deleteCollaborator")
	public ResponseEntity<Response> deleteCollaborator(@RequestHeader String token , @RequestParam long noteId,@RequestParam String emailId)
	{
		Response response = noteService.deleteCollaboratorToNote(token, noteId, emailId);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	@PostMapping("/setImage")
	public ResponseEntity<Response> uploadImage(@RequestHeader String token ,@RequestParam long noteId, @RequestParam MultipartFile imageFile) throws IOException
	{
		Response response = noteService.uploadImageToNote(token, noteId, imageFile);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
		
	}
}
