package com.example.taskease;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

/**
 * ViewModel para gestionar la lógica de negocio y los datos de las tareas.
 * Se adhiere al patrón MVVM (RNF-03).
 */
public class TaskViewModel extends AndroidViewModel {
    private TaskRepository mRepository;
    private final LiveData<List<Task>> mAllTasks;

    // Constructor
    public TaskViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TaskRepository(application);
        mAllTasks = mRepository.getAllTasks();
    }

    // --- API pública para la Vista (MainActivity) ---

    /**
     * Proporciona la lista de tareas a la UI (Vista).
     * (RF-02: Visualización de tareas)
     * @return LiveData<List<Task>>
     */
    public LiveData<List<Task>> getAllTasks() {
        return mAllTasks;
    }

    /**
     * Pasa la solicitud de inserción al Repositorio.
     * @param task La tarea a insertar.
     */
    public void insert(Task task) {
        mRepository.insert(task);
    }

    /**
     * Pasa la solicitud de actualización al Repositorio.
     * @param task La tarea a actualizar.
     */
    public void update(Task task) {
        mRepository.update(task);
    }

    /**
     * Pasa la solicitud de eliminación al Repositorio.
     * @param task La tarea a eliminar.
     */
    public void delete(Task task) {
        mRepository.delete(task);
    }
}
