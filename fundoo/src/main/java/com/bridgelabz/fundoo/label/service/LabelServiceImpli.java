
package com.bridgelabz.fundoo.label.service;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.bridgelabz.fundoo.exception.UserException;
import com.bridgelabz.fundoo.label.dto.LabelDto;
import com.bridgelabz.fundoo.label.model.LabelModel;
import com.bridgelabz.fundoo.label.repository.LabelRepo;
import com.bridgelabz.fundoo.notes.dto.NotesDto;
import com.bridgelabz.fundoo.notes.model.NotesModel;
import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.user.model.UserModel;
import com.bridgelabz.fundoo.user.repository.UserRepository;
import com.bridgelabz.fundoo.utility.ResponseHelper;
import com.bridgelabz.fundoo.utility.TokenGenerator;
import com.bridgelabz.fundoo.utility.Utility;

@Service("labelservice")
public class LabelServiceImpli implements LabelService {
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private LabelRepo labelRepository;

	@Autowired
	private UserRepository UserRepository;

	@Autowired
	private TokenGenerator tokenUtil;

	@Override
	public Response createLabel(LabelDto labelDto, String token) {
		long userId = tokenUtil.decodeToken(token);
		Optional<UserModel> userModel = UserRepository.findById(userId);
		if (!userModel.isPresent()) {
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
		userModel.get().getLabel().add(label);
		UserRepository.save(userModel.get());
		Response response = ResponseHelper.statusResponse(200, "label created");
		return response;
	}

	@Override
	public Response deleteLabel(long labelId, String token) {
		long userId = tokenUtil.decodeToken(token);
		Optional<UserModel> userModel = UserRepository.findById(userId);
		System.out.println("user info----------->"+userModel);
		if (!userModel.isPresent()) {
			throw new UserException("invalid input");
		}
		LabelModel label = labelRepository.findByLabelIdAndUserId(labelId, userId);
		System.out.println("label info----->"+label);
		if (label == null)
//		Optional<LabelModel> labelAvaliabel = labelRepository.findByUserIdAndLabelName(userId, labelDto.getLabelName());
		{
			
			throw new UserException("invalid input");
		}

		else {
			UserRepository.save(userModel.get());
			Response response = ResponseHelper.statusResponse(200, "label deleted");
			System.out.println("response--->"+response);
			return response;
		}
	}

	@Override
	public List<LabelModel> getAllLabel(String token) {
		long userId = tokenUtil.decodeToken(token);
		UserModel userModel = UserRepository.findById(userId).get();
		return (List<LabelModel>) userModel.getLabel();
	}

	@Override
	public Response updateLabel(Long labelId, LabelDto labelDto, String token) {
		long userId = tokenUtil.decodeToken(token);
		Optional<UserModel> userModel = UserRepository.findById(userId);
		if (!userModel.isPresent()) {
			throw new UserException("invalid input");
		}
		LabelModel label = labelRepository.findByLabelIdAndUserId(labelId, userId);
		if (label == null) {
			throw new UserException("label not exists");
		}
		if (labelDto.getLabelName().isEmpty()) {
			throw new UserException("label has no name");
		}
		Optional<LabelModel> labelAvaliabel = labelRepository.findByUserIdAndLabelName(userId, labelDto.getLabelName());
		if (labelAvaliabel.isPresent()) {
			throw new UserException("label alredy exist");
		}
		label.setLabelName(labelDto.getLabelName());
		labelRepository.save(label);
		Response response = ResponseHelper.statusResponse(200, "label updated");
		return response;
	}
}
