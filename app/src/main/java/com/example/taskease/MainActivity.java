package com.example.taskease;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private TaskViewModel mTaskViewModel;
    private TaskAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 1. Configurar el RecyclerView (RF-02)
        RecyclerView recyclerView = findViewById(R.id.recycler_view_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        mAdapter = new TaskAdapter();
        recyclerView.setAdapter(mAdapter);

        // 2. Configurar el ViewModel
        // Obtenemos el ViewModel
        mTaskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // 3. Observar el LiveData (RF-08: Actualización automática)
        // Esto conecta la UI a los datos.
        // Cada vez que los datos cambien en Room, esto se ejecutará.
        mTaskViewModel.getAllTasks().observe(this, tasks -> {
            // Actualiza la lista en el adapter
            mAdapter.submitList(tasks);
        });

        // 4. Configurar el Botón Flotante (FAB) (RF-03)
        FloatingActionButton fab = findViewById(R.id.fab_add_task);
        fab.setOnClickListener(view -> {
            // TODO: Iniciar la Actividad de Añadir/Editar Tarea
            // Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            // startActivity(intent);
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivity(intent);
        });

        // 5. Configurar Swipe-to-Delete (RF-05: Eliminación de tareas)
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Obtenemos la tarea y le decimos al ViewModel que la borre
                Task taskToDelete = mAdapter.getTaskAt(viewHolder.getAdapterPosition());
                mTaskViewModel.delete(taskToDelete);
                Toast.makeText(MainActivity.this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);


        // 6. Configurar los Clicks del Adapter (RF-04 y RF-06)
        mAdapter.setOnTaskClickListener(new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onTaskClick(Task task) {
                Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);

                // Pasamos los datos de la tarea a la actividad de edición (RF-04)
                intent.putExtra(AddEditTaskActivity.EXTRA_ID, task.getId());
                intent.putExtra(AddEditTaskActivity.EXTRA_TITLE, task.getTitle());
                intent.putExtra(AddEditTaskActivity.EXTRA_DESCRIPTION, task.getDescription());
                intent.putExtra(AddEditTaskActivity.EXTRA_DATE, task.getDate());
                intent.putExtra(AddEditTaskActivity.EXTRA_COMPLETED, task.isCompleted());

                startActivity(intent); // Iniciar la actividad en modo "Editar"
            }

            @Override
            public void onTaskCheckedChanged(Task task) {
                // Actualiza la tarea en la base de datos (RF-06)
                mTaskViewModel.update(task);
            }
        });
    }
}