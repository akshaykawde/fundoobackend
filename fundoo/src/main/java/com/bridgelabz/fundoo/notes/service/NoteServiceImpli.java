package com.bridgelabz.fundoo.notes.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.validation.constraints.Email;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoo.elasticSearch.elasticSearch;
import com.bridgelabz.fundoo.exception.UserException;
import com.bridgelabz.fundoo.notes.dto.NotesDto;
import com.bridgelabz.fundoo.notes.model.NotesModel;
import com.bridgelabz.fundoo.notes.repository.NotesRepo;
import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.user.model.MailModel;
import com.bridgelabz.fundoo.user.model.UserModel;
import com.bridgelabz.fundoo.user.repository.UserRepository;
import com.bridgelabz.fundoo.utility.RabbitMqSenderImpl;
import com.bridgelabz.fundoo.utility.ResponseHelper;
import com.bridgelabz.fundoo.utility.TokenGenerator;
import com.bridgelabz.fundoo.utility.Utility;
import com.sun.mail.handlers.message_rfc822;

@Service
public class NoteServiceImpli implements NoteService {
	@Autowired
	private UserRepository userRepositary;

	@Autowired
	private NotesRepo noteRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private elasticSearch elasticSearch;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private Response response;

	@Autowired
	private Response statusResponse;

	@Autowired
	private Utility utility;

	@Autowired
	private RabbitMqSenderImpl rabbitMqSenderImpl;

	@Autowired
	private TokenGenerator tokenUtil;

	@Override
	public Response createNote(NotesDto notesDto, String token) {
		long id = tokenUtil.decodeToken(token);
		if (notesDto.getTitle().isEmpty() && notesDto.getDiscription().isEmpty()) {
			throw new UserException("Title and Discription is empty");
		}
		NotesModel notesModel = modelMapper.map(notesDto, NotesModel.class);
		Optional<UserModel> userModel = userRepositary.findByuserId(id);
		userModel.get().getNotes().add(notesModel);
		notesModel.setUserId(id);
		noteRepository.save(notesModel);
		// userRepositary.save(user.get());
		elasticSearch.createNote(notesModel);
		rabbitMqSenderImpl.sendMessageToQueue(notesModel);
		// user to create json database of elasticSearch
		Response response = ResponseHelper.statusResponse(200, " note created successfully");
		return response;
	}

	@Override
	public Response updateNote(NotesDto notesDto, String token, Long noteID) {
		if (notesDto.getTitle().isEmpty() && notesDto.getDiscription().isEmpty()) {
			throw new UserException("Title and Discription is empty");
		}
		long id = tokenUtil.decodeToken(token);
		// Note notes=NotesRepo.findByIdAndUserId(noteID, id);
		NotesModel notesModels = noteRepository.findByNoteIdAndUserId(noteID, id);
		notesModels.setTitle(notesDto.getTitle());
		notesModels.setDiscription(notesDto.getDiscription());
		noteRepository.save(notesModels);
		elasticSearch.updateNote(notesModels);
		// user to update json database of elasticSearch
		Response response = ResponseHelper.statusResponse(200, "notes updated");
		return response;
	}

	@Override
	public Response noteIntrash(String token, long noteId) {
		long userId = tokenUtil.decodeToken(token);
		NotesModel notesModels = noteRepository.findByNoteIdAndUserId(noteId, userId);
		if (notesModels.isTrash() == false) {
			notesModels.setTrash(true);
			noteRepository.save(notesModels);
			Response response = ResponseHelper.statusResponse(200, "Note is in trash");
			return response;

		} else {
			notesModels.setTrash(false);
			noteRepository.save(notesModels);
			Response response = ResponseHelper.statusResponse(200, "note is untrash!!");
			return response;
		}

	}

	@Override
	public Response noteIsPin(String token, long noteId) {
		long userId = tokenUtil.decodeToken(token);
		NotesModel notesModels = noteRepository.findByNoteIdAndUserId(noteId, userId);
		if (notesModels.isPin() == false) {
			notesModels.setPin(true);
			noteRepository.save(notesModels);
			Response response = ResponseHelper.statusResponse(200, "Note is pined");
			return response;

		} else {
			notesModels.setPin(false);
			noteRepository.save(notesModels);
			Response response = ResponseHelper.statusResponse(200, "note is unpined!!");
			return response;
		}

	}

	@Override
	public Response noteArchive(String token, long noteId) {
		long userId = tokenUtil.decodeToken(token);
		NotesModel notesModels = noteRepository.findByNoteIdAndUserId(noteId, userId);
		if (notesModels.isArchive() == false) {
			notesModels.setArchive(true);
			noteRepository.save(notesModels);
			Response response = ResponseHelper.statusResponse(200, "Note is archive");
			return response;

		} else {
			notesModels.setArchive(false);
			noteRepository.save(notesModels);
			Response response = ResponseHelper.statusResponse(200, "note is unarchive");
			return response;
		}

	}

