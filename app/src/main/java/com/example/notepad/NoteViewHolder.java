package com.example.notepad;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    TextView titleText;
    TextView contentText;
    TextView timeText;

    NoteViewHolder(View view) {
        super(view);
        titleText = view.findViewById(R.id.titleText);
        contentText = view.findViewById(R.id.contentText);
        timeText = view.findViewById(R.id.timeText);
    }



}

