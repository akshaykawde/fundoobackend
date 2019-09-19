package com.bridgelabz.fundoo.user.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.bridgelabz.fundoo.label.model.LabelModel;
import com.bridgelabz.fundoo.notes.model.NotesModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
@Entity
@Table
public class UserModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long userId;
	private String firstName;
	private String lastName;
	private String emailId;
	private String password;
	private String mobileNum;
	private boolean isVerify;

	public UserModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserModel(long userId, String firstName, String lastName, String emailId, String password, String mobileNum,
			boolean isVerify, List<NotesModel> notesModels) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.password = password;
		this.mobileNum = mobileNum;
		this.isVerify = isVerify;

		this.notesModels = notesModels;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", emailId=" + emailId
				+ ", password=" + password + ", mobileNum=" + mobileNum + ", isVerify=" + isVerify + ", notes=" + notesModels
				+ "]";
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public boolean isVerify() {
		return isVerify;
	}

	public void setVerify(boolean isVerify) {
		this.isVerify = isVerify;
	}

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	private List<NotesModel> notesModels;

	public List<NotesModel> getNotes() {
		return notesModels;
	}

	public void setNotes(List<NotesModel> notesModels) {
		this.notesModels = notesModels;
	}

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	private List<LabelModel> label;

	public List<LabelModel> getLabel() {
		return label;
	}

	public void setLabel(List<LabelModel> label) {
		this.label = label;
	}

	

}
