package com.bridgelabz.fundoo.note.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoo.note.dto.NoteDTO;
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

}
