package com.bridgelabz.fundoo.notes.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bridgelabz.fundoo.notes.model.NotesModel;
@Repository
public interface NotesRepo extends JpaRepository<NotesModel, Long> 
{
	public NotesModel findByNoteIdAndUserId(long noteId,long userId);
	public NotesModel findByisTrashAndUserId(boolean isTrash, long id);
	public List<NotesModel> findByUserId(long userId);	
}
