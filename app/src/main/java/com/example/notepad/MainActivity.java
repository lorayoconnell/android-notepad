package com.example.notepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainActivity";

    private static final int EDIT_NOTE_CODE = 10;
    private static final int NEW_NOTE_CODE = 11;

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

        loadFile();

        makeList();
        updateTitle();  // reflects current number of notes
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

    public void createNewNote() {        // openEditActivity
        Log.d(TAG, "createNewNote: CREATE NEW NOTE");
        currNote = -1;
        Intent intent = new Intent(this, EditActivity.class);
        startActivityForResult(intent, NEW_NOTE_CODE);
    }

    public void openNoteInEditActivity(View view, int pos) {
        Log.d(TAG, "openNoteInEditActivity: EDIT EXISTING NOTE");
        Note selectedNote = noteList.get(pos);
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("note", selectedNote);
        startActivityForResult(intent, EDIT_NOTE_CODE);
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
                createNewNote();
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

        if (requestCode == EDIT_NOTE_CODE) {
            Log.d(TAG, "onActivityResult: EDIT_NOTE_CODE");
        }
        else if (requestCode == NEW_NOTE_CODE) {
            Log.d(TAG, "onActivityResult: NEW_NOTE_CODE");
        }



        if (data.hasExtra("input")) {
            Note note = (Note) data.getSerializableExtra("input");

            if (note != null) {
                Log.d(TAG, "onActivityResult: note object is NOT null");

                String title = note.getNoteTitle();
                if (title != null) {
                    Log.d(TAG, "onActivityResult: title: " + title);

                    String content = note.getNoteContent();
                    if (content != null) {
                        Log.d(TAG, "onActivityResult: content: " + content);

                        String update = note.getLastUpdateTime();
                        if (update != null) {
                            Log.d(TAG, "onActivityResult: update: " + update);





                            if (currNote == -1) {
                                Log.d(TAG, "onActivityResult: This is a new note to be added");

                                noteList.add(note);


                            }
                            else {
                                Log.d(TAG, "onActivityResult: This is an existing note at index " + currNote);
                                Note n = noteList.get(currNote);
                                n.setNoteTitle(title);
                                n.setNoteContent(content);
                                n.setLastUpdateTime(update);

                            }




                        }
                        else Log.d(TAG, "onActivityResult: ERROR5");
                    }
                    else Log.d(TAG, "onActivityResult: ERROR4");
                }
                else Log.d(TAG, "onActivityResult: ERROR3");
            }
            else Log.d(TAG, "onActivityResult: ERROR2");
        }
        else Log.d(TAG, "onActivityResult: ERROR1");


        noteAdapter.notifyDataSetChanged();
        updateTitle();

    }

    private void showNotes() {

    }

    private void loadFile() {
        Log.d(TAG, "loadFile: LOADING JSON FILE");
        try {
            InputStream is = getApplicationContext().openFileInput("Notes.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();

            Log.d(TAG, "loadFile: JSON: " + sb.toString());

            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String nTitle = jsonObject.getString("noteTitle");
                String nContent = jsonObject.getString("noteContent");
                String nDate = jsonObject.getString("noteDate");
                Note n = new Note(nTitle, nContent, nDate);
                noteList.add(n);
            }
            showNotes();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "No JSON Note File Present", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void saveNotes() throws IOException, JSONException {
        Log.d(TAG, "saveNotes: SAVING JSON FILE");
        FileOutputStream fos = getApplicationContext().openFileOutput("Notes.json", Context.MODE_PRIVATE);
        JSONArray jsonArray = new JSONArray();
        for (Note n : noteList) {
            JSONObject noteJSON = new JSONObject();
            noteJSON.put("noteTitle", n.getNoteTitle());
            noteJSON.put("noteContent", n.getNoteContent());
            noteJSON.put("noteDate", n.getLastUpdateTime());
            jsonArray.put(noteJSON);
        }
        String jsonText = jsonArray.toString();
        Log.d(TAG, "saveNotes: JSON: " + jsonText);

        fos.write(jsonText.getBytes());
        fos.close();
    }


}
