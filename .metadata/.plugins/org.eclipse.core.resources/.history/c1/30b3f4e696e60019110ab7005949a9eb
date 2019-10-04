package com.bridgelabz.fundoo.label.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@SuppressWarnings("serial")
@Data
@Entity
@Table
public class LabelModel implements Serializable
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long labelId;
	private String labelName;
	private long userId;
	private LocalDateTime createdDate;
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}
	public long getLabelId() {
		return labelId;
	}
	public void setLabelId(long labelId) {
		this.labelId = labelId;
	}
	public String getLabelName() {
		return labelName;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
}
