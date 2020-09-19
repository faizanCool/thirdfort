package com.thirdfort.note;

import com.thirdfort.note.dao.NoteRepository;
import com.thirdfort.note.model.Note;
import com.thirdfort.note.service.NoteService;
import com.thirdfort.note.util.Util;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class NoteApplicationTests {

	@Autowired
	private NoteService noteService;
	@Autowired
	private NoteRepository noteRepository;

	@BeforeEach
	void beforeEachTest() {
		noteRepository.deleteAll();
	}

	@Test
	void addUnarchiveNoteForUser1() throws Exception {
		Note note = new Note();
		note.setArchive(false);
		note.setNoteContent("user1 unarchived note");
		note.setUserId("user_1");
		Integer noteId = noteService.saveOrUpdateNote(note);
		List<Note> notes = noteService.fetchNoteByUser(note);
		Assertions.assertEquals("user_1", notes.get(0).getUserId());
		Assertions.assertEquals(noteId, notes.get(0).getNoteId());
		Assertions.assertEquals("user1 unarchived note", notes.get(0).getNoteContent());
		Assertions.assertEquals(false, notes.get(0).isArchive());
	}

	@Test
	void addArchiveNoteUser1() throws Exception {
		Note note = new Note();
		note.setArchive(true);
		note.setNoteContent("user1 archived note");
		note.setUserId("user_1");
		Integer noteId = noteService.saveOrUpdateNote(note);
		List<Note> notes = noteService.fetchNoteByUser(note);
		Assertions.assertEquals("user_1", notes.get(0).getUserId());
		Assertions.assertEquals(noteId, notes.get(0).getNoteId());
		Assertions.assertEquals("user1 archived note", notes.get(0).getNoteContent());
		Assertions.assertEquals(true, notes.get(0).isArchive());
	}

	@Test
	void getAllNotes() throws Exception {
		addNotes();

		Note note = new Note();
		note.setUserId("user_1");
		List<Note> user1Notes = noteService.fetchNoteByUser(note);
		Assertions.assertEquals(2, user1Notes.size());
		user1Notes.forEach(userNote -> {
			Assertions.assertEquals("user_1", userNote.getUserId());
		});

		note.setUserId("user_2");
		List<Note> user2Notes = noteService.fetchNoteByUser(note);
		Assertions.assertEquals(1, user2Notes.size());
		user2Notes.forEach(userNote -> {
			Assertions.assertEquals("user_2", userNote.getUserId());
		});
	}

	@Test
	void getArchiveNotes() throws Exception {
		addNotes();

		Note note = new Note();
		note.setUserId("user_1");
		note.setArchive(true);

		List<Note> user1ArchiveNotes = noteService.fetchFilteredNotes(note);
		Assertions.assertEquals(1, user1ArchiveNotes.size());
		user1ArchiveNotes.forEach(userNote -> {
			Assertions.assertEquals("user_1", userNote.getUserId());
			Assertions.assertEquals(true, userNote.isArchive());
		});


		note.setUserId("user_2");
		List<Note> user2ArchiveNotes = noteService.fetchFilteredNotes(note);
		Assertions.assertEquals(0, user2ArchiveNotes.size());
	}

	@Test
	void getUnArchiveNotes() throws Exception {
		addNotes();

		Note note = new Note();
		note.setUserId("user_1");
		note.setArchive(false);

		List<Note> user1UnArchiveNotes = noteService.fetchFilteredNotes(note);
		Assertions.assertEquals(1, user1UnArchiveNotes.size());
		user1UnArchiveNotes.forEach(userNote -> {
			Assertions.assertEquals("user_1", userNote.getUserId());
			Assertions.assertEquals(false, userNote.isArchive());
		});

		note.setUserId("user_2");
		List<Note> user2UnArchiveNotes = noteService.fetchFilteredNotes(note);
		Assertions.assertEquals(1, user2UnArchiveNotes.size());
		user2UnArchiveNotes.forEach(userNote -> {
			Assertions.assertEquals("user_2", userNote.getUserId());
			Assertions.assertEquals(false, userNote.isArchive());
		});
	}

	@Test
	void archiveUnarchiveNote() throws Exception {
		Note archiveNote = new Note();
		archiveNote.setArchive(false);
		archiveNote.setNoteContent("user1 unarchived note");
		archiveNote.setUserId("user_1");
		int noteId = noteService.saveOrUpdateNote(archiveNote);

		Note note = new Note();
		note.setUserId("user_1");
		List<Note> user1Notes = noteService.fetchNoteByUser(note);

		Assertions.assertEquals(noteId, user1Notes.get(0).getNoteId());
		Assertions.assertEquals(false, user1Notes.get(0).isArchive());

		archiveNote.setArchive(true);
		noteService.saveOrUpdateNote(archiveNote);

		user1Notes = noteService.fetchNoteByUser(note);
		Assertions.assertEquals(noteId, user1Notes.get(0).getNoteId());
		Assertions.assertEquals(true, user1Notes.get(0).isArchive());
	}

	@Test
	void unarchiveArchiveNote() throws Exception {
		Note unarchiveNote = new Note();
		unarchiveNote.setArchive(true);
		unarchiveNote.setNoteContent("user1 archived note");
		unarchiveNote.setUserId("user_1");
		int noteId = noteService.saveOrUpdateNote(unarchiveNote);

		Note note = new Note();
		note.setUserId("user_1");
		List<Note> user1Notes = noteService.fetchNoteByUser(note);

		Assertions.assertEquals(noteId, user1Notes.get(0).getNoteId());
		Assertions.assertEquals(true, user1Notes.get(0).isArchive());

		unarchiveNote.setArchive(false);
		noteService.saveOrUpdateNote(unarchiveNote);

		user1Notes = noteService.fetchNoteByUser(note);
		Assertions.assertEquals(noteId, user1Notes.get(0).getNoteId());
		Assertions.assertEquals(false, user1Notes.get(0).isArchive());
	}

	@Test
	void updateNoteContent() throws Exception {
		Note unarchiveNote = new Note();
		unarchiveNote.setArchive(true);
		unarchiveNote.setNoteContent("user1 unarchived note");
		unarchiveNote.setUserId("user_1");
		int noteId = noteService.saveOrUpdateNote(unarchiveNote);

		Note note = new Note();
		note.setUserId("user_1");
		List<Note> user1Notes = noteService.fetchNoteByUser(note);
		Assertions.assertEquals("user1 unarchived note", user1Notes.get(0).getNoteContent());
		Assertions.assertEquals(noteId, user1Notes.get(0).getNoteId());

		unarchiveNote.setNoteContent("user1 updated unarchived note");
		noteService.saveOrUpdateNote(unarchiveNote);

		user1Notes = noteService.fetchNoteByUser(note);
		Assertions.assertEquals("user1 updated unarchived note", user1Notes.get(0).getNoteContent());
		Assertions.assertEquals(noteId, user1Notes.get(0).getNoteId());
	}

	@Test
	void noteContentCantBeEmpty() throws Exception {
		Note unarchiveNote = new Note();
		unarchiveNote.setArchive(false);
		unarchiveNote.setNoteContent("");
		unarchiveNote.setUserId("user_1");

		Exception exception = Assertions.assertThrows(Exception.class, () -> {
			noteService.saveOrUpdateNote(unarchiveNote);
		});
		Assertions.assertEquals(Util.ERROR_MESSAGE.INVALID_NOTE, exception.getMessage());
	}

	@Test
	void noteContentCantBeExceed250Char() throws Exception {
		String content = "1234567890";
		content = content.repeat(25);
		content += "1";

		Assertions.assertTrue(content.length() > Util.MAX_NOTE_LENGTH);

		Note unarchiveNote = new Note();
		unarchiveNote.setArchive(false);
		unarchiveNote.setNoteContent(content);
		unarchiveNote.setUserId("user_1");

		Exception exception = Assertions.assertThrows(Exception.class, () -> {
			noteService.saveOrUpdateNote(unarchiveNote);
		});
		Assertions.assertEquals(Util.ERROR_MESSAGE.EXCEED_NOTE_SIZE, exception.getMessage());
	}

	@Test
	void deleteNote() throws Exception {
		Note unarchiveNote = new Note();
		unarchiveNote.setArchive(false);
		unarchiveNote.setNoteContent("user1 unarchive note");
		unarchiveNote.setUserId("user_1");
		int noteId = noteService.saveOrUpdateNote(unarchiveNote);

		Note note = new Note();
		note.setUserId("user_1");
		List<Note> user1Notes = noteService.fetchNoteByUser(note);
		Assertions.assertEquals(1, user1Notes.size());

		Note deleteNote = new Note();
		deleteNote.setNoteId(noteId);
		noteService.deleteNote(deleteNote);

		user1Notes = noteService.fetchNoteByUser(note);
		Assertions.assertEquals(0, user1Notes.size());
	}

	private void addNotes() throws Exception {
		// unarchive note for user_2
		Note note1 = new Note();
		note1.setArchive(false);
		note1.setNoteContent("user2 unarchived note");
		note1.setUserId("user_2");
		noteService.saveOrUpdateNote(note1);

		// unarchive note for user_1
		Note note2 = new Note();
		note2.setArchive(false);
		note2.setNoteContent("user1 unarchived note");
		note2.setUserId("user_1");
		noteService.saveOrUpdateNote(note2);

		// archive note for user_1
		Note note3 = new Note();
		note3.setArchive(true);
		note3.setNoteContent("user1 archived note");
		note3.setUserId("user_1");
		noteService.saveOrUpdateNote(note3);
	}
}
