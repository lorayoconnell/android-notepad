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

        if (intent != null) {
            note = (Note) intent.getSerializableExtra("note");

            editTitleText.setText(note.getNoteTitle());
            editContentText.setText(note.getNoteContent());
        }


    }

    private void updateNote() {
        Log.d(TAG, "updateNote: newTitle: " + editTitleText.getText().toString());
        Intent data = new Intent();
        note.setNoteTitle(editTitleText.getText().toString());
        note.setNoteContent(editContentText.getText().toString());
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
        updateNote();
        data.putExtra("input", note);
        setResult(RESULT_OK, data);
        super.onBackPressed();
    }

}
