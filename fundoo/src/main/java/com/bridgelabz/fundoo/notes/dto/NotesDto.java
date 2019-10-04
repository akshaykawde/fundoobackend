package com.bridgelabz.fundoo.notes.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class NotesDto {
	@NotNull
	private String title;
	@NotNull
	private String Discription;
	
	private String setcolour;
	
	
	public String getSetcolour() {
		return setcolour;
	}
	public void setSetcolour(String setcolour) {
		this.setcolour = setcolour;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDiscription() {
		return Discription;
	}
	public void setDiscription(String discription) {
		Discription = discription;
	}
	@Override
	public String toString() {
		return "NotesDto [title=" + title + ", Discription=" + Discription + ", setcolour="
				+ setcolour + "]";
	}
	
	
}
	
