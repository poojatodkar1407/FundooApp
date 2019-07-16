package com.bridgelabz.fundoo.note.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoo.note.model.Label;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
	
	public Label findByLabelIdAndUserId(long lableId , long userId);
	public Optional<Label> findByUserIdAndLabelName(long userId, String labelName);
	public List<Label> findByUserId(long userId);
	//public Label deleteLabelByLabelIdAndNoteId(long labelId,long noteId);
	//public List<Label> findAllById(long userId);
}
	