/**
 *
 * didn't add spacing in between list items
 * in list_note_item.xml
 *
 */


package com.example.notepad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

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

        // makeList();
        updateTitle();  // reflects current number of notes
    }

    //private void makeList() {
        // to have some default notes loaded
        //Note n = new Note("Welcome to Notes", "Caution: notes without titles will not be saved.", "init");
        //noteList.add(n);
    //}

    private void updateTitle() {
        setTitle("Notes (" + noteList.size() + ")");
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
     * @return true
     */
    @Override
    public boolean onLongClick(View view) {
        Log.d(TAG, "onLongClick: ");
        final int pos = recyclerView.getChildLayoutPosition(view);
        // confirm delete dialog


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Wait!");
        builder.setMessage("Are you sure you want to delete your note?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                noteList.remove(pos);
                updateTitle();
                noteAdapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == EDIT_NOTE_CODE || requestCode == NEW_NOTE_CODE) {
            if (resultCode == RESULT_OK) {
                Note note = (Note) data.getSerializableExtra("input");
                String title = note.getNoteTitle();
                String content = note.getNoteContent();

                long update = note.getLastUpdateTime();
                String upd = millisToStr(update);

                if (currNote == -1) {
                    noteList.add(note);
                }
                else {
                    Note n = noteList.get(currNote);
                    n.setNoteTitle(title);
                    n.setNoteContent(content);
                    n.setLastUpdateTime(update);
                }
            }
        }
        Collections.sort(noteList);
        Collections.reverse(noteList);

        noteAdapter.notifyDataSetChanged();
        updateTitle();
    }

    private String millisToStr(long m) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d, h:mm a");
        return sdf.format(m);
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

            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String nTitle = jsonObject.getString("noteTitle");
                String nContent = jsonObject.getString("noteContent");
                long nDate = jsonObject.getLong("noteDate");
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

    @Override
    protected void onPause() {
        try {
            saveNotes();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException | JSONException e) {
            Toast.makeText(this, "Not saved", Toast.LENGTH_SHORT).show();
        }
        super.onPause();
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

        fos.write(jsonText.getBytes());
        fos.close();
    }

}
