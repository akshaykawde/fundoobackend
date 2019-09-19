package com.bridgelabz.fundoo.user.service;

import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.bridgelabz.fundoo.exception.UserException;
import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.response.ResponseToken;
import com.bridgelabz.fundoo.user.dto.ForgetDto;
import com.bridgelabz.fundoo.user.dto.LoginDto;
import com.bridgelabz.fundoo.user.dto.RegistrationDto;
import com.bridgelabz.fundoo.user.model.UserModel;
import com.bridgelabz.fundoo.user.repository.UserRepository;
import com.bridgelabz.fundoo.utility.ResponseHelper;
import com.bridgelabz.fundoo.utility.TokenGenerator;
import com.bridgelabz.fundoo.utility.Utility;

@Service
public class UserServiceImpi implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TokenGenerator tokenUtil;

	@Autowired
	private Response statusResponse;

	@Autowired
	private Utility utility;

	@Override
	public Response userRegistration(RegistrationDto userDto) {

		String emailId = userDto.getEmailId();
		UserModel modelInstance = modelMapper.map(userDto, UserModel.class);
		System.out.println("user email is--->" + modelInstance.getEmailId());
		Optional<UserModel> userAlreadyPresent = userRepository.findByEmailId(modelInstance.getEmailId());
		if (userAlreadyPresent.isPresent()) {
			throw new UserException("emailExistError");
		}
		String password = passwordEncoder.encode(userDto.getPassword());
		modelInstance.setPassword(password);
		modelInstance = userRepository.save(modelInstance);
		Long userId = modelInstance.getUserId();
		utility.send(emailId, "confirmation mail", utility.getUrl(userId));
		statusResponse = ResponseHelper.statusResponse(200, "register successfully");
		return statusResponse;

	}

	public ResponseToken userLogin(LoginDto LoginDto) {
		System.out.println("Enter in the login");
		Optional<UserModel> user = userRepository.findByEmailId(LoginDto.getEmailId());
		System.out.println("user----->"+user.get());
		ResponseToken response = new ResponseToken();
		if (user.isPresent()) {
			System.out.println("password..." + (LoginDto.getPassword()));
			return authentication(user, LoginDto.getPassword());
		} else {

			return response;
		}
	}

	@Override
	public Response userForgetPassword(ForgetDto emailDto) {
		String emailId = emailDto.getEmailId();
		Optional<UserModel> userAlredyPresent = userRepository.findByEmailId(emailDto.getEmailId());
		if (!userAlredyPresent.isPresent()) {
			throw new UserException("user not found");
		}
		ResponseToken response = new ResponseToken();
		long id = userAlredyPresent.get().getUserId();
		utility.send(emailId, "confirmation mail", "http://localhost:4200/resetpassword");

		return ResponseHelper.statusResponse(200, "check your Email");

	}

	@Override
	public Response userResetPassword(String token, String password) {
		long id = tokenUtil.decodeToken(token);
		UserModel modelInstance = userRepository.findById(id).orElseThrow(() -> new UserException(404, "data not found"));
		String encodedPassword = passwordEncoder.encode(password);
		modelInstance.setPassword(encodedPassword);
		modelInstance = userRepository.save(modelInstance);
		return ResponseHelper.statusResponse(200, "password sucessfully reset");

	}

	@Override
	public ResponseToken authentication(Optional<UserModel> user, String password) {
		ResponseToken response = new ResponseToken();
		if (user.get().isVerify()) {
			boolean status = passwordEncoder.matches(password, user.get().getPassword());
			if (status == true) {
				System.out.println("logged in");
				String token = tokenUtil.createToken(user.get().getUserId());
				System.out.println(token);
				response.setToken(token);
				response.setStatusCode(200);
				response.setStatusMessage("user.login");
				return response;
			}
			throw new UserException("user.login.password");
		}
		throw new UserException("user.login.verification");
	}

	@Override
	public Response userEmailValidation(String token) {
		Long id = tokenUtil.decodeToken(token);
		UserModel user = userRepository.findById(id).orElseThrow(() -> new UserException("user.validation.email"));
		user.setVerify(true);
		userRepository.save(user);
		statusResponse = ResponseHelper.statusResponse(200, ("user.validation"));
		return statusResponse;
	}

}
