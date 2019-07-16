package com.bridgelabz.fundoo.note.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bridgelabz.fundoo.exception.UserException;
import com.bridgelabz.fundoo.note.dto.LabelDTO;
import com.bridgelabz.fundoo.note.dto.NoteDTO;
import com.bridgelabz.fundoo.note.model.Label;
import com.bridgelabz.fundoo.note.model.Note;
import com.bridgelabz.fundoo.note.repository.LabelRepository;
import com.bridgelabz.fundoo.note.repository.NoteRepository;
import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.user.model.User;
import com.bridgelabz.fundoo.user.repository.UserRepository;
import com.bridgelabz.fundoo.utility.ResponseHelper;
import com.bridgelabz.fundoo.utility.TokenUtil;

@Service("LableService")
@PropertySource("classpath:message.properties")
@Transactional(propagation = Propagation.SUPPORTS,readOnly = false)
public class LabelServiceImpl implements LabelService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Environment enviornment;

	@Autowired
	private TokenUtil userToken;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LabelRepository labelRepository;

	@Autowired
	private NoteRepository noteRepository;

	public Response createLabel(LabelDTO labeldto, String token) {

		long userId = userToken.decodeToken(token);
		Optional<User> user = userRepository.findById(userId);

		if (!user.isPresent()) {
			throw new UserException(100, "invalid user");
		}

		Optional<Label> labelPresent = labelRepository.findByUserIdAndLabelName(userId, labeldto.getLabelName());

		if (labelPresent.isPresent()) {
			throw new UserException(101, "label already exist");
		}

		Label label = modelMapper.map(labeldto, Label.class);

		label.setLabelName(labeldto.getLabelName());
		label.setUserId(userId);
		label.setCreated(LocalDateTime.now());
		label.setModified(LocalDateTime.now());

		user.get().getLabel().add(label);

		labelRepository.save(label);
		userRepository.save(user.get());

		Response response = ResponseHelper.statusResponse(200, enviornment.getProperty("status.label.created"));
		System.out.println("back to the service");
		return response;
	}

	@Override
	public Response updateLabel(LabelDTO labeldto, String token, long labelId) {

		long userId = userToken.decodeToken(token);
		Optional<User> user = userRepository.findById(userId);

		if (!user.isPresent()) {
			throw new UserException(100, "invalid user");
		}

		Label label = labelRepository.findByLabelIdAndUserId(labelId, userId);
		if (label == null) {
			throw new UserException(-6, "No label exist");
		}

		Optional<Label> labelPresent = labelRepository.findByUserIdAndLabelName(userId, labeldto.getLabelName());

		if (labelPresent.isPresent()) {
			throw new UserException(101, "label already exist");
		}

		label.setLabelName(labeldto.getLabelName());
		label.setModified(LocalDateTime.now());
		labelRepository.save(label);

		Response response = ResponseHelper.statusResponse(100, enviornment.getProperty("status.label.updated"));
		return response;

	}

	@Override
	public Response deleteLabel(String token, long labelId) {
		long userId = userToken.decodeToken(token);
		Optional<User> user = userRepository.findById(userId);

		if (!user.isPresent()) {
			throw new UserException(100, "Invalid user");
		}

		Label label = labelRepository.findByLabelIdAndUserId(labelId, userId);
		if (label == null) {
			throw new UserException(101, "label not exist");
		}
		labelRepository.delete(label);

		Response response = ResponseHelper.statusResponse(100, enviornment.getProperty("status.label.deleted"));
		return response;
	}

	public List<Label> getAllLabelFromUser(String token) {
		long userId = userToken.decodeToken(token);
		Optional<User> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			throw new UserException("Invalid input");
		}

		List<Label> labels = labelRepository.findByUserId(userId);
//		List<Label> labelList = labelRepository.findAllById(userId);
//		for(Label labels1 : labelList)
//		{
//			System.out.println(labels1);
//		}
		List<Label> listLabel = new ArrayList<>();
		for (Label noteLabel : labels) {
			Label labelDto = modelMapper.map(noteLabel, Label.class);
			listLabel.add(labelDto);
		}
		return listLabel;
	}

	@Override
	public Response addLabelToNote(long labelId, String token, long noteId) {
		long userId = userToken.decodeToken(token);
		Optional<User> user = userRepository.findById(userId);

		if (user == null) {
			throw new UserException(-6, "invalid input");
		}

		Note note = noteRepository.findByUserIdAndNoteId(userId, noteId);
		if (note == null) {
			throw new UserException(-6, "invalid note");
		}

		Label label = labelRepository.findByLabelIdAndUserId(labelId, userId);
		if (label == null) {
			throw new UserException(-6, "invalid label");
		}

		note.getListLabel().add(label);
		note.setModified(LocalDateTime.now());
		label.getNotes().add(note);
		label.setModified(LocalDateTime.now());
		noteRepository.save(note);
		//labelRepository.save(label);

		Response response = ResponseHelper.statusResponse(200, enviornment.getProperty("status.label.addtonote"));
		return response;
	}

	public List<LabelDTO> getLabelsOfNote(String token, long noteId) {
		long userId = userToken.decodeToken(token);
		Optional<User> user = userRepository.findById(userId);
		if (!user.isPresent()) {
			throw new UserException(-6, "User does not exist");
		}
		Optional<Note> note = noteRepository.findById(noteId);
		if (!note.isPresent()) {
			throw new UserException(-6, "Note does not exist");
		}
		List<Label> label = note.get().getListLabel();
		System.out.println(label.toString());
		List<LabelDTO> listLabel = new ArrayList<>();
		for (Label noteLabel : label) {
			LabelDTO labelDto = modelMapper.map(noteLabel, LabelDTO.class);
			listLabel.add(labelDto);
			System.out.println(labelDto.getLabelName());
		}
		return listLabel;

	}

	@Override
	public Response removeLabelFromNote(long labelId, String token, long noteId) {

		long userId = userToken.decodeToken(token);
		Optional<User> user = userRepository.findById(userId);
		System.out.println("user is" + user);
		if (user == null) {
			throw new UserException(-6, "invalid input");
		}

		Label label = labelRepository.findByLabelIdAndUserId(labelId, userId);
		System.out.println("label is" + label);
		if (label == null) {
			throw new UserException(-6, "invalid label");
		}

		Note note = noteRepository.findByUserIdAndNoteId(userId, noteId);
		System.out.println("note is" + note);
		if (note == null) {
			throw new UserException(-6, "invalid label");
		}

		label.setModified(LocalDateTime.now());
		labelRepository.delete(label);
		note.setModified(LocalDateTime.now());
		noteRepository.save(note);
		Response response = ResponseHelper.statusResponse(100, enviornment.getProperty("status.label.removed"));
		return response;
	}

	@Override
	public List<NoteDTO> getNotesOfLabel(String token, long labelId) {
		long userId = userToken.decodeToken(token);
		Optional<User> user = userRepository.findById(userId);

		if (!user.isPresent()) {
			throw new UserException(-6, "invalid user");
		}

		Label label = labelRepository.findByLabelIdAndUserId(labelId, userId);
		if (label == null) {
			throw new UserException(-6, "invalid label");
		}

		List<Note> notes = label.getNotes();
		List<NoteDTO> noteList = new ArrayList<>();

		for (Note note : notes) {
			NoteDTO noteDto = modelMapper.map(note, NoteDTO.class);
			noteList.add(noteDto);

		}
		return noteList;
	}

}
