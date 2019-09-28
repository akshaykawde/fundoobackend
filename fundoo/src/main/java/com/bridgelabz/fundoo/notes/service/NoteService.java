package com.bridgelabz.fundoo.notes.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.bridgelabz.fundoo.notes.dto.NotesDto;

import com.bridgelabz.fundoo.notes.model.Note;
import com.bridgelabz.fundoo.response.Response;

public interface NoteService {

	Response update(NotesDto notesDto, String token, Long noteId);

	List<Note> getAllNotes(String token);

	Response trash(String token, long noteId);

	Response pin(String token, long noteId);

	Response archive(String token, long noteId);

	Response temporarydelete(String token, long noteId);

	Response onCrete(NotesDto notesDto, String token);

	List<NotesDto> getAllArchive(String token);

	Response setColor(String token, long noteId, String color);

	List<NotesDto> getAllTrash(String token);

	List<NotesDto> getAllPinedNotes(String token);

	Response permanentdelete(String token, long noteId);

}
