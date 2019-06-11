package com.bridgelabz.fundoo.note.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

	@Override
	public Response checkPinOrNot(String token, long noteId) {

		long userId = userToken.decodeToken(token);
		System.out.println(userId);
		Note note = noteRepository.findByUserIdAndNoteId(userId, noteId);
		System.out.println("note is" + note);

		if (note == null) {
			throw new UserException(100, "note is not exist");
		}

		if (note.isPin() == false) {
			note.setPin(true);
			noteRepository.save(note);

			Response response = ResponseHelper.statusResponse(200, environment.getProperty("status.note.pinned"));
			return response;
		} else {
			note.setPin(false);
			noteRepository.save(note);
			Response response = ResponseHelper.statusResponse(200, environment.getProperty("status.note.unpinned"));
			return response;
		}
	}

	@Override
	public Response checkArchieveOrNot(String token, long noteId) {

		long userId = userToken.decodeToken(token);
		Note note = noteRepository.findByUserIdAndNoteId(userId, noteId);

		if (note == null) {
			throw new UserException(100, "note is not exist");
		}

		if (note.isArchieve() == false) {
			note.setArchieve(true);
			noteRepository.save(note);

			Response response = ResponseHelper.statusResponse(200, environment.getProperty("status.note.archieved"));
			return response;
		} else {
			note.setArchieve(false);
			noteRepository.save(note);

			Response response = ResponseHelper.statusResponse(200, environment.getProperty("status.note.unarchieved"));
			return response;
		}

	}

	@Override
	public Response setColour(String token, long noteId, String color) {

		long userId = userToken.decodeToken(token);
		Note note = noteRepository.findByUserIdAndNoteId(userId, noteId);
		if (note == null) {
			throw new UserException(100, "invalid note or not exist");
		}

		note.setColour(color);
		noteRepository.save(note);

		Response response = ResponseHelper.statusResponse(200, environment.getProperty("status.note.color"));
		return response;
	}

	/*
	 * public List<Note> restoreTrashNotes(String token)
	 * {
	  		long userId = userToken.decodeToken(token);
	  		List<Note> trashNote = noteRepository.findByUserId(userId);
	  		List<Note> listTrashNote = new ArrayList<>();
	  		
	  		for(Note userNotes : trashNote)
	  		{
	  			Note noteDto = modelmapper.map(userNotes,Note.class);
	  			if(userNotes.isTrash() == true)
	  			{
	  				listTrashNote.add(noteDto);
	  			}
	  			
	  		}
	 * }
	 */
	@Override
	public List<Note> restoreTrashNotes(String token) {

		long userId = userToken.decodeToken(token);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserException("Sorry ! Note are not available"));
		List<Note> userNote = user.getNotes().stream().filter(data -> (data.isTrash() == true))
				.collect(Collectors.toList());
		
		return userNote;

	}

	/*

	 public List<Note> getPinnedNote(String token)
	 {
	 long userId = userToken.decode(token);
	 User user = userRepository.fingById(userId).orElseThrow(()->new UserException("not available"));

	 List<Note> pinNote = noteRepository.findByUserId(userId);
	 List<Note> listPinNote = new ArrayList<>();
	 
	 for (Note userNotes : pinNote) {
			Note notesDto = modelMapper.map(userNotes, Note.class);
			if (userNotes.isArchive() == true) {
				listNotes.add(notesDto);

			}
	 
	 */
	@Override
	public List<Note> getPinnedNote(String token) {
		long userId = userToken.decodeToken(token);

		User user = userRepository.findById(userId).orElseThrow(() -> new UserException("No note is available"));
		List<Note> pinNote = user.getNotes().stream().filter(data -> (data.isPin() == true))
				.collect(Collectors.toList());
		user.getNotes().stream().filter(data1 -> (data1.isPin() == true)).collect(Collectors.toList()).forEach(System.out::println);
		
		return pinNote;

	}
	/*
	 * 
	 * 	public List<Note> getArchivedNotes(String token) {
		long id = userToken.decodeToken(token);
		List<Note> notes = (List<Note>) notesRepository.findByUserId(id);
		List<Note> listNotes = new ArrayList<>();
		for (Note userNotes : notes) {
			Note notesDto = modelMapper.map(userNotes, Note.class);
			if (userNotes.isArchive() == true) {
				listNotes.add(notesDto);

			}
		}
		return listNotes;
	}
	 */
	@Override
	public List<Note> getArchievedNote(String token) {
		long userId = userToken.decodeToken(token);

		User user = userRepository.findById(userId).orElseThrow(() -> new UserException("No note is available"));
		List<Note> archieveNote = user.getNotes().stream().filter(data -> (data.isArchieve() == true))
				.collect(Collectors.toList());

		user.getNotes().stream().filter(data1 -> (data1.isArchieve() == true)).collect(Collectors.toList()).forEach(System.out::println);
		return archieveNote;

	}
}
