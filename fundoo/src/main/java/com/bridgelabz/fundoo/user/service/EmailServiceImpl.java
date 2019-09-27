package com.bridgelabz.fundoo.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import com.bridgelabz.fundoo.user.dto.LoginDto;
import com.bridgelabz.fundoo.exception.UserException;
import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.user.model.MailModel;
import com.bridgelabz.fundoo.user.model.UserModel;
import com.bridgelabz.fundoo.user.repository.UserRepository;
import com.bridgelabz.fundoo.utility.RabbitMqImpli;
import com.bridgelabz.fundoo.utility.ResponseHelper;
import com.bridgelabz.fundoo.utility.TokenGenerator;

public class EmailServiceImpl implements EmailService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private Environment environment;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TokenGenerator tokenHelper;

	@Autowired
	private MailModel mailModel;

	@Autowired
	private RabbitMqImpli rabbitMq;

	@Override
	public Response validateEmail(String email) {
		Optional<UserModel> user = userRepository.findByEmailId(email);
		if (!user.isPresent()) {
			throw new UserException(environment.getProperty("status.email.invalidMail"));
		} else {
			String token = tokenHelper.createToken(user.get().getUserId());
			String body = "http://localhost:8080/" + token + "/SetPassword";
			mailModel.setTo(email);
			mailModel.setFrom("kawde4@gmail.com");
			mailModel.setSubject("Reset password");
			mailModel.setBody(body);
			rabbitMq.sendMessageToQueue(mailModel);
			rabbitMq.send(mailModel);
			return ResponseHelper.statusResponse(200, "Mail Sent Succesfully");
		}
	}

	@Override
	public Response setPassword1(LoginDto loginDto, String token) 
	{
		long id = tokenHelper.decodeToken(token);
		Optional<UserModel> user = userRepository.findByuserId(id);
		String password = passwordEncoder.encode(loginDto.getPassword());
		user.get().setPassword(password);
		userRepository.save(user.get());
		return ResponseHelper.statusResponse(200, "Password Reset successfully");
	}

}
