package com.bridgelabz.fundoo.elasticSearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoo.notes.model.NotesModel;
import com.bridgelabz.fundoo.response.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import com.bridgelabz.fundoo.utility.ResponseHelper;
import com.bridgelabz.fundoo.utility.TokenGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("elasticSearch")
public class elasticSearchImpli implements elasticSearch {
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private RestHighLevelClient client;

	@Autowired
	private TokenGenerator tokenUtil;

	@Autowired
	private Environment environment;

	private final String INDEX = "fundoonotes";
	// json Database name, always in small letters;
	private final String TYPE = "notesmodels";
	// json Database table name;

	@Override
	public Response createNote(NotesModel noteModel)
	// to create json database, also write updateNote in NoteServiceImpl
	{
		@SuppressWarnings("unchecked")
		Map<String, Object> documentMapper = objectMapper.convertValue(noteModel, Map.class);

		IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, String.valueOf(noteModel.getNoteId()))
				.source(documentMapper);
		// INDEX=name of database
		// TYPE=table Name
		try {
			client.index(indexRequest, RequestOptions.DEFAULT);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Response response = ResponseHelper.statusResponse(100, environment.getProperty("status.notes.created"));
		return response;
	}

	@Override
	public Response updateNote(NotesModel noteModel)
	// to update json database, also write updateNote in NoteServiceImpl
	{
		@SuppressWarnings("unchecked")
		Map<String, Object> documentMapper = objectMapper.convertValue(noteModel, Map.class);
		UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, String.valueOf(noteModel.getNoteId()))
				.doc(documentMapper);
		
		// INDEX=name of database
		// TYPE=table Name
		try {
			client.update(updateRequest, RequestOptions.DEFAULT);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Response response = ResponseHelper.statusResponse(200,
				environment.getProperty("status.notes.updatedSuccessfull"));
		return response;
	}

	@Override
	public Response deleteNote(long noteId) {
		@SuppressWarnings("unchecked")
		DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, String.valueOf(noteId));
		try {
			client.delete(deleteRequest, RequestOptions.DEFAULT);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Response resoponse = ResponseHelper.statusResponse(200, environment.getProperty("status.note.deleted"));

		return resoponse;
	}

	@Override
	public List<NotesModel> searchData(String query, String token) {
		long userId = tokenUtil.decodeToken(token);
		SearchRequest searchRequest = new SearchRequest(INDEX).types(TYPE);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.queryStringQuery("*" + query + "*")
				.analyzeWildcard(true).field("title").field("Discription"))
				.filter(QueryBuilders.termsQuery("userId", String.valueOf(userId)));
		System.out.println();
//		SearchSourceBuilder.query(QueryBuilder);
		
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;
		try {
			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			System.out.println(searchResponse);
		} catch (IOException e) {

			e.printStackTrace();
		}
//		List<NotesModel> allnote = getSearchResult(searchResponse);

		return getSearchResult(searchResponse);
	}

	private List<NotesModel> getSearchResult(SearchResponse response) {
		SearchHit[] searchHits = response.getHits().getHits();
		List<NotesModel> notes = new ArrayList<>();
		if (searchHits.length > 0) {
			Arrays.stream(searchHits)
					.forEach(hit -> notes.add(objectMapper.convertValue(hit.getSourceAsMap(), NotesModel.class)));

		}
		System.out.println(notes);
		return notes;
	}

}
