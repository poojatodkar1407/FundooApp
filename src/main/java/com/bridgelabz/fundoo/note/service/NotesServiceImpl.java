package com.bridgelabz.fundoo.note.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoo.exception.UserException;
import com.bridgelabz.fundoo.note.dto.CollaboratorDTO;
import com.bridgelabz.fundoo.note.dto.NoteDTO;
import com.bridgelabz.fundoo.note.model.Collaborator;
import com.bridgelabz.fundoo.note.model.Note;
import com.bridgelabz.fundoo.note.repository.CollaboratorRepository;
import com.bridgelabz.fundoo.note.repository.NoteRepository;
import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.user.model.EmailId;
import com.bridgelabz.fundoo.user.model.User;
import com.bridgelabz.fundoo.user.repository.UserRepository;
import com.bridgelabz.fundoo.utility.ResponseHelper;
import com.bridgelabz.fundoo.utility.TokenUtil;
import com.bridgelabz.fundoo.utility.Utility;

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
	private CollaboratorRepository collaboratorRepository;

	@Autowired
	private Environment environment;

	private final Path fileLocation = Paths.get("/home/admin1/Pictures/Wallpapers/");

	
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
		Note note = noteRepository.findByUserIdAndNoteId(userId, noteId);

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
	 * public List<Note> restoreTrashNotes(String token) { long userId =
	 * userToken.decodeToken(token); List<Note> trashNote =
	 * noteRepository.findByUserId(userId); List<Note> listTrashNote = new
	 * ArrayList<>();
	 * 
	 * for(Note userNotes : trashNote) { Note noteDto =
	 * modelmapper.map(userNotes,Note.class); if(userNotes.isTrash() == true) {
	 * listTrashNote.add(noteDto); }
	 * 
	 * } }
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
	 * 
	 * public List<Note> getPinnedNote(String token) { long userId =
	 * userToken.decode(token); User user =
	 * userRepository.fingById(userId).orElseThrow(()->new
	 * UserException("not available"));
	 * 
	 * List<Note> pinNote = noteRepository.findByUserId(userId); List<Note>
	 * listPinNote = new ArrayList<>();
	 * 
	 * for (Note userNotes : pinNote) { Note notesDto = modelMapper.map(userNotes,
	 * Note.class); if (userNotes.isArchive() == true) { listNotes.add(notesDto);
	 * 
	 * }
	 * 
	 */
	@Override
	public List<Note> getPinnedNote(String token) {
		long userId = userToken.decodeToken(token);

		User user = userRepository.findById(userId).orElseThrow(() -> new UserException("No note is available"));
		List<Note> pinNote = user.getNotes().stream().filter(data -> (data.isPin() == true))
				.collect(Collectors.toList());
		user.getNotes().stream().filter(data1 -> (data1.isPin() == true)).collect(Collectors.toList())
				.forEach(System.out::println);

		return pinNote;

	}

	/*
	 * 
	 * public List<Note> getArchivedNotes(String token) { long id =
	 * userToken.decodeToken(token); List<Note> notes = (List<Note>)
	 * notesRepository.findByUserId(id); List<Note> listNotes = new ArrayList<>();
	 * for (Note userNotes : notes) { Note notesDto = modelMapper.map(userNotes,
	 * Note.class); if (userNotes.isArchive() == true) { listNotes.add(notesDto);
	 * 
	 * } } return listNotes; }
	 */
	@Override
	public List<Note> getArchievedNote(String token) {
		long userId = userToken.decodeToken(token);

		User user = userRepository.findById(userId).orElseThrow(() -> new UserException("No note is available"));
		List<Note> archieveNote = user.getNotes().stream().filter(data -> (data.isArchieve() == true))
				.collect(Collectors.toList());

		user.getNotes().stream().filter(data1 -> (data1.isArchieve() == true)).collect(Collectors.toList())
				.forEach(System.out::println);
		return archieveNote;

	}

	@Override
	public Note findNoteFromUser(String title, String description) {

		Note note = noteRepository.findByTitleAndDescription(title, description);
		System.out.println(note);
		if (note == null) {
			throw new UserException(-6, "note is not available");
		}

		return note;
	}

	@Override
	public Response setReminder(String token, long noteId, String time) {
		long userId = userToken.decodeToken(token);
		Note note = noteRepository.findByUserIdAndNoteId(userId, noteId);

		if (note == null) {
			throw new UserException(-5, "invalid note");
		}

		System.out.println("time is" + time);
		DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime localDateTime = LocalDateTime.parse(time, datetimeFormatter);
		System.out.println(localDateTime);
		LocalDateTime CurrentDateAndTime = LocalDateTime.now();
		System.out.println(CurrentDateAndTime);
		if (CurrentDateAndTime.compareTo(localDateTime) < 0) {
			note.setRemainder(localDateTime);
			noteRepository.save(note);
			Response response = ResponseHelper.statusResponse(100, environment.getProperty("note.status.remainder"));
			return response;
		}

		Response response = ResponseHelper.statusResponse(101, environment.getProperty("note.status.remainderfail"));

		return response;
	}

	@Override
	public Response deleteReminder(String token, long noteId) {
		long userId = userToken.decodeToken(token);
		Note note = noteRepository.findByUserIdAndNoteId(userId, noteId);
		if (note == null) {
			throw new UserException(-5, "note invalid");
		}

		note.setRemainder(null);
		noteRepository.save(note);

		Response response = ResponseHelper.statusResponse(100, environment.getProperty("note.status.deleteRemainder"));

		return response;

	}

	@Override
	public Response addCollaboratorToNote(String token, long noteId, CollaboratorDTO collaboratordto) {
		EmailId emailId = new EmailId();
		long userId = userToken.decodeToken(token);

		Note note = noteRepository.findByUserIdAndNoteId(userId, noteId);

		if (note == null) {
			throw new UserException(-5, "note invalid");
		}
		Optional<Collaborator> userCollaborator = collaboratorRepository.findByEmailId(collaboratordto.getEmailId());

		if (userCollaborator.isPresent()) {
			throw new UserException(-5, "collaborator already present");
		}

		Collaborator collaborator = modelMapper.map(collaboratordto, Collaborator.class);
		Optional<User> mainUser = userRepository.findById(userId);
		Optional<User> collaborateUser = userRepository.findByEmailId(collaboratordto.getEmailId());
		System.out.println(collaborateUser);
		collaborator.setEmailId(collaboratordto.getEmailId());
		collaborator.setNoteId(noteId);
		collaborator.setUserId(userId);
		collaborator.setCreatedAt(LocalDateTime.now());

		collaborateUser.get().getCollaboratedNotes().add(note);
		note.getCollaboratedUser().add(collaborateUser.get());

		collaboratorRepository.save(collaborator);
		noteRepository.save(note);

		emailId.setFrom("poojasparkle124@gmail.com");
		emailId.setTo(collaboratordto.getEmailId());
		emailId.setSubject("Note collaborate to user");
		emailId.setBody("note collaboration from" + mainUser.get().getEmailId() + "to collaborating to"
				+ collaboratordto.getEmailId() + " for following note : title" + note.getTitle() + "and Description is"
				+ note.getDescription());

		Utility.sendEmail(emailId);

		Response response = ResponseHelper.statusResponse(100, environment.getProperty("collaborator.status.create"));

		return response;

	}

	@Override
	public Response deleteCollaboratorToNote(String token, long noteId, String emailId) {
		long userId = userToken.decodeToken(token);
		Optional<User> user = userRepository.findByEmailId(emailId);

		if (!user.isPresent()) {
			throw new UserException(-5, "user is not exist");
		}

		Note note = noteRepository.findByUserIdAndNoteId(userId, noteId);
		if (note == null) {
			throw new UserException(-5, "note is not exist");
		}

		Optional<Collaborator> collaborator = collaboratorRepository.findByEmailId(emailId);
		if (collaborator == null) {
			throw new UserException(-5, "collaborator is not exist");
		}
		
		user.get().getCollaboratedNotes().remove(note);
		note.getCollaboratedUser().remove(user.get());

		collaboratorRepository.delete(collaborator.get());
		userRepository.save(user.get());
		noteRepository.save(note);

		Response response = ResponseHelper.statusResponse(100, environment.getProperty("status.collaborator.deleted"));
		return response;
	}

	/*
	 * public Response removeCollaborator(String token, String email, long noteId) {
	 * 
	 * long userId = userToken.decodeToken(token);// Optional<User> user =
	 * userRepository.findByEmailId(email);
	 * 
	 * if (!user.isPresent()) throw new UserException(-4, "No user exist");
	 * 
	 * Note note = notesRepository.findByIdAndUserId(noteId, userId);
	 * 
	 * if (note == null) throw new UserException(-5, "No note exist");
	 * 
	 * user.get().getCollaboratedNotes().remove(note);
	 * note.getCollaboratedUser().remove(user.get());
	 * 
	 * userRepository.save(user.get()); notesRepository.save(note);
	 * 
	 * // Response response =
	 * StatusHelper.statusInfo(environment.getProperty("status.collab.remove"),
	 * Integer.parseInt(environment.getProperty("status.success.code"))); // return
	 * response;
	 * 
	 * Response response = ResponseHelper.statusResponse(100,
	 * environment.getProperty("status.note.trashError")); // return response;
	 * return response; }
	 * 
	 */
	
	@Override
	public Response uploadImageToNote(String token , long noteId, MultipartFile imageFile)
	{
		long userId = userToken.decodeToken(token);
		
		Optional<User> user = userRepository.findById(userId);
		
		if(!user.isPresent())
		{
			throw new UserException(-5,"user is not exist");
		}
		
		Note note = noteRepository.findByUserIdAndNoteId(userId, noteId);
		
		if(note == null)
		{
			throw new UserException(-5, "note is not available");
		}
		
		UUID randomUuid = UUID.randomUUID();
		
		String uniqueId = randomUuid.toString();
		try
		{
			Files.copy(imageFile.getInputStream(), fileLocation.resolve(uniqueId), StandardCopyOption.REPLACE_EXISTING);
			note.setNoteImage(uniqueId);
			noteRepository.save(note);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return ResponseHelper.statusResponse(200, "note picture is uploaded");		
	}
}
