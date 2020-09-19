package com.thirdfort.note.dao;

import com.thirdfort.note.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    List<Note> findByUserIdAndArchive(String userId, boolean isArchive);
    List<Note> findByUserId(String userId);
    Note findByNoteId(Integer noteId);
}
