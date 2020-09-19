package com.thirdfort.note.service;

import com.thirdfort.note.dao.NoteRepository;
import com.thirdfort.note.model.Note;
import com.thirdfort.note.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    NoteRepository noteRepository;

    public Integer saveOrUpdateNote(Note note) throws Exception{
        if (ObjectUtils.isEmpty(note) || StringUtils.isEmpty(note.getNoteContent()))
            throw new Exception(Util.ERROR_MESSAGE.INVALID_NOTE);
        if (note.getNoteContent().length() > Util.MAX_NOTE_LENGTH)
            throw new Exception(Util.ERROR_MESSAGE.EXCEED_NOTE_SIZE);
        Note newNote = noteRepository.save(note);
        return newNote.getNoteId();
    }

    public List<Note> fetchNoteByUser(Note note) {
        return noteRepository.findByUserId(note.getUserId());
    }

    public List<Note> fetchFilteredNotes(Note note) {
        return noteRepository.findByUserIdAndArchive(note.getUserId(), note.isArchive());
    }

    public void deleteNote(Note note) {
        noteRepository.delete(note);
    }

    public void archiveNote(Note note) {
        Note unarchiveNote = noteRepository.findByNoteId(note.getNoteId());
        unarchiveNote.setArchive(true);
        noteRepository.save(unarchiveNote);
    }

    public void unarchiveNote(Note note) {
        Note archiveNote = noteRepository.findByNoteId(note.getNoteId());
        archiveNote.setArchive(false);
        noteRepository.save(archiveNote);
    }
}

