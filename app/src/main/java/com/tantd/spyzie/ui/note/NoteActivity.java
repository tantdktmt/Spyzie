package com.tantd.spyzie.ui.note;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.tantd.spyzie.R;
import com.tantd.spyzie.core.ObjectBox;
import com.tantd.spyzie.data.db.Note;
import com.tantd.spyzie.data.db.Note_;
import com.tantd.spyzie.util.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.Query;

public class NoteActivity extends Activity {

    private EditText editText;
    private View addNoteButton;

    private Box<Note> notesBox;
    private Query<Note> notesQuery;
    private NotesAdapter notesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        setUpViews();

        notesBox = ObjectBox.get().boxFor(Note.class);

        // query all notes, sorted a-z by their text (https://docs.objectbox.io/queries)
        notesQuery = notesBox.query().order(Note_.text).build();
//        updateNotes();
    }

    private void updateNotes() {
        long startTime = System.currentTimeMillis();
        List<Note> notes = notesQuery.find();
        Log.d(Constants.LOG_TAG, "Fetch notes, spent time=" + (System.currentTimeMillis() - startTime) + " ms, count=" + notes.size());
        notesAdapter.setNotes(notes);
    }

    protected void setUpViews() {
        ListView listView = findViewById(R.id.listViewNotes);
        listView.setOnItemClickListener(noteClickListener);

        notesAdapter = new NotesAdapter();
        listView.setAdapter(notesAdapter);

        addNoteButton = findViewById(R.id.buttonAdd);
        addNoteButton.setEnabled(false);

        editText = findViewById(R.id.editTextNote);
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addNote();
                return true;
            }
            return false;
        });
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean enable = s.length() != 0;
                addNoteButton.setEnabled(enable);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public void onAddButtonClick(View view) {
        addNote();
    }

    public void onGetNoteButtonClicked(View view) {
        updateNotes();
    }

    public void onDeleteNotesButtonClicked(View view) {
        List<Note> notes = notesQuery.find();
        long startTime = System.currentTimeMillis();
        notesBox.remove(notes);
        Log.d(Constants.LOG_TAG, "Delete notes, spent time=" + (System.currentTimeMillis() - startTime) + " ms, count=" + notes.size());
        notesAdapter.setNotes(notes);
    }

    private void addNote() {
        String noteText = editText.getText().toString();
        editText.setText("");

        int COUNT = 50000;
        List<Note> notes = new ArrayList<>();
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Added on " + df.format(new Date());
        Date date = new Date();
        for (int i = 0; i < COUNT; i++) {
            notes.add(new Note(noteText + i, comment, date));
        }
        Log.d(Constants.LOG_TAG, "Add note, started");
        long startTime = System.currentTimeMillis();
        notesBox.put(notes);
        Log.d(Constants.LOG_TAG, "Add note, spent time=" + (System.currentTimeMillis() - startTime) + " ms");
    }

    OnItemClickListener noteClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Note note = notesAdapter.getItem(position);
            notesBox.remove(note);
            Log.d(Constants.LOG_TAG, "Deleted note, ID: " + note.getId());
            updateNotes();
        }
    };

}