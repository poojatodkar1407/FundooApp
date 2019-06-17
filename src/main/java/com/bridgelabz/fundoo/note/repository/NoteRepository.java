package com.bridgelabz.fundoo.note.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoo.note.model.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

	//public Note findByNoteId(long noteId);

	public Note findByUserIdAndNoteId(long userId, long noteId);
	public Note findByTitleAndDescription(String Title,String Description);
	

}
