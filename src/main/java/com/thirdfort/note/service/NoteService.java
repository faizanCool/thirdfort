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

    public Integer addNote(Note note) throws Exception{
        validateNote(note);
        Note newNote = noteRepository.save(note);
        return newNote.getNoteId();
    }

    public void updateNote(Note note) throws Exception {
        Note updateNote = validateNoteExistence(note);
        if (note.getNoteTitle() != null)
            updateNote.setNoteTitle(note.getNoteTitle());
        if (note.getNoteContent() != null)
            updateNote.setNoteContent(note.getNoteContent());
        if (note.getArchive() != null)
            updateNote.setArchive(note.getArchive());
        validateNote(updateNote);
        noteRepository.save(updateNote);
    }

    public List<Note> fetchNoteByUser(Note note) {
        return noteRepository.findByUserId(note.getUserId());
    }

    public List<Note> fetchFilteredNotes(Note note) {
        return noteRepository.findByUserIdAndArchive(note.getUserId(), note.getArchive());
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

    private void validateNote(Note note) throws Exception {
        if (StringUtils.isEmpty(note.getNoteTitle()))
            throw new Exception(Util.ERROR_MESSAGE.EMPTY_NOTE_TITLE);
        if (note.getNoteTitle().length() > Util.MAX_NOTE_TITLE_LENGTH)
            throw new Exception(Util.ERROR_MESSAGE.EXCEED_NOTE_TITLE_SIZE);
        if (StringUtils.isEmpty(note.getNoteContent()))
            throw new Exception(Util.ERROR_MESSAGE.EMPTY_NOTE_CONTENT);
        if (note.getNoteContent().length() > Util.MAX_NOTE_LENGTH)
            throw new Exception(Util.ERROR_MESSAGE.EXCEED_NOTE_SIZE);
    }

    private Note validateNoteExistence(Note note) throws Exception{
        if (ObjectUtils.isEmpty(note.getNoteId()))
            throw new Exception(Util.ERROR_MESSAGE.EMPTY_NOTE_ID);
        Note fetchedNote = noteRepository.findByNoteId(note.getNoteId());
        if (ObjectUtils.isEmpty(fetchedNote))
            throw new Exception(Util.ERROR_MESSAGE.NOTE_DOES_NOT_EXISTS);
        return fetchedNote;
    }
}

