package com.bridgelabz.fundoo.label.repository;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.bridgelabz.fundoo.label.dto.Labeldto;
import com.bridgelabz.fundoo.label.model.LabelModel;


public interface LabelRepo  extends JpaRepository<LabelModel, Long>
{
	
	Optional<LabelModel> findByUserIdAndLabelName(long userId, String labelName);


	LabelModel findByLabelIdAndUserId(long labelId, long userId);

	public List<LabelModel>findByUserId(long userId);
	
}
