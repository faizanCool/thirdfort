package com.thirdfort.note.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity @Table
@Getter @Setter
public class Note {

    @Id @Column(name = "note_id")
    @GeneratedValue
    private Integer noteId;

    @Column(name = "is_archive")
    private boolean archive;

    @Column(name = "note_content")
    private String noteContent;

    @Column(name = "user_id")
    private String userId;
}
