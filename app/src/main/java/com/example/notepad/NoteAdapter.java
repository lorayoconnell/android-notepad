package com.example.notepad;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private static final String TAG = "NoteAdapter";
    private ArrayList<Note> noteList;
    private MainActivity mainActivity;

    NoteAdapter(ArrayList<Note> nList, MainActivity mainActivity) {
        noteList = nList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Log.d(TAG, "onCreateViewHolder: CREATING NEW");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_note_item, parent, false);
        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);

        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        //Log.d(TAG, "onBindViewHolder: SETTING ITEM DATA");
        Note selectedNote = noteList.get(position);    // get current note
        holder.titleText.setText(selectedNote.getNoteTitle());
        holder.contentText.setText(selectedNote.getNoteContent());
        holder.timeText.setText(selectedNote.getLastUpdateTime());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
