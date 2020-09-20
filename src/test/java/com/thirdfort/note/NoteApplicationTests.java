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
    void findNoteById() throws Exception {
	    Note addNote = new Note();
	    addNote.setNoteTitle("test");
	    addNote.setNoteContent("test c");
	    addNote.setArchive(false);
	    addNote.setUserId("user_1");
	    Integer id = noteService.addNote(addNote);

	    Note fetchNote = noteService.findNoteById(addNote);
	    Assertions.assertEquals(id, fetchNote.getNoteId());

	    fetchNote.setNoteId(999);
        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            noteService.findNoteById(fetchNote);
        });
        Assertions.assertEquals(Util.ERROR_MESSAGE.NOTE_DOES_NOT_EXISTS, exception.getMessage());

        Note note = new Note();
        exception = Assertions.assertThrows(Exception.class, () -> {
            noteService.findNoteById(note);
        });
        Assertions.assertEquals(Util.ERROR_MESSAGE.EMPTY_NOTE_ID, exception.getMessage());
    }

	@Test
	void addUnarchiveNoteForUser1() throws Exception {
		Note note = new Note();
		note.setArchive(false);
		note.setNoteContent("user1 unarchived note");
		note.setUserId("user_1");
		note.setNoteTitle("title test1 title");
		Integer noteId = noteService.addNote(note);

		Note fetchNote = noteService.findNoteById(note);
		Assertions.assertEquals("user_1", fetchNote.getUserId());
		Assertions.assertEquals(noteId, fetchNote.getNoteId());
		Assertions.assertEquals("user1 unarchived note", fetchNote.getNoteContent());
		Assertions.assertEquals(false, fetchNote.getArchive());
	}

	@Test
	void addArchiveNoteUser1() throws Exception {
		Note note = new Note();
		note.setArchive(true);
		note.setNoteContent("user1 archived note");
		note.setUserId("user_1");
        note.setNoteTitle("title test1 title");
		Integer noteId = noteService.addNote(note);

		Note fetchNote = noteService.findNoteById(note);
		Assertions.assertEquals("user_1", fetchNote.getUserId());
		Assertions.assertEquals(noteId, fetchNote.getNoteId());
		Assertions.assertEquals("user1 archived note", fetchNote.getNoteContent());
		Assertions.assertEquals(true, fetchNote.getArchive());
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
			Assertions.assertEquals(true, userNote.getArchive());
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
			Assertions.assertEquals(false, userNote.getArchive());
		});

		note.setUserId("user_2");
		List<Note> user2UnArchiveNotes = noteService.fetchFilteredNotes(note);
		Assertions.assertEquals(1, user2UnArchiveNotes.size());
		user2UnArchiveNotes.forEach(userNote -> {
			Assertions.assertEquals("user_2", userNote.getUserId());
			Assertions.assertEquals(false, userNote.getArchive());
		});
	}

	@Test
	void archiveANote() throws Exception {
		Note unarchiveNote = new Note();
		unarchiveNote.setArchive(false);
		unarchiveNote.setNoteContent("user1 unarchived note");
		unarchiveNote.setUserId("user_1");
		unarchiveNote.setNoteTitle("archive note");
		int noteId = noteService.addNote(unarchiveNote);

		Note fetchNote = noteService.findNoteById(unarchiveNote);

		Assertions.assertEquals(noteId, fetchNote.getNoteId());
		Assertions.assertEquals(false, fetchNote.getArchive());

		fetchNote.setArchive(true);
		noteService.archiveNote(fetchNote);

		Note archivedNote = noteService.findNoteById(fetchNote);
		Assertions.assertEquals(noteId, archivedNote.getNoteId());
		Assertions.assertEquals(true, archivedNote.getArchive());
	}

	@Test
	void unarchiveNote() throws Exception {
		Note archiveNote = new Note();
		archiveNote.setArchive(true);
		archiveNote.setNoteContent("user1 archived note");
		archiveNote.setUserId("user_1");
		archiveNote.setNoteTitle("unarchive note");
		int noteId = noteService.addNote(archiveNote);

		Note fetchNote = noteService.findNoteById(archiveNote);

		Assertions.assertEquals(noteId, fetchNote.getNoteId());
		Assertions.assertEquals(true, fetchNote.getArchive());

		fetchNote.setArchive(false);
		noteService.unarchiveNote(fetchNote);

		Note unarchiveNote = noteService.findNoteById(fetchNote);
		Assertions.assertEquals(noteId, unarchiveNote.getNoteId());
		Assertions.assertEquals(false, unarchiveNote.getArchive());
	}

	@Test
	void updateNoteContentAndNoteTitle() throws Exception {
		Note unarchiveNote = new Note();
		unarchiveNote.setArchive(true);
		unarchiveNote.setNoteContent("user1 unarchived note");
		unarchiveNote.setUserId("user_1");
		unarchiveNote.setNoteTitle("before update");
		int noteId = noteService.addNote(unarchiveNote);

		Note note = noteService.findNoteById(unarchiveNote);
		Assertions.assertEquals("user1 unarchived note", note.getNoteContent());
        Assertions.assertEquals("before update", note.getNoteTitle());
		Assertions.assertEquals(noteId, note.getNoteId());

		unarchiveNote.setNoteContent("user1 updated unarchived note");
		unarchiveNote.setNoteTitle("after update");
		noteService.updateNote(unarchiveNote);

		Note fetchNote = noteService.findNoteById(unarchiveNote);

		Assertions.assertEquals("user1 updated unarchived note", fetchNote.getNoteContent());
        Assertions.assertEquals("after update", fetchNote.getNoteTitle());
		Assertions.assertEquals(noteId, fetchNote.getNoteId());

		fetchNote.setNoteTitle("this is additional test");
		noteService.updateNote(fetchNote);

		// from this assertions, we can clarify that note content doesnot change when update only note title
        fetchNote = noteService.findNoteById(unarchiveNote);
        Assertions.assertEquals("user1 updated unarchived note", fetchNote.getNoteContent());
        Assertions.assertEquals("this is additional test", fetchNote.getNoteTitle());
        Assertions.assertEquals(noteId, fetchNote.getNoteId());

        fetchNote.setNoteContent("this also changed");
        noteService.updateNote(fetchNote);
        fetchNote = noteService.findNoteById(unarchiveNote);

        // from this assertions, we can clarify that note title doesnot change when update only note content
        Assertions.assertEquals("this also changed", fetchNote.getNoteContent());
        Assertions.assertEquals("this is additional test", fetchNote.getNoteTitle());
        Assertions.assertEquals(noteId, fetchNote.getNoteId());


	}

	@Test
	void noteContentCantBeEmpty() {
		Note unarchiveNote = new Note();
		unarchiveNote.setArchive(false);
		unarchiveNote.setNoteContent("");
		unarchiveNote.setNoteTitle("test");
		unarchiveNote.setUserId("user_1");

		Exception exception = Assertions.assertThrows(Exception.class, () -> {
			noteService.addNote(unarchiveNote);
		});
		Assertions.assertEquals(Util.ERROR_MESSAGE.EMPTY_NOTE_CONTENT, exception.getMessage());
	}

	@Test
	void noteContentCantBeExceed250Char() {
		String content = "1234567890";
		content = content.repeat(25);
		content += "1";

		Assertions.assertTrue(content.length() > Util.MAX_NOTE_LENGTH);

		Note unarchiveNote = new Note();
		unarchiveNote.setArchive(false);
		unarchiveNote.setNoteContent(content);
		unarchiveNote.setNoteTitle("test");
		unarchiveNote.setUserId("user_1");

		Exception exception = Assertions.assertThrows(Exception.class, () -> {
			noteService.addNote(unarchiveNote);
		});
		Assertions.assertEquals(Util.ERROR_MESSAGE.EXCEED_NOTE_SIZE, exception.getMessage());
	}

    @Test
    void noteTitleCantBeEmpty() {
        Note unarchiveNote = new Note();
        unarchiveNote.setArchive(false);
        unarchiveNote.setNoteContent("testing content");
        unarchiveNote.setNoteTitle("");
        unarchiveNote.setUserId("user_1");

        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            noteService.addNote(unarchiveNote);
        });
        Assertions.assertEquals(Util.ERROR_MESSAGE.EMPTY_NOTE_TITLE, exception.getMessage());
    }

    @Test
    void noteTitleCantBeExceed50Char() {
        String title = "1234567890";
        title = title.repeat(5);
        title += "1";

        Assertions.assertTrue(title.length() > Util.MAX_NOTE_TITLE_LENGTH);

        Note unarchiveNote = new Note();
        unarchiveNote.setArchive(false);
        unarchiveNote.setNoteContent("test ");
        unarchiveNote.setNoteTitle(title);
        unarchiveNote.setUserId("user_1");

        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            noteService.addNote(unarchiveNote);
        });
        Assertions.assertEquals(Util.ERROR_MESSAGE.EXCEED_NOTE_TITLE_SIZE, exception.getMessage());
    }

	@Test
	void deleteNote() throws Exception {
		Note unarchiveNote = new Note();
		unarchiveNote.setArchive(false);
		unarchiveNote.setNoteContent("user1 unarchive note");
		unarchiveNote.setUserId("user_1");
		unarchiveNote.setNoteTitle("delete note");
		int noteId = noteService.addNote(unarchiveNote);

		Note fetchNote = noteService.findNoteById(unarchiveNote);
		Assertions.assertEquals(noteId, fetchNote.getNoteId());

		noteService.deleteNote(fetchNote);

        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            noteService.findNoteById(fetchNote);
        });
        Assertions.assertEquals(Util.ERROR_MESSAGE.NOTE_DOES_NOT_EXISTS, exception.getMessage());
	}

	private void addNotes() throws Exception {
		// unarchive note for user_2
		Note note1 = new Note();
		note1.setArchive(false);
		note1.setNoteContent("user2 unarchived note");
		note1.setUserId("user_2");
        note1.setNoteTitle("title filter1 test title");
		noteService.addNote(note1);

		// unarchive note for user_1
		Note note2 = new Note();
		note2.setArchive(false);
		note2.setNoteContent("user1 unarchived note");
		note2.setUserId("user_1");
        note2.setNoteTitle("title filter1 test title");
		noteService.addNote(note2);

		// archive note for user_1
		Note note3 = new Note();
		note3.setArchive(true);
		note3.setNoteContent("user1 archived note");
		note3.setUserId("user_1");
        note3.setNoteTitle("title filter2 test title");
		noteService.addNote(note3);
	}
}
