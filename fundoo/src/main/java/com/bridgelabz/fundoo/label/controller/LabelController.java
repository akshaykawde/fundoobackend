package com.bridgelabz.fundoo.label.controller;




import java.util.List;

import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoo.label.dto.LabelDto;
import com.bridgelabz.fundoo.label.model.LabelModel;
import com.bridgelabz.fundoo.label.service.LabelService;
import com.bridgelabz.fundoo.notes.controller.NotesController;
import com.bridgelabz.fundoo.notes.model.NotesModel;
import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.utility.Utility;

import ch.qos.logback.classic.Logger;
@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/user/label")
public class LabelController {
	
	@Autowired
	private LabelService labelservice;
	
	@PostMapping("/create")
	ResponseEntity<Response>createLabel(@RequestBody LabelDto labelDto,@RequestHeader String token)	
	{
		Response statusResponse = labelservice.createLabel(labelDto, token);
		return new ResponseEntity<Response>(statusResponse,HttpStatus.OK);
	}
	
	@DeleteMapping("/delete")
	ResponseEntity<Response>deleteLabel(@RequestParam Long labelId,@RequestHeader String token)	
	{
		Response statusResponse = labelservice.deleteLabel(labelId, token);
		return new ResponseEntity<Response>(statusResponse,HttpStatus.OK);
	}
	
	
	@PutMapping("/update")
	ResponseEntity<Response>updateLabel(@RequestBody LabelDto labelDto,@RequestHeader String token,@RequestParam Long labelId)	
	{
		Response statusResponse = labelservice.updateLabel(labelId,labelDto, token);
		return new ResponseEntity<Response>(statusResponse,HttpStatus.OK);
	}
	
	@GetMapping("/AllLabel")
	public List<LabelModel>getAllLabel(@RequestHeader String token)
	{
		List<LabelModel> listLabel=labelservice.getAllLabel(token);
		return listLabel;
	}
	
	
}
