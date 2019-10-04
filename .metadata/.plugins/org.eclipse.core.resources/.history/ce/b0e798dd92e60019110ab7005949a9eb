package com.bridgelabz.fundoo.notes.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.bridgelabz.fundoo.notes.dto.NotesDto;

import com.bridgelabz.fundoo.notes.model.Note;
import com.bridgelabz.fundoo.response.Response;

public interface NoteService {

	Response update(NotesDto notesDto, long noteId);

	List<Note> getAllNotes();

	Response trash(long noteId);

	Response pin(long noteId);

	Response archive(long noteId);

	Response temporarydelete(long noteId);

	Response onCrete(NotesDto notesDto);

	List<NotesDto> getAllArchive();

	Response setColor(long noteId, String color);

	List<NotesDto> getAllTrash();

	List<NotesDto> getAllPinedNotes();

	Response permanentdelete(long noteId);
//
//	String getToken(String emailId);
//
//	String getTokenByuserId(String userId);

}
