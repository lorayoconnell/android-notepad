package com.example.notepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "EditActivity";

    private EditText editTitleText;
    private EditText editContentText;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editTitleText = findViewById(R.id.editTitleText);
        editContentText = findViewById(R.id.editContentText);

        Intent intent = getIntent();

        if (intent.hasExtra("note")) {
            note = (Note) intent.getSerializableExtra("note");

            String t = note.getNoteTitle();
            if (t != null)
                editTitleText.setText(note.getNoteTitle());
            String c = note.getNoteContent();
            if (c != null)
                editContentText.setText(note.getNoteContent());
        }
        else {
            editTitleText.setText("");
            editContentText.setText("");
        }
    }

    private void updateNote() {
        Intent data = new Intent();

        String title = editTitleText.getText().toString();
        String content = editContentText.getText().toString();
        String update = "date & time";

        if (note == null) {
            Log.d(TAG, "updateNote: Note object does not yet exist.");
            note = new Note(title, content, update);
        }
        else {
            Log.d(TAG, "updateNote: Updating an existing note.");
            note.setNoteTitle(title);
            note.setNoteContent(content);
            note.setLastUpdateTime(update);
        }

        if (note == null) {
            Log.d(TAG, "updateNote: ERROR - note should not be null at this point");
        }


        data.putExtra("input", note);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveNote:
                updateNote();
                break;
            default:
                break; // should never be called...
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        Intent data = new Intent();
        // updateNote();




       /*
        Log.d(TAG, "updateNote: newTitle: " + editTitleText.getText().toString());
        //Intent data = new Intent();

        String title = editTitleText.getText().toString();
        String content = editContentText.getText().toString();
        String update = "date&time";

        note = new Note(title,content,update);

        if (note == null) {
            //Log.d(TAG, "updateNote: note object doesn't exist");
            note = new Note(title,content,update);
        }
        else {
            //Log.d(TAG, "updateNote: updating an old note");
            note.setNoteTitle(title);
            note.setNoteContent(content);
            note.setLastUpdateTime(update);
        }

        //data.putExtra("input", note);
        //setResult(RESULT_OK, data);
        //Log.d(TAG, "updateNote: Time to send back results");
        //finish();
*/





    //    data.putExtra("input", note);
    //    setResult(RESULT_OK, data);
        super.onBackPressed();
    }

}
