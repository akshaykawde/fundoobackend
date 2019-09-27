package com.bridgelabz.fundoo.elasticSearch;

import java.util.List;

import com.bridgelabz.fundoo.notes.model.NotesModel;
import com.bridgelabz.fundoo.response.Response;

public interface elasticSearch {
	Response createNote (NotesModel noteModel);
	Response updateNote (NotesModel noteModel);
	Response deleteNote (long noteId);
	List<NotesModel> searchData(String query, String token);
	}