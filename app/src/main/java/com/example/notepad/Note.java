package com.example.notepad;

import androidx.annotation.NonNull;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note implements Comparable, Serializable {

    private String noteTitle;
    private String noteContent;
    private long noteDate;

    public Note(String noteTitle, String noteContent, long lastUpdateTime) {
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

    public long getLastUpdateTime() {
        return noteDate;
    }

    public String getLastUpdateTimeStr() {
        long m = getLastUpdateTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d, h:mm a");
        return sdf.format(m);
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.noteDate = lastUpdateTime;
    }

    @NonNull
    @Override
    public String toString() {
        return this.noteTitle + ": " + this.noteContent + ": " + this.noteDate;
    }

    @Override
    public int compareTo(Object o) {

        Date thisDate = new Date(getLastUpdateTime());
        Note thatNote = (Note)o;
        Date thatDate = new Date(thatNote.getLastUpdateTime());

        // if (thisDate == null || thatDate == null) return 0;

        return thisDate.compareTo(thatDate);
    }

}