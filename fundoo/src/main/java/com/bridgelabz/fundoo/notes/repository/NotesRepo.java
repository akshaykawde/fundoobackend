package com.bridgelabz.fundoo.notes.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bridgelabz.fundoo.notes.model.Note;
@Repository
public interface NotesRepo extends JpaRepository<Note, Long> 
{
	public Note findByNoteIdAndUserId(long noteId,long userId);
	public Note findByisTrashAndUserId(boolean isTrash, long id);
	public List<Note> findByUserId(long userId);	
}
