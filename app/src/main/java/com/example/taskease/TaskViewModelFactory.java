package com.example.taskease;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Fábrica para crear instancias de TaskViewModel con un constructor que requiere dependencias.
 */
public class TaskViewModelFactory implements ViewModelProvider.Factory {
    private final TaskRepository repository;

    public TaskViewModelFactory(TaskRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TaskViewModel.class)) {
            return (T) new TaskViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
