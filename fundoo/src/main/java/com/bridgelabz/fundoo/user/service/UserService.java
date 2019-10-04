package com.bridgelabz.fundoo.user.service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.bridgelabz.fundoo.exception.UserException;
import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.response.ResponseToken;
import com.bridgelabz.fundoo.user.dto.ForgotDto;
import com.bridgelabz.fundoo.user.dto.LoginDTO;
import com.bridgelabz.fundoo.user.dto.RegisterDto;
import com.bridgelabz.fundoo.user.model.User;

@Service
public interface UserService {
	Response onRegister(RegisterDto userDto) throws UserException, UnsupportedEncodingException;

	ResponseToken onLogin(LoginDTO loginDto) throws UserException, UnsupportedEncodingException;

	Response validateEmailId(String token) throws UserException;

	ResponseToken authentication(Optional<User> user, String password)
			throws UnsupportedEncodingException, UserException;

	Response forgetPassword(ForgotDto emailDto);

	Response resetPassword(String token, String password);

}
