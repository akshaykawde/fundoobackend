package com.bridgelabz.fundoo.user.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoo.exception.UserException;
import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.response.ResponseToken;
import com.bridgelabz.fundoo.user.dto.ForgetDto;
import com.bridgelabz.fundoo.user.dto.LoginDto;
import com.bridgelabz.fundoo.user.dto.RegistrationDto;
import com.bridgelabz.fundoo.user.repository.UserRepository;
import com.bridgelabz.fundoo.user.service.UserService;
@Valid
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@PostMapping("/register")
	public ResponseEntity<Response> userRegistration(@Valid @RequestBody RegistrationDto userDto) throws Exception {
		Response userResponse = userService.userRegistration(userDto);
		System.out.println(userResponse);
		return new ResponseEntity<>(userResponse, HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<ResponseToken> userLogin(@Valid @RequestBody LoginDto LoginDto)
			throws UserException, UnsupportedEncodingException {
		ResponseToken userResponse = userService.userLogin(LoginDto);
		return new ResponseEntity<>(userResponse, HttpStatus.OK);
	}

	// to verify
	@GetMapping(value = "/{token}")
	public ResponseEntity<Response> userEmailValidation(@PathVariable String token) throws UserException {
		Response userResponse = userService.userEmailValidation(token);
		return new ResponseEntity<Response>(userResponse, HttpStatus.OK);
	}

	@PostMapping("/forgetpassword")
	public ResponseEntity<Response> userForgetPassword(@RequestBody ForgetDto emailDto)
			throws UnsupportedEncodingException, UserException, MessagingException {
		System.out.println(emailDto);
		Response userResponse = userService.userForgetPassword(emailDto);
		return new ResponseEntity<Response>(userResponse, HttpStatus.OK);

	}

	@PutMapping(value = "/resetPassword")
	public ResponseEntity<?> userResetPassword(@RequestParam String token, @RequestParam("password") String password) {
		{
			Response userResponse = userService.userResetPassword(token, password);
			return new ResponseEntity<Response>(userResponse, HttpStatus.OK);

		}

	}

}
