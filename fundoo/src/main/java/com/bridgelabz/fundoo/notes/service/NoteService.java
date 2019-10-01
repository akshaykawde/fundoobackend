package com.bridgelabz.fundoo.notes.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.bridgelabz.fundoo.notes.dto.NotesDto;

import com.bridgelabz.fundoo.notes.model.NotesModel;
import com.bridgelabz.fundoo.response.Response;

public interface NoteService {

	Response updateNote(NotesDto notesDto, String token, Long noteId);

	List<NotesModel> getAllNotes(String token);

	Response noteIntrash(String token, long noteId);

	Response noteIsPin(String token, long noteId);

	Response noteArchive(String token, long noteId);

	Response createNote(NotesDto notesDto, String token);

	List<NotesDto> getAllArchive(String token);

	Response setColor(String token, long noteId, String color);

	List<NotesDto> getAllTrash(String token);

	List<NotesDto> getAllPinedNotes(String token);

	Response permanentDelete(String token, long noteId);
	
	Response addCollaborator(String token,long noteId,String emailId);
}
