package com.example.taskease;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.activity.EdgeToEdge;

import com.google.android.material.textfield.TextInputEditText;

public class AddEditTaskActivity extends AppCompatActivity {

    // Constantes para pasar datos entre Activities
    public static final String EXTRA_ID = "com.tudominio.taskease.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.tudominio.taskease.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.tudominio.taskease.EXTRA_DESCRIPTION";
    public static final String EXTRA_DATE = "com.tudominio.taskease.EXTRA_DATE";
    public static final String EXTRA_COMPLETED = "com.tudominio.taskease.EXTRA_COMPLETED";

    private TextInputEditText editTextTitle;
    private TextInputEditText editTextDescription;
    private TaskViewModel taskViewModel;

    private int currentTaskId = -1; // -1 indica "Añadir Tarea", cualquier otro valor es "Editar Tarea"
    private long originalTaskDate = 0;
    private boolean originalTaskCompleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_task);

        // Configurar vistas
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_add_edit);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close); // Necesitarás un icono 'ic_close'
        }

        // Obtener el ViewModel
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Comprobar si estamos en modo "Añadir" o "Editar"
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            // ----- MODO EDITAR -----
            setTitle("Editar Tarea");
            currentTaskId = intent.getIntExtra(EXTRA_ID, -1);

            // Rellenar campos con datos existentes
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));

            // Guardar datos originales que no se editan en esta pantalla
            originalTaskDate = intent.getLongExtra(EXTRA_DATE, System.currentTimeMillis());
            originalTaskCompleted = intent.getBooleanExtra(EXTRA_COMPLETED, false);

        } else {
            // ----- MODO AÑADIR -----
            setTitle("Añadir Tarea");
        }
    }

    /**
     * Guarda la tarea (inserta una nueva o actualiza una existente)
     */
    private void saveTask() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        // Validación simple (RF-03 requiere un título)
        if (title.trim().isEmpty()) {
            Toast.makeText(this, "Por favor, introduce un título", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentTaskId == -1) {
            // ----- Guardar NUEVA Tarea -----
            long currentDate = System.currentTimeMillis();
            Task task = new Task(title, description, currentDate, false);
            taskViewModel.insert(task);
            Toast.makeText(this, "Tarea guardada", Toast.LENGTH_SHORT).show();

        } else {
            // ----- Actualizar Tarea EXISTENTE -----
            // Creamos una tarea nueva con los datos actualizados y los originales
            Task task = new Task(title, description, originalTaskDate, originalTaskCompleted);
            task.setId(currentTaskId); // ¡MUY IMPORTANTE! Room necesita el ID para saber qué fila actualizar
            taskViewModel.update(task);
            Toast.makeText(this, "Tarea actualizada", Toast.LENGTH_SHORT).show();
        }

        finish(); // Cierra esta actividad y vuelve a MainActivity
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_edit_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_task) {
            saveTask();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            finish(); // Cierra la actividad si pulsa la flecha "atrás"
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}