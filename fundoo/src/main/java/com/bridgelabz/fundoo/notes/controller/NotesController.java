package com.bridgelabz.fundoo.notes.controller;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
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

import com.bridgelabz.fundoo.notes.model.Note;
import com.bridgelabz.fundoo.notes.service.NoteService;
import com.bridgelabz.fundoo.response.Response;
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RestController
@RequestMapping("/notes")
public class NotesController {
	Logger logger = LoggerFactory.getLogger(NotesController.class);
	@Autowired
	private NoteService noteService;
	
	@PostMapping("/create")
	public ResponseEntity<Response> createNote(@RequestBody NotesDto notesDto, @RequestHeader String token) {
		logger.info(notesDto.toString());
		Response responseStatus = noteService.onCrete(notesDto, token);
		return new ResponseEntity<Response>(responseStatus, HttpStatus.OK);
	}
	@PutMapping("/update")
	public ResponseEntity<Response> updateNote(@RequestBody NotesDto notesDto, String token,
			@RequestParam Long noteId) {
		logger.info(notesDto.toString());
		Response responseStatus = noteService.update(notesDto, token, noteId);
		return new ResponseEntity<Response>(responseStatus, HttpStatus.OK);
	}
		
	@PutMapping("/permanetdelete")
	public ResponseEntity<Response>permanentdeletNote(@RequestHeader String token, @RequestParam long noteId)
	{
		Response responseStatus=noteService.permanentdelete(token, noteId);
		return new ResponseEntity<Response>(responseStatus,HttpStatus.OK);
	}

//	@PutMapping("/Untrash")
//	public ResponseEntity<Response>untrash(@RequestHeader String token,@RequestParam long noteId)
//	{
//		Response responseStatus=noteService.untrash(token, noteId);
//		return new ResponseEntity<Response>(responseStatus,HttpStatus.OK);
//	}
// 	@PutMapping("/trash")
// 	public ResponseEntity<Response> trash(@RequestHeader String token, @RequestParam long noteId) {
// 		Response responseStatus = noteService.trash(token, noteId);
// 		return new ResponseEntity<Response>(responseStatus, HttpStatus.OK);
// 	}

	@PutMapping("/pin")
	public ResponseEntity<Response> pin(@RequestHeader String token, @RequestParam long noteId) {
		Response responseStatus = noteService.pin(token, noteId);
		return new ResponseEntity<Response>(responseStatus, HttpStatus.OK);
	}

//	@PutMapping("/unpin")
//	public ResponseEntity<Response>unpin(@RequestHeader String token,@RequestParam long noteId)
//	{
//		Response responseStatus=noteService.unpin(token, noteId);
//		return new ResponseEntity<Response>(responseStatus,HttpStatus.OK);	
//	}
//	
	@PutMapping("/archive")
	public ResponseEntity<Response> archive(@RequestHeader String token, @RequestParam long noteId) {
		Response responseStatus = noteService.archive(token, noteId);
		return new ResponseEntity<Response>(responseStatus, HttpStatus.OK);
	}
	@GetMapping("/getAllNotes")
	public List<Note> getAllNotes(@RequestHeader String token) {
		List<Note>listNotes=noteService.getAllNotes(token);
		return listNotes;
	}
	
	@GetMapping("/getAllArchive")
	public List<NotesDto> getPinnedNotes(@RequestHeader String token) {
		List<NotesDto>listNotes=noteService.getAllArchive(token);
		return listNotes;
	}
	@GetMapping("/getAllTrash")
	public List<NotesDto> getAllTrash(@RequestHeader String token)
	{
		List<NotesDto>listNotes=noteService.getAllTrash(token);
		return listNotes;
	}
	
	@GetMapping("/getAllPinedNotes")
	public List<NotesDto> getAllPinedNotes(@RequestHeader String token)
	{
		List<NotesDto>listNotes=noteService.getAllPinedNotes(token);
		return listNotes;
	}
	
	
	
		
//	@GetMapping("/getArchieve")
//	public List<Note> getArchieveNotes(@RequestHeader String token) {
//		List<Note> listnotes = noteService.getArchievedNote(token);
//		return listnotes;
//	}
//	@PutMapping("/unarchive")
//	public ResponseEntity<Response>unarchive(@RequestHeader String token,@RequestParam long noteId)
//	{
//		Response responseStatus=noteService.unarchive(token, noteId);
//		return new ResponseEntity<Response>(responseStatus,HttpStatus.OK);	
//	}
//	@PutMapping("/colour")
//	public ResponseEntity<Response>noteColour(@RequestHeader String token,@RequestParam long noteId)
//	{
//		Response responseStatus = noteService.setColour(token,noteId);
//		System.out.println("response--->"+responseStatus);
//		return new ResponseEntity<Response>(responseStatus, HttpStatus.OK);			
//	}
	
	@PutMapping("/color")
	public ResponseEntity<Response>noteColor(@RequestHeader String token,@RequestParam long noteId, String color )
	{
		Response responseStatus =noteService.setColor(token,noteId,color);
		return new ResponseEntity<Response>(responseStatus, HttpStatus.OK);
	}
	
	
}

