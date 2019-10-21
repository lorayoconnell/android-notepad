package com.example.notepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainActivity";

    private static final int CODE_EDITNOTE_ACTIVITY = 10;

    private ArrayList<Note> noteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private int currNote = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        noteAdapter = new NoteAdapter(noteList, this);

        recyclerView.setAdapter(noteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        makeList();
        updateTitle();
    }

    private void makeList() {
        // to have some default notes loaded
        // maybe a 'welcome to notes' object with some information?
        Note n = new Note("Welcome to Notes", "Some content that will be added to the note.", "right now");
        noteList.add(n);
        Note n2 = new Note("Another Title", "Some other content", "yesterday");
        noteList.add(n2);
    }

    private void updateTitle() {
        setTitle("Notes (" + noteList.size() + ")");
    }

    public void testClick() {
        Toast.makeText(this, "Hi", Toast.LENGTH_LONG).show();
    }

    public void openAboutActivity() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void openEditActivity() {
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }

    public void openNoteInEditActivity(View view, int pos) {
        Note selectedNote = noteList.get(pos);
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        intent.putExtra("note", selectedNote);
        startActivityForResult(intent, CODE_EDITNOTE_ACTIVITY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addNote:
                // open new activity to add a new note
                openEditActivity();
                break;
            case R.id.about:
                openAboutActivity();
                break;
            default:
                break; // should never be called...
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: ");
        int pos = recyclerView.getChildLayoutPosition(view);
        currNote = pos;
        openNoteInEditActivity(view, pos);
    }

    /**
     * Removes the selected note on LongClick
     * and redraws the updated list
     * @param view
     * @return true
     */
    @Override
    public boolean onLongClick(View view) {
        Log.d(TAG, "onLongClick: ");
        int pos = recyclerView.getChildLayoutPosition(view);
        // confirm delete dialog
        noteList.remove(pos);
        updateTitle();
        noteAdapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public String getCurrentTime() {
        // system.printmillis() then convert to a real timestamp
        // will talk about in lecture 5?
        return "time";
    }

    // auto-save when onpause

    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_EDITNOTE_ACTIVITY) {
            if (resultCode == RESULT_OK) {

                Note note = (Note) data.getSerializableExtra("input");
                String noteTitle = note.getNoteTitle();
                String noteContent = note.getNoteContent();

                Note n = noteList.get(currNote);
                n.setNoteTitle(noteTitle);
                n.setNoteContent(noteContent);


                Log.d(TAG, "onActivityResult: noteTitle: " + noteTitle + " noteList[i]: ");

                noteAdapter.notifyDataSetChanged();
            }
        }
    }


}
