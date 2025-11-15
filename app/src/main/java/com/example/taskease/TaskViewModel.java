package com.example.taskease;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

/**
 * ViewModel que actúa como intermediario entre la UI (Vistas) y la capa de datos (Repositorio).
 * Sobrevive a los cambios de configuración (como rotaciones) y mantiene los datos de la UI.
 */
public class TaskViewModel extends AndroidViewModel {
    // Referencia al Repositorio, que es la única fuente de datos.
    private final TaskRepository mRepository;
    // LiveData que contiene la lista de todas las tareas. La UI observará este objeto.
    private final LiveData<List<Task>> mAllTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        // Crea una instancia del Repositorio.
        mRepository = new TaskRepository(application);
        // Obtiene la lista de tareas del Repositorio.
        mAllTasks = mRepository.getAllTasks();
    }

    /**
     * Expone la lista de tareas como LiveData para que la UI la observe.
     * La UI se actualizará automáticamente cuando los datos cambien.
     * @return Un LiveData que contiene la lista de todas las tareas.
     */
    public LiveData<List<Task>> getAllTasks() {
        return mAllTasks;
    }

    /**
     * Delega la operación de inserción al Repositorio.
     * La UI llama a este método para crear una nueva tarea.
     * @param task La tarea a insertar.
     */
    public void insert(Task task) {
        mRepository.insert(task);
    }

    /**
     * Delega la operación de actualización al Repositorio.
     * La UI llama a este método para modificar una tarea existente.
     * @param task La tarea a actualizar.
     */
    public void update(Task task) {
        mRepository.update(task);
    }

    /**
     * Delega la operación de eliminación al Repositorio.
     * La UI llama a este método para borrar una tarea.
     * @param task La tarea a eliminar.
     */
    public void delete(Task task) {
        mRepository.delete(task);
    }
}
