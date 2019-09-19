package com.bridgelabz.fundoo.user.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ForgetDto 
{	
	@NotNull
	private String emailId;

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
}
