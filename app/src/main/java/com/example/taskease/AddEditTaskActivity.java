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

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class AddEditTaskActivity extends AppCompatActivity {

    // Constantes para pasar datos entre actividades de forma segura y consistente.
    public static final String EXTRA_ID = "com.example.taskease.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.taskease.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.taskease.EXTRA_DESCRIPTION";
    public static final String EXTRA_DATE = "com.example.taskease.EXTRA_DATE";
    public static final String EXTRA_COMPLETED = "com.example.taskease.EXTRA_COMPLETED";

    // Vistas de la interfaz de usuario.
    private TextInputEditText editTextTitle;
    private TextInputEditText editTextDescription;

    // ViewModel para interactuar con la capa de datos (Repositorio -> Room).
    private TaskViewModel taskViewModel;

    // Variables para gestionar el estado: si es una tarea nueva (-1) o una edición (ID de la tarea).
    private int currentTaskId = -1;
    private long originalTaskDate = 0;
    private boolean originalTaskCompleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicializa la actividad y establece su layout.
        setContentView(R.layout.activity_add_edit_task);

        // Referencia a los campos de texto del layout.
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);

        // Configura la Toolbar como la ActionBar de la actividad.
        Toolbar toolbar = findViewById(R.id.toolbar_add_edit);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            // Muestra el botón de "atrás" (flecha) en la Toolbar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Establece un ícono personalizado para el botón de "atrás".
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        }

        // Obtiene una instancia del ViewModel, atada al ciclo de vida de esta actividad.
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Obtiene el Intent que inició esta actividad para comprobar si se pasaron datos.
        Intent intent = getIntent();
        // Comprueba si se pasó un ID. Si es así, estamos en "Modo Edición".
        if (intent.hasExtra(EXTRA_ID)) {
            // Cambia el título de la Toolbar para reflejar la acción de editar.
            setTitle("Editar Tarea");
            // Obtiene el ID de la tarea a editar.
            currentTaskId = intent.getIntExtra(EXTRA_ID, -1);
            // Rellena los campos de la UI con los datos de la tarea que se está editando.
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));

            // Guarda los datos originales que no se editan en esta pantalla para no perderlos al actualizar.
            originalTaskDate = intent.getLongExtra(EXTRA_DATE, System.currentTimeMillis());
            originalTaskCompleted = intent.getBooleanExtra(EXTRA_COMPLETED, false);
        } else {
            // Si no se pasó un ID, estamos en "Modo Añadir".
            setTitle("Añadir Tarea");
        }
    }

    /**
     * Guarda o actualiza una tarea en la base de datos.
     */
    private void saveTask() {
        // Obtiene el texto de los campos de entrada y elimina espacios en blanco al inicio y al final.
        String title = Objects.requireNonNull(editTextTitle.getText()).toString().trim();
        String description = Objects.requireNonNull(editTextDescription.getText()).toString().trim();

        // Validación: El título no puede estar vacío.
        if (title.isEmpty()) {
            Toast.makeText(this, "Por favor, introduce un título", Toast.LENGTH_SHORT).show();
            return; // Detiene la ejecución del método si la validación falla.
        }

        Task task;
        // Si el ID es -1, significa que es una tarea nueva.
        if (currentTaskId == -1) {
            // Crea un nuevo objeto Task con los datos introducidos y la fecha actual.
            task = new Task(title, description, System.currentTimeMillis(), false);
            // Llama al ViewModel para que inserte la nueva tarea en la base de datos.
            taskViewModel.insert(task);
            Toast.makeText(this, "Tarea guardada", Toast.LENGTH_SHORT).show();
        } else {
            // Si el ID es diferente de -1, es una tarea existente que hay que actualizar.
            // Crea un objeto Task con los datos modificados y los datos originales que no se editan.
            task = new Task(title, description, originalTaskDate, originalTaskCompleted);
            // ¡Importante! Establece el ID en el objeto para que Room sepa qué fila actualizar.
            task.setId(currentTaskId);
            // Llama al ViewModel para que actualice la tarea.
            taskViewModel.update(task);
            Toast.makeText(this, "Tarea actualizada", Toast.LENGTH_SHORT).show();
        }

        // Cierra la actividad y regresa a la pantalla principal (MainActivity).
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú de la Toolbar (en este caso, solo contiene el botón de guardar).
        getMenuInflater().inflate(R.menu.add_edit_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Gestiona los clics en los elementos del menú de la Toolbar.
        int itemId = item.getItemId();
        if (itemId == R.id.save_task) {
            // Si el usuario pulsa "Guardar", llama al método correspondiente.
            saveTask();
            return true;
        } else if (itemId == android.R.id.home) {
            // Si el usuario pulsa el botón de "atrás" de la Toolbar, cierra la actividad.
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
