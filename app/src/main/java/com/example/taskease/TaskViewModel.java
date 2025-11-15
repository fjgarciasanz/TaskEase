package com.example.taskease;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;

/**
 * ViewModel que actúa como intermediario entre la UI (Vistas) y la capa de datos (Repositorio).
 * Hereda de ViewModel, por lo que no tiene dependencias del framework de Android.
 */
public class TaskViewModel extends ViewModel {
    private final TaskRepository mRepository;
    private final LiveData<List<Task>> mAllTasks;

    /**
     * Constructor único que recibe sus dependencias (Inyección de Dependencias).
     * @param repository El repositorio que se usará para acceder a los datos.
     */
    public TaskViewModel(@NonNull TaskRepository repository) {
        mRepository = repository;
        mAllTasks = mRepository.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return mAllTasks;
    }

    public void insert(Task task) {
        mRepository.insert(task);
    }

    public void update(Task task) {
        mRepository.update(task);
    }

    public void delete(Task task) {
        mRepository.delete(task);
    }
}
