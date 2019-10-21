package com.example.notepad;

import java.io.Serializable;

public class Note implements Serializable {

    private String noteTitle;
    private String noteContent;
    private String lastUpdateTime;

    public Note(String noteTitle, String noteContent, String lastUpdateTime) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
