package com.bridgelabz.fundoo.notes.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class Note
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long noteId;
	
	private String title;
	private String Discription;
	boolean isTrash;
	boolean isArchive;
	boolean isPin;
	private String setcolour;



	public String getSetcolour() {
		return setcolour;
	}

	public void setSetcolour(String setcolour) {
		this.setcolour = setcolour;
	}

	public boolean isTrash() {
		return isTrash;
	}

	public void setTrash(boolean isTrash) {
		this.isTrash = isTrash;
	}

	public boolean isArchive() {
		return isArchive;
	}

	public void setArchive(boolean isArchive) {
		this.isArchive = isArchive;
	}

	public boolean isPin() {
		return isPin;
	}

	public void setPin(boolean isPin) {
		this.isPin = isPin;
	}

	private long userId;

	public long getNoteId() {
		return noteId;
	}

	public void setNoteId(long noteId) {
		this.noteId = noteId;
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
		this.Discription = discription;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	

}
