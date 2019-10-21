package com.example.notepad;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Note implements Serializable {

    private static final String TAG = "Note";
    
    private String noteTitle;
    private String noteContent;
    private String noteDate;

    public Note(String noteTitle, String noteContent, String lastUpdateTime) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.noteDate = lastUpdateTime;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public String getLastUpdateTime() {
        return noteDate;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.noteDate = lastUpdateTime;
    }

    @NonNull
    @Override
    public String toString() {
        return this.noteTitle + ": " + this.noteContent + ": " + this.noteDate;
    }
}
