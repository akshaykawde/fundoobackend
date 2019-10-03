package com.bridgelabz.fundoo.notes.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoo.notes.dto.NotesDto;

import com.bridgelabz.fundoo.notes.model.NotesModel;
import com.bridgelabz.fundoo.notes.service.NoteService;
import com.bridgelabz.fundoo.response.Response;

@CrossOrigin(allowedHeaders = "*", origins = "*")
@RestController
@RequestMapping("/notes")
public class NotesController {

	@Autowired
	private NoteService noteService;

	@Autowired
	private com.bridgelabz.fundoo.elasticSearch.elasticSearch elasticSearch;

	@PostMapping("/create")
	public ResponseEntity<Response> createNote(@RequestBody NotesDto notesDto, @RequestHeader String token) {
		Response responseStatus = noteService.createNote(notesDto, token);
		return new ResponseEntity<Response>(responseStatus, HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<Response> updateNote(@RequestBody NotesDto notesDto, String token,
			@RequestParam Long noteId) {
		Response responseStatus = noteService.updateNote(notesDto, token, noteId);
		return new ResponseEntity<Response>(responseStatus, HttpStatus.OK);
	}

	@PutMapping("/permanetdelete")
	public ResponseEntity<Response> permanentdeletNote(@RequestHeader String token, @RequestParam long noteId) {
		Response responseStatus = noteService.permanentDelete(token, noteId);
		return new ResponseEntity<Response>(responseStatus, HttpStatus.OK);
	}

	@PutMapping("/trash")
	public ResponseEntity<Response> noteIntrash(@RequestHeader String token, @RequestParam long noteId) {
		Response responseStatus = noteService.noteIntrash(token, noteId);
		return new ResponseEntity<Response>(responseStatus, HttpStatus.OK);
	}

	@PutMapping("/pin")
	public ResponseEntity<Response> noteIsPin(@RequestHeader String token, @RequestParam long noteId) {
		Response responseStatus = noteService.noteIsPin(token, noteId);
		return new ResponseEntity<Response>(responseStatus, HttpStatus.OK);
	}

	@PutMapping("/archive")
	public ResponseEntity<Response> noteArchive(@RequestHeader String token, @RequestParam long noteId) {
		Response responseStatus = noteService.noteArchive(token, noteId);
		return new ResponseEntity<Response>(responseStatus, HttpStatus.OK);
	}

	@GetMapping("/getAllNotes")
	public List<NotesModel> getAllNotes(@RequestHeader String token) {
		List<NotesModel> listNotes = noteService.getAllNotes(token);
		return listNotes;
	}

	@GetMapping("/getAllArchive")
	public List<NotesDto> getPinnedNotes(@RequestHeader String token) {
		List<NotesDto> listNotes = noteService.getAllArchive(token);
		return listNotes;
	}

	@GetMapping("/getAllTrash")
	public List<NotesDto> getAllTrash(@RequestHeader String token) {
		List<NotesDto> listNotes = noteService.getAllTrash(token);
		return listNotes;
	}

	@GetMapping("/getAllPinedNotes")
	public List<NotesDto> getAllPinedNotes(@RequestHeader String token) {
		List<NotesDto> listNotes = noteService.getAllPinedNotes(token);
		return listNotes;
	}

	@PutMapping("/color")
	public ResponseEntity<Response> noteColor(@RequestHeader String token, @RequestParam long noteId, String color) {
		Response responseStatus = noteService.setColor(token, noteId, color);
		return new ResponseEntity<Response>(responseStatus, HttpStatus.OK);
	}

	@GetMapping("/searchNote")
	public List<NotesModel> elasticSearch(@RequestHeader String token, @RequestParam String query) {
		List<NotesModel> response = elasticSearch.searchData(query, token);
		return response;
	}

	@PutMapping("/addCollaborator")
	public ResponseEntity<Response> addCollaborator(@RequestHeader String token, @RequestParam String emailId,
			@RequestParam long noteId) {
		Response responseStatus = noteService.addCollaborator(token, noteId, emailId);
		return new ResponseEntity<Response>(responseStatus, HttpStatus.OK);
	}

	@PutMapping("/deleteCollaborator")
	public ResponseEntity<Response> deleteCollaborator(@RequestHeader String token, @RequestParam long noteId,String emailId) {
		Response responseStatus = noteService.deleteCollaborator(token, noteId,emailId);
		return new ResponseEntity<Response>(responseStatus, HttpStatus.OK);
	}
}
