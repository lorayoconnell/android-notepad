package com.example.notepad;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note implements Comparable, Serializable {

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





    @Override
    public int compareTo(Object o) {

        Date d1 = new Date();
        Date d2 = new Date();

        Note n = (Note) o;

        try {
            d1 = strToDate(n.getLastUpdateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            d2 = strToDate(this.getLastUpdateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (d1.getTime() < d2.getTime()) return -1;
        else if (d1.getTime() > d2.getTime()) return 1;

        return 0;
    }

    public static Date strToDate(String d) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d, h:mm a");
        Date date = sdf.parse(d);
        return date;
    }

}