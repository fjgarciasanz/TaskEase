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

    private TaskViewModel mTaskViewModel;
    private TaskAdapter mAdapter;
    private View mEmptyStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        mEmptyStateView = findViewById(R.id.empty_state_view);

        mAdapter = new TaskAdapter();
        recyclerView.setAdapter(mAdapter);

        mTaskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        mTaskViewModel.getAllTasks().observe(this, tasks -> {
            mAdapter.submitList(tasks);
            if (tasks.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                mEmptyStateView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                mEmptyStateView.setVisibility(View.GONE);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab_add_task);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivity(intent);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Task taskToDelete = mAdapter.getTaskAt(position);
                    mTaskViewModel.delete(taskToDelete);
                    Toast.makeText(MainActivity.this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(recyclerView);

        mAdapter.setOnTaskClickListener(new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onTaskClick(Task task) {
                Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
                intent.putExtra(AddEditTaskActivity.EXTRA_ID, task.getId());
                intent.putExtra(AddEditTaskActivity.EXTRA_TITLE, task.getTitle());
                intent.putExtra(AddEditTaskActivity.EXTRA_DESCRIPTION, task.getDescription());
                intent.putExtra(AddEditTaskActivity.EXTRA_DATE, task.getDate());
                intent.putExtra(AddEditTaskActivity.EXTRA_COMPLETED, task.isCompleted());
                startActivity(intent);
            }

            @Override
            public void onTaskCheckedChanged(Task task) {
                mTaskViewModel.update(task);
            }
        });
    }
}
