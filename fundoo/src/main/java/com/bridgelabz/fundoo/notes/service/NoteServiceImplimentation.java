package com.bridgelabz.fundoo.notes.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.loader.plan.exec.process.spi.ReturnReader;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.w3c.dom.ls.LSInput;

import com.bridgelabz.fundoo.exception.UserException;
import com.bridgelabz.fundoo.notes.dto.NotesDto;

import com.bridgelabz.fundoo.notes.model.Note;
import com.bridgelabz.fundoo.notes.repository.NotesRepo;
import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.user.model.User;
import com.bridgelabz.fundoo.user.repository.UserRepo;
import com.bridgelabz.fundoo.utility.ResponseHelper;
import com.bridgelabz.fundoo.utility.TokenGenerator;
import com.bridgelabz.fundoo.utility.Utility;

@Service
public class NoteServiceImplimentation implements NoteService {
	@Autowired
	private UserRepo userRepositary;

	@Autowired
	private NotesRepo noteRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Response response;

	@Autowired
	private Response statusResponse;

	@Autowired
	private Utility utility;

	@Autowired
	private TokenGenerator tokenUtil;

	@Override
	public Response onCrete(NotesDto notesDto, String token) {
		long id = tokenUtil.decodeToken(token);
		if (notesDto.getTitle().isEmpty() && notesDto.getDiscription().isEmpty()) {
			throw new UserException("Title and Discription is empty");
		}
		Note note = modelMapper.map(notesDto, Note.class);
		Optional<User> user = userRepositary.findByuserId(id);
		user.get().getNotes().add(note);
		note.setUserId(id);
		noteRepository.save(note);
		// userRepositary.save(user.get());
		Response response = ResponseHelper.statusResponse(200, " note created successfully");
		return response;
	}

	@Override
	public Response update(NotesDto notesDto, String token, Long noteID) {
		if (notesDto.getTitle().isEmpty() && notesDto.getDiscription().isEmpty()) {
			throw new UserException("Title and Discription is empty");
		}
		long id = tokenUtil.decodeToken(token);
		// Note notes=NotesRepo.findByIdAndUserId(noteID, id);
		Note notes = noteRepository.findByNoteIdAndUserId(noteID, id);
		notes.setTitle(notesDto.getTitle());
		notes.setDiscription(notesDto.getDiscription());
		noteRepository.save(notes);
		Response response = ResponseHelper.statusResponse(200, "notes updated");
		return response;
	}

	@Override
	public Response trash(String token, long noteId) {
		long userId = tokenUtil.decodeToken(token);
		Note notes = noteRepository.findByNoteIdAndUserId(noteId, userId);
		if (notes.isTrash() == false) {
			notes.setTrash(true);
			noteRepository.save(notes);
			Response response = ResponseHelper.statusResponse(200, "Note is in trash");
			return response;

		} else {
			notes.setTrash(false);
			noteRepository.save(notes);
			Response response = ResponseHelper.statusResponse(200, "note is untrash!!");
			return response;
		}

	}

	@Override
	public Response pin(String token, long noteId) {
		long userId = tokenUtil.decodeToken(token);
		Note notes = noteRepository.findByNoteIdAndUserId(noteId, userId);
		if (notes.isPin() == false) {
			notes.setPin(true);
			noteRepository.save(notes);
			Response response = ResponseHelper.statusResponse(200, "Note is pined");
			return response;

		} else {
			notes.setPin(false);
			noteRepository.save(notes);
			Response response = ResponseHelper.statusResponse(200, "note is unpined!!");
			return response;
		}

	}

	@Override
	public Response archive(String token, long noteId) {
		long userId = tokenUtil.decodeToken(token);
		Note notes = noteRepository.findByNoteIdAndUserId(noteId, userId);
		if (notes.isArchive() == false) {
			notes.setArchive(true);
			noteRepository.save(notes);
			Response response = ResponseHelper.statusResponse(200, "Note is archive");
			return response;

		} else {
			notes.setArchive(false);
			noteRepository.save(notes);
			Response response = ResponseHelper.statusResponse(200, "note is unarchive");
			return response;
		}

	}

	@Override
	public List<Note> getAllNotes(String token) {
		long userId = tokenUtil.decodeToken(token);
		User user = userRepositary.findById(userId).get();
		return (List<Note>) user.getNotes();
	}

	@Override
	public List<NotesDto> getAllArchive(String token) {
		long userId = tokenUtil.decodeToken(token);
		List<Note> notes = (List<Note>) noteRepository.findByUserId(userId);

		List<NotesDto> listNotes = new ArrayList<NotesDto>();
		for (Note userNotes : notes) {
			NotesDto noteDto = modelMapper.map(userNotes, NotesDto.class);
			if (userNotes.isArchive() == true && userNotes.isTrash() == false) {
				listNotes.add(noteDto);
			}

		}
		return listNotes;
	}

	@Override
	public List<NotesDto> getAllTrash(String token) {
		long userId = tokenUtil.decodeToken(token);
		List<Note> notes = (List<Note>) noteRepository.findByUserId(userId);
		List<NotesDto> listNotes = new ArrayList<NotesDto>();
		for (Note userNotes : notes) {
			NotesDto notesDto = modelMapper.map(userNotes, NotesDto.class);
			if (userNotes.isTrash() == true) {
				listNotes.add(notesDto);
			}
		}
		return listNotes;
	}

	@Override
	public List<NotesDto> getAllPinedNotes(String token) {
		long userId = tokenUtil.decodeToken(token);
		List<Note> User = noteRepository.findByUserId(userId);
		List<NotesDto> list = new ArrayList<NotesDto>();
		for (Note userNote : User) {
			NotesDto notesDto = modelMapper.map(userNote, NotesDto.class);
			if (userNote.isPin() == true) {
				list.add(notesDto);
			}
		}
		return list;
	}

	@Override
	public Response temporarydelete(String token, long noteId) {
		long userId = tokenUtil.decodeToken(token);
		Note notes = noteRepository.findByNoteIdAndUserId(noteId, userId);
		if (notes.isTrash() == false) {
			notes.setTrash(true);
			noteRepository.save(notes);
			Response response = ResponseHelper.statusResponse(200, "Note is in trash");
			return response;

		} else {
			notes.setTrash(false);
			noteRepository.save(notes);
			Response response = ResponseHelper.statusResponse(200, "note is untrash!!");
			return response;
		}
	}

	@Override
	public Response setColor(String token, long noteId, String color) {
		long userId = tokenUtil.decodeToken(token);
		Note notes = noteRepository.findByNoteIdAndUserId(noteId, userId);
		if (notes == null) {
			throw new UserException(100, "invalid note or not exist");
		}
		notes.setSetcolour(color);
		noteRepository.save(notes);
		Response response = ResponseHelper.statusResponse(200, "colour change");
		return response;

	}

	public Response permanentdelete(String token, long noteId) {
		long userId = tokenUtil.decodeToken(token);
		Optional<User> user = userRepositary.findById(userId);
		Note note = noteRepository.findById(noteId).orElseThrow();
		System.out.println(note);
		if (note.isTrash() == true) {
			((List<Note>) user.get().getNotes()).remove(note);
			userRepositary.save(user.get());
			noteRepository.delete(note);
			Response response = ResponseHelper.statusResponse(200, "note deleted sucessfuly");
			return response;
		} else {
			Response response = ResponseHelper.statusResponse(100, "failed to delet");
			return response;
		}

	}
}