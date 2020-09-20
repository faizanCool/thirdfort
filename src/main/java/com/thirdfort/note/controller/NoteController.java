package com.thirdfort.note.controller;

import com.thirdfort.note.model.Note;
import com.thirdfort.note.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    NoteService noteService;

    @PostMapping("/add")
    Integer addNote(@RequestBody Note note) throws Exception {
        return noteService.addNote(note);
    }

    @PutMapping("/update")
    void updateNote(@RequestBody Note note) throws Exception {
        noteService.updateNote(note);
    }

    @GetMapping("/")
    List<Note> fetchAllNotes(@RequestBody Note note) {
        return noteService.fetchNoteByUser(note);
    }

    @GetMapping("/search")
    List<Note> fetchFilterArchiveNote(@RequestBody Note note) {
        return noteService.fetchFilteredNotes(note);
    }

    @DeleteMapping("/delete")
    void deleteNote(@RequestBody Note note) {
        noteService.deleteNote(note);
    }

    @PutMapping("/archive")
    void archive(@RequestBody Note note) throws Exception {
        noteService.archiveNote(note);
    }

    @PutMapping("/unarchive")
    void unarchive(@RequestBody Note note) throws Exception {
        noteService.unarchiveNote(note);
    }

    @GetMapping("/getnote")
    void findNoteById(@RequestBody Note note) throws Exception {
        noteService.findNoteById(note);
    }
}
