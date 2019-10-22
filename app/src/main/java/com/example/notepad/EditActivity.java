/**
 * A note without a title is not allowed to be saved (even if there is note text).
 * If such an attempt is made simply exit the activity without saving and show a
 * Toast message indicating that the un-titled activity was not saved.
 */

package com.example.notepad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "EditActivity";

    private EditText editTitleText;
    private EditText editContentText;
    private String origTitle;
    private String origContent;
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
            origTitle = t;
            if (t != null)
                editTitleText.setText(note.getNoteTitle());
            String c = note.getNoteContent();
            origContent = c;
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
        long update = getUpdateTime();

        if (title.isEmpty()) {
            Log.d(TAG, "updateNote: title.isEmpty - NO SAVE");
            Toast.makeText(this, "Un-titled note was not saved.", Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            if (note == null) {
                note = new Note(title, content, update);
            } else {
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
    }

    private long getUpdateTime() {
        long res = System.currentTimeMillis();
        return res;
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

        // if note hasn't been changed, just finish()
        String newTitle = editTitleText.getText().toString();
        String newContent = editContentText.getText().toString();
        if (newTitle.equals(origTitle) && newContent.equals(origContent)) {
            finish();
        }
        else if (newTitle.isEmpty()) {
            Log.d(TAG, "updateNote: title.isEmpty - NO SAVE");
            Toast.makeText(this, "Un-titled note was not saved.", Toast.LENGTH_LONG).show();
            finish();
        }

        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Wait!");
            builder.setMessage("Do you want to save your note?");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    updateNote();
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

}
