package com.bridgelabz.fundoo.label.service;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.bridgelabz.fundoo.exception.UserException;
import com.bridgelabz.fundoo.label.dto.Labeldto;
import com.bridgelabz.fundoo.label.model.LabelModel;
import com.bridgelabz.fundoo.label.repository.LabelRepo;
import com.bridgelabz.fundoo.notes.dto.NotesDto;
import com.bridgelabz.fundoo.notes.model.Note;
import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.user.model.User;
import com.bridgelabz.fundoo.user.repository.UserRepo;
import com.bridgelabz.fundoo.utility.ResponseHelper;
import com.bridgelabz.fundoo.utility.TokenGenerator;
import com.bridgelabz.fundoo.utility.Utility;

@Service("labelservice")
public class LabelServiceImpletation implements LabelService {
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private LabelRepo labelRepository;

	@Autowired
	private UserRepo UserRepository;

	@Autowired
	private TokenGenerator tokenUtil;

	@Override
	public Response createLabel(Labeldto labelDto, String token) {
		long userId = tokenUtil.decodeToken(token);
		Optional<User> user = UserRepository.findById(userId);
		if (!user.isPresent()) {
			throw new UserException("Invalid input");
		}
		if (labelDto.getLabelName().isEmpty()) {
			throw new UserException("label do not having name");
		}
		String labelName = labelDto.getLabelName();
		Optional<LabelModel> labelAvailability = labelRepository.findByUserIdAndLabelName(userId, labelName);
		if (labelAvailability.isPresent()) {
			throw new UserException("label is exist");
		}
		LabelModel label = modelMapper.map(labelDto, LabelModel.class);
		label.setUserId(userId);
		label.setCreatedDate(LocalDateTime.now());
		// labelRepository.save(label);
		user.get().getLabel().add(label);
		UserRepository.save(user.get());
		Response response = ResponseHelper.statusResponse(200, "label created");
		return response;
	}

	@Override
	public Response deleteLabel(long labelId, String token) {
		long userId = tokenUtil.decodeToken(token);
		Optional<User> user = UserRepository.findById(userId);
		if (!user.isPresent()) {
			throw new UserException("invalid input");
		}
		LabelModel label = labelRepository.findByLabelIdAndUserId(labelId, userId);
		if (label == null) {
			throw new UserException("invalid input");
		}

		labelRepository.delete(label);
		Response response = ResponseHelper.statusResponse(200, "label deleted");
		return response;
	}

	@Override
	public List<LabelModel> getAllLabel(String token) {
		long userId = tokenUtil.decodeToken(token);
		User user = UserRepository.findById(userId).get();
		return (List<LabelModel>) user.getLabel();
	}

	@Override
	public Response updateLabel(Labeldto labelDto, String token) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public Response updateLabel(Long labelId, Labeldto labelDto, String token)
//	{
//		long UserId=tokenUtil.decodeToken(token);
//		Optional<User> user=UserRepository.findById(UserId);
//		if (!user.isPresent())
//		{
//			throw new UserException("invalid input");
//		}
//		LabelModel label=labelRepository.findByLabelIdAndUserId(labelId, UserId);
//		if(label==null)
//		{
//			throw new UserException("label not exists");
//		}
//		if(labelDto.getLabelName().isEmpty())
//		{
//			throw new UserException("label has no name");
//		}
//		Optinal
//		UserRepository.save(label);
//		Response response=ResponseHelper.statusResponse(200, "label updated");
//		return response;
	}

	


