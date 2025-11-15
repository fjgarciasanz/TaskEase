package com.example.taskease;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class AddEditTaskActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.taskease.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.taskease.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.taskease.EXTRA_DESCRIPTION";
    public static final String EXTRA_DATE = "com.example.taskease.EXTRA_DATE";
    public static final String EXTRA_COMPLETED = "com.example.taskease.EXTRA_COMPLETED";

    private TextInputEditText editTextTitle;
    private TextInputEditText editTextDescription;
    private TaskViewModel taskViewModel;

    private int currentTaskId = -1;
    private long originalTaskDate = 0;
    private boolean originalTaskCompleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        Toolbar toolbar = findViewById(R.id.toolbar_add_edit);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        }

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);

        Button buttonSave = findViewById(R.id.button_save);
        buttonSave.setOnClickListener(v -> saveTask());

        // --- ARREGLO ARQUITECTÓNICO CLAVE ---
        TaskRepository repository = new TaskRepository(getApplication());
        TaskViewModelFactory factory = new TaskViewModelFactory(repository);
        taskViewModel = new ViewModelProvider(this, factory).get(TaskViewModel.class);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Editar Tarea");
            currentTaskId = intent.getIntExtra(EXTRA_ID, -1);
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            originalTaskDate = intent.getLongExtra(EXTRA_DATE, System.currentTimeMillis());
            originalTaskCompleted = intent.getBooleanExtra(EXTRA_COMPLETED, false);
        } else {
            setTitle("Añadir Tarea");
        }
    }

    private void saveTask() {
        String title = Objects.requireNonNull(editTextTitle.getText()).toString().trim();
        String description = Objects.requireNonNull(editTextDescription.getText()).toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Por favor, introduce un título", Toast.LENGTH_SHORT).show();
            return;
        }

        Task task;
        if (currentTaskId == -1) {
            task = new Task(title, description, System.currentTimeMillis(), false);
            taskViewModel.insert(task);
            Toast.makeText(this, "Tarea guardada", Toast.LENGTH_SHORT).show();
        } else {
            task = new Task(title, description, originalTaskDate, originalTaskCompleted);
            task.setId(currentTaskId);
            taskViewModel.update(task);
            Toast.makeText(this, "Tarea actualizada", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
