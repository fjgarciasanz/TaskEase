package com.example.taskease;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    // ViewModel para interactuar con la capa de datos.
    private TaskViewModel mTaskViewModel;
    // Adaptador para el RecyclerView, que gestiona la visualización de la lista de tareas.
    private TaskAdapter mAdapter;
    // Vista que se muestra cuando la lista de tareas está vacía.
    private View mEmptyStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Configuración de la Toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        // 2. Configuración del RecyclerView.
        RecyclerView recyclerView = findViewById(R.id.recycler_view_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true); // Optimización: el tamaño del RecyclerView no cambia.

        // 3. Referencia a la vista de estado vacío.
        mEmptyStateView = findViewById(R.id.empty_state_view);

        // 4. Creación y configuración del adaptado.r
        mAdapter = new TaskAdapter();
        recyclerView.setAdapter(mAdapter);

        // 5. Obtención del ViewModel.
        mTaskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // 6. Observación de los datos (LiveData).
        // Cada vez que los datos en la base de datos cambian, este bloque se ejecuta.
        mTaskViewModel.getAllTasks().observe(this, tasks -> {
            // Envía la nueva lista de tareas al adaptador.
            mAdapter.submitList(tasks);

            // Comprueba si la lista está vacía para mostrar el estado vacío o el RecyclerView.
            if (tasks.isEmpty()) {
                recyclerView.setVisibility(View.GONE); // Oculta la lista.
                mEmptyStateView.setVisibility(View.VISIBLE); // Muestra el mensaje de estado vacío.
            } else {
                recyclerView.setVisibility(View.VISIBLE); // Muestra la lista.
                mEmptyStateView.setVisibility(View.GONE); // Oculta el mensaje de estado vacío.
            }
        });

        // 7. Configuración del Botón de Acción Flotante (FAB) para añadir tareas.
        FloatingActionButton fab = findViewById(R.id.fab_add_task);
        fab.setOnClickListener(view -> {
            // Inicia la actividad AddEditTaskActivity sin pasarle datos (modo "Añadir").
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivity(intent);
        });

        // 8. Configuración del gesto de deslizar para eliminar (Swipe-to-Delete).
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // No se permite mover elementos (reordenar).
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Obtiene la posición del elemento deslizado.
                int position = viewHolder.getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Obtiene la tarea en esa posición.
                    Task taskToDelete = mAdapter.getTaskAt(position);
                    // Llama al ViewModel para eliminar la tarea.
                    mTaskViewModel.delete(taskToDelete);
                    // Muestra un mensaje de confirmación.
                    Toast.makeText(MainActivity.this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(recyclerView); // Asocia el ItemTouchHelper al RecyclerView.

        // 9. Configuración de los clics en los elementos del RecyclerView.
        mAdapter.setOnTaskClickListener(new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onTaskClick(Task task) {
                // Al hacer clic en una tarea, se abre AddEditTaskActivity en modo "Editar".
                Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
                // Se pasan los datos de la tarea a la actividad de edición.
                intent.putExtra(AddEditTaskActivity.EXTRA_ID, task.getId());
                intent.putExtra(AddEditTaskActivity.EXTRA_TITLE, task.getTitle());
                intent.putExtra(AddEditTaskActivity.EXTRA_DESCRIPTION, task.getDescription());
                intent.putExtra(AddEditTaskActivity.EXTRA_DATE, task.getDate());
                intent.putExtra(AddEditTaskActivity.EXTRA_COMPLETED, task.isCompleted());
                startActivity(intent);
            }

            @Override
            public void onTaskCheckedChanged(Task task) {
                // Cuando se marca o desmarca el checkbox, se actualiza la tarea en la base de datos.
                mTaskViewModel.update(task);
            }
        });
    }
}
