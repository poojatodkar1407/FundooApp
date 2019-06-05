package com.bridgelabz.fundoo.note.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoo.exception.UserException;
import com.bridgelabz.fundoo.note.dto.NoteDTO;
import com.bridgelabz.fundoo.note.model.Note;
import com.bridgelabz.fundoo.note.repository.*;
import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.user.model.User;
import com.bridgelabz.fundoo.user.repository.*;
import com.bridgelabz.fundoo.utility.ResponseHelper;
import com.bridgelabz.fundoo.utility.TokenUtil;

@Service("notesService")
@PropertySource("classpath:message.properties")
public class NotesServiceImpl implements NoteService {

	@Autowired
	private TokenUtil userToken;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private Environment environment;

	@Override
	public Response createNote(NoteDTO noteDto, String token) {

		long id = userToken.decodeToken(token);

		if (noteDto.getTitle().isEmpty() && noteDto.getDescription().isEmpty()) {

			throw new UserException(-5, "Title and description are empty");

		}
		Note note = modelMapper.map(noteDto, Note.class);
		Optional<User> user = userRepository.findById(id);
		note.setUserId(id);
		note.setCreated(LocalDateTime.now());
		note.setModified(LocalDateTime.now());
		user.get().getNotes().add(note);
		noteRepository.save(note);
		userRepository.save(user.get());

		Response response = ResponseHelper.statusResponse(100,
				environment.getProperty("status.notes.createdSuccessfull"));
		return response;

	}

	@Override
	public Response updateNote(NoteDTO noteDto, String token, long noteId) {
		if (noteDto.getTitle().isEmpty() && noteDto.getDescription().isEmpty()) {
			throw new UserException(-5, "Title and description are empty");
		}

		long id = userToken.decodeToken(token);
		Note note = noteRepository.findByUserIdAndNoteId(id, noteId);
		note.setTitle(noteDto.getTitle());
		note.setDescription(noteDto.getDescription());
		note.setModified(LocalDateTime.now());
		noteRepository.save(note);

		Response response = ResponseHelper.statusResponse(200,
				environment.getProperty("status.notes.updatedSuccessfull"));
		return response;
	}

	@Override
	public Response retrieveNote(String token, long noteId) {

		long id = userToken.decodeToken(token);
		Note note = noteRepository.findByUserIdAndNoteId(id, noteId);
		String title = note.getTitle();
		System.out.println(title);

		String description = note.getDescription();
		System.out.println(description);

		Response response = ResponseHelper.statusResponse(300, "retrieved successfully");
		return response;
	}

	public Response deleteNote(String token, long noteId) {
		long id = userToken.decodeToken(token);

		Note note = noteRepository.findByUserIdAndNoteId(id, noteId);

		if (note.isTrash() == false) {
			note.setTrash(true);
			note.setModified(LocalDateTime.now());
			noteRepository.save(note);
			Response response = ResponseHelper.statusResponse(100, environment.getProperty("status.note.trashed"));
			return response;
		}

		Response response = ResponseHelper.statusResponse(100, environment.getProperty("status.note.trashError"));
		return response;
	}

	public Response deleteNotePermenantly(String token, long noteId) {

		long id = userToken.decodeToken(token);

		Optional<User> user = userRepository.findById(id);
		Note note = noteRepository.findByUserIdAndNoteId(id, noteId);

		if (note.isTrash() == true) {
			user.get().getNotes().remove(note);
			userRepository.save(user.get());
			noteRepository.delete(note);
			Response response = ResponseHelper.statusResponse(200, environment.getProperty("status.note.deleted"));
			return response;
		} else {
			Response response = ResponseHelper.statusResponse(100, environment.getProperty("status.note.noteDeleted"));
			return response;
		}
	}
}
