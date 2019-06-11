package com.bridgelabz.fundoo.note.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoo.note.dto.LabelDTO;
import com.bridgelabz.fundoo.note.dto.NoteDTO;
import com.bridgelabz.fundoo.note.model.Label;
import com.bridgelabz.fundoo.note.service.LabelService;
import com.bridgelabz.fundoo.response.Response;

@RestController
@RequestMapping("/user")
public class LabelController {
	@Autowired
	private LabelService LabelService;

	@PostMapping("/createLabel")
	public ResponseEntity<Response> createLabel(@RequestBody LabelDTO labeldto, @RequestHeader String token) {
		Response response = LabelService.createLabel(labeldto, token);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@PutMapping("/updateLabel")
	public ResponseEntity<Response> updateLabel(@RequestBody LabelDTO labeldto, @RequestHeader String token,
			@RequestParam long labelId) {
		Response response = LabelService.updateLabel(labeldto, token, labelId);
		return new ResponseEntity<Response>(response, HttpStatus.ACCEPTED);
	}

	@PutMapping("/deleteLabel")
	public ResponseEntity<Response> deleteLabel(@RequestHeader String token, @RequestParam long labelId) {
		Response response = LabelService.deleteLabel(token, labelId);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@GetMapping("/getlabel")
	List<Label> getLabel(@RequestHeader String token) {
		List<Label> listLabel = LabelService.getAllLabelFromUser(token);
		return listLabel;
	}

	@PutMapping("/removeLabelFromNote")
	public ResponseEntity<Response> removeFromNote(@RequestParam long labelId, @RequestHeader String token,
			@RequestParam long noteId) {
		Response response = LabelService.removeLabelFromNote(labelId, token, noteId);
		return new ResponseEntity<Response>(response, HttpStatus.OK);

	}

	@PutMapping("/addLabelToNote")
	public ResponseEntity<Response> addLabelToNote(@RequestParam long labelId, @RequestHeader String token,
			@RequestParam long noteId) {
		Response response = LabelService.addLabelToNote(labelId, token, noteId);
		return new ResponseEntity<Response>(response, HttpStatus.OK);

	}

	@GetMapping("/getlabelofnote")
	List<LabelDTO> getLebelOfNote(@RequestHeader String token, @RequestParam long noteId) {
		List<LabelDTO> listLabel = LabelService.getLabelsOfNote(token, noteId);
		return listLabel;
	}
	
	 @GetMapping("/getNoteOfLabel")
	  List<NoteDTO> getNoteOfLabel(@RequestHeader String token,@RequestParam long labelId) throws IllegalArgumentException
	  {
		  List<NoteDTO>labelList=LabelService.getNotesOfLabel(token, labelId);
		  return labelList;
	  }

}
