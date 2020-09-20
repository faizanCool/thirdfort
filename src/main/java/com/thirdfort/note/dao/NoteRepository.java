package com.thirdfort.note.dao;

import com.thirdfort.note.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Integer> {

    Note findByNoteId(Integer noteId);

    @Query(value = "SELECT note_id, user_id, note_title, is_archive, '' as note_content FROM note n" +
            " WHERE n.user_id = :userId AND (:archive is NULL OR n.is_archive = :archive) AND " +
            "(:title is NULL OR :title = '' OR n.note_title LIKE %:title%)", nativeQuery = true)
    List<Note> filterNotes(@Param("userId") String userId, @Param("archive") Boolean isArchive, @Param("title") String noteTitle);

    @Query(value = "SELECT note_id, user_id, note_title, is_archive, '' as note_content FROM note n WHERE n.user_id = :userId", nativeQuery = true)
    List<Note> findAllByUser(@Param("userId") String userId);

}