	@Override
	public List<NotesModel> getAllNotes(String token) {
		long userId = tokenUtil.decodeToken(token);
		UserModel userModel = userRepositary.findById(userId).get();
		return (List<NotesModel>) userModel.getNotes();
	}

	@Override
	public List<NotesDto> getAllArchive(String token) {
		long userId = tokenUtil.decodeToken(token);
		List<NotesModel> notesModels = (List<NotesModel>) noteRepository.findByUserId(userId);

		List<NotesDto> listNotes = new ArrayList<NotesDto>();
		for (NotesModel userNotes : notesModels) {
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
		List<NotesModel> notesModels = (List<NotesModel>) noteRepository.findByUserId(userId);
		List<NotesDto> listNotes = new ArrayList<NotesDto>();
		for (NotesModel userNotes : notesModels) {
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
		List<NotesModel> User = noteRepository.findByUserId(userId);
		List<NotesDto> list = new ArrayList<NotesDto>();
		for (NotesModel userNote : User) {
			NotesDto notesDto = modelMapper.map(userNote, NotesDto.class);
			if (userNote.isPin() == true) {
				list.add(notesDto);
			}
		}
		return list;
	}

	@Override
	public Response setColor(String token, long noteId, String color) {
		long userId = tokenUtil.decodeToken(token);
		NotesModel notesModels = noteRepository.findByNoteIdAndUserId(noteId, userId);
		if (notesModels == null) {
			throw new UserException(100, "invalid note or not exist");
		}
		notesModels.setSetcolour(color);
		noteRepository.save(notesModels);
		Response response = ResponseHelper.statusResponse(200, "colour change");
		return response;

	}

	public Response permanentDelete(String token, long noteId) {
		long userId = tokenUtil.decodeToken(token);
		Optional<UserModel> userModel = userRepositary.findById(userId);
		NotesModel notesModel = noteRepository.findById(noteId).orElseThrow();
		System.out.println(notesModel);
		if (notesModel.isTrash() == true) {
			((List<NotesModel>) userModel.get().getNotes()).remove(notesModel);
			userRepositary.save(userModel.get());
			noteRepository.delete(notesModel);
			elasticSearch.deleteNote(noteId);
			Response response = ResponseHelper.statusResponse(200, "note deleted sucessfuly");
			return response;
		} else {
			Response response = ResponseHelper.statusResponse(100, "Note is deleted only if, when it is in Trash");
			return response;
		}

	}

	public Response addCollaborator(String token, long noteId, String emailId) {
		MailModel collabEmail = new MailModel();
		long userId = tokenUtil.decodeToken(token);
		Optional<UserModel> user = userRepositary.findByEmailId(emailId);
		Optional<UserModel> MainUser = userRepositary.findById(userId);
		if (!user.isPresent())
			throw new UserException("User is not present");
		NotesModel note = noteRepository.findByNoteIdAndUserId(noteId, userId);

		if (note == null)
			throw new UserException("Note is empty");

		if (user.get().getCollaboratedNotes().contains(note))
			throw new UserException(-5, "Note is already collaborated");

		user.get().getCollaboratedNotes().add(note);
		note.getCollaboratedUser().add(user.get());

		userRepositary.save(user.get());
		noteRepository.save(note);

		collabEmail.setFrom("akshay.skawde@gmail.com");
		collabEmail.setTo(emailId);
		collabEmail.setBody("Note from" + MainUser.get() + "collaborated to you,  Title:" + note.getTitle()
				+ "Discription :" + note.getDiscription());
		utility.sendEmail(collabEmail);
		Response response = ResponseHelper.statusResponse(200, "collaborated");
		return response;
	}

	@Override
	public Response deleteCollaborator(String token, long noteId, String emailId) {
		long userId = tokenUtil.decodeToken(token);
		Optional<UserModel> user = userRepositary.findByEmailId(emailId);
		if (!user.isPresent())
			throw new UserException(-4, "No user exist");

		NotesModel note = noteRepository.findByNoteIdAndUserId(noteId, userId);
		if (note == null)
			throw new UserException(-5, "Note is not exist");
		user.get().getCollaboratedNotes().remove(note);
		note.getCollaboratedUser().remove(user.get());
		userRepositary.save(user.get());
		noteRepository.save(note);
		Response response = ResponseHelper.statusResponse(100, "status.note.trashError");
		return response;

	}

}