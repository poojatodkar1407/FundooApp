package com.bridgelabz.fundoo.note.service;

import java.util.List;

import com.bridgelabz.fundoo.note.dto.LabelDTO;
import com.bridgelabz.fundoo.note.dto.NoteDTO;
import com.bridgelabz.fundoo.note.model.Label;
import com.bridgelabz.fundoo.response.Response;

public interface LabelService {
	
	public Response createLabel(LabelDTO labeldto , String token);

	public Response updateLabel(LabelDTO labeldto, String token, long labelId);

	public Response deleteLabel(String token, long labeId);

	public List<Label> getAllLabelFromUser(String token);

	public Response removeLabelFromNote(long labelId , String token ,long noteId);

	public Response addLabelToNote(long labelId, String token, long noteId);

	public List<LabelDTO> getLabelsOfNote(String token, long noteId);

	public List<NoteDTO> getNotesOfLabel(String token, long labelId);
}
