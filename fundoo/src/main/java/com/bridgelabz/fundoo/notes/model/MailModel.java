package com.bridgelabz.fundoo.notes.model;

import org.springframework.stereotype.Component;


public class MailModel {
	private String to;
	private String text;
	private String subject;
	private String content;

	
	public MailModel() {
		super();
	}

	public MailModel(String to, String text, String subject, String content) {
		super();
		this.to = to;
		this.text = text;
		this.subject = subject;
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Override
	public String toString() {
		return "MailModel [to=" + to + ", text=" + text + ", subject=" + subject + ", content=" + content + "]";
	}

}
