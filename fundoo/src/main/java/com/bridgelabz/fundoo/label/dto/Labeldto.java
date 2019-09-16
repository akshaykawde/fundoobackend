package com.bridgelabz.fundoo.label.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Labeldto {
private String labelName;


public String getLabelName() {
	return labelName;
}

public void setLabelName(String labelName) {
	this.labelName = labelName;
}

@Override
public String toString() {
	return "Labeldto [labelName=" + labelName + "]";
}






}