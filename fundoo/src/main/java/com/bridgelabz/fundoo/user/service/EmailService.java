package com.bridgelabz.fundoo.user.service;

import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.user.dto.LoginDto;

public interface EmailService {
	Response validateEmail(String email);

	Response setPassword1(LoginDto loginDto, String token);
}
