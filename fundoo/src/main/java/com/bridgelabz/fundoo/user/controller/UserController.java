package com.bridgelabz.fundoo.user.controller;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
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
import com.bridgelabz.fundoo.user.dto.ForgotDto;
import com.bridgelabz.fundoo.user.dto.LoginDTO;
import com.bridgelabz.fundoo.user.dto.RegisterDto;
import com.bridgelabz.fundoo.user.repository.UserRepo;
import com.bridgelabz.fundoo.user.service.UserService;

@CrossOrigin(allowedHeaders = "*", origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;

	@Autowired
	UserRepo userRepo;

	@PostMapping("/register")
	public ResponseEntity<Response> register(@RequestBody RegisterDto userDto)
			throws UserException, UnsupportedEncodingException {
		Response response = userService.onRegister(userDto);
		System.out.println(response);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	@PostMapping("/login")
	public ResponseEntity<ResponseToken> onLogin(@RequestBody LoginDTO loginDTO)
			throws UserException, UnsupportedEncodingException {
		ResponseToken response = userService.onLogin(loginDTO);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// to verify
	@GetMapping(value = "/{token}")
	public ResponseEntity<Response> emailValidation(@PathVariable String token) throws UserException {
		Response response = userService.validateEmailId(token);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@PostMapping("/forgotpassword")
	public ResponseEntity<Response> forgotPassword(@RequestBody ForgotDto emailDto)
			throws UnsupportedEncodingException, UserException, MessagingException {
		System.out.println(emailDto);
		Response status = userService.forgetPassword(emailDto);
		return new ResponseEntity<Response>(status, HttpStatus.OK);
		

	}

	@PutMapping(value = "/resetpassword")
	public ResponseEntity<?> resetpassword(@RequestParam String token, @RequestParam("password") String password) {
		{
			Response response = userService.resetPassword(token, password);
			return new ResponseEntity<Response>(response, HttpStatus.OK);

		}

	}

}
