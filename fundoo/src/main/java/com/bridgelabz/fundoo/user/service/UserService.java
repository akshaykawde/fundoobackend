package com.bridgelabz.fundoo.user.service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.bridgelabz.fundoo.exception.UserException;
import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.response.ResponseToken;
import com.bridgelabz.fundoo.user.dto.ForgetDto;
import com.bridgelabz.fundoo.user.dto.LoginDto;
import com.bridgelabz.fundoo.user.dto.RegistrationDto;
import com.bridgelabz.fundoo.user.model.UserModel;

@Service
public interface UserService {
	Response userRegistration(RegistrationDto userDto) throws UserException, UnsupportedEncodingException;

	ResponseToken userLogin(LoginDto LoginDto) throws UserException, UnsupportedEncodingException;

	Response userEmailValidation(String token) throws UserException;

	ResponseToken authentication(Optional<UserModel> userModel, String password)
			throws UnsupportedEncodingException, UserException;

	Response userForgetPassword(ForgetDto emailDto);

	Response userResetPassword(String token, String password);

}
