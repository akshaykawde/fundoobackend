package com.bridgelabz.fundoo.user.service;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

//	ResponseToken authentication(Optional<User> user, String password)
//			throws UnsupportedEncodingException, UserException;

	Response forgetPassword(ForgotDto emailDto);

	Response resetPassword(String token, String password);

	Response uploadImage(MultipartFile imageFile);

	Resource getUploadedImageOfUser() throws MalformedURLException;

	List<String> showProfilePic();

	ResponseToken authentication(Optional<User> user, String password, String emailId, String token);

}
