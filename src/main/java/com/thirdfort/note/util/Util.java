package com.thirdfort.note.util;

public class Util {
    public static final Integer MAX_NOTE_LENGTH = 250;
    public static final Integer MAX_NOTE_TITLE_LENGTH = 50;

    public static class ERROR_MESSAGE {
        public static final String EMPTY_NOTE_CONTENT = "Empty note content was received";
        public static final String EMPTY_NOTE_TITLE = "Empty note title was received";
        public static final String EXCEED_NOTE_SIZE = "Exceeds maximum number of characters";
        public static final String EXCEED_NOTE_TITLE_SIZE = "Exceeds maximum number of characters note title";

        public static final String EMPTY_NOTE_ID = "Note Id cannot be empty in update";
        public static final String NOTE_DOES_NOT_EXISTS = "Note does not exists for given note id";
    }
}
