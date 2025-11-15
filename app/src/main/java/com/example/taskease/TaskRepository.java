package com.example.taskease;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repositorio que actúa como una capa de abstracción entre el ViewModel y las fuentes de datos.
 * Es el único punto de acceso a los datos de la aplicación (Principio de Única Fuente de Verdad).
 */
public class TaskRepository {
    // Referencia al DAO (Data Access Object) para interactuar con la base de datos.
    private final TaskDao mTaskDao;
    // LiveData que contiene la lista de todas las tareas.
    private final LiveData<List<Task>> mAllTasks;

    // ExecutorService para ejecutar operaciones de base de datos en un hilo secundario.
    // Esto es crucial para no bloquear el hilo principal (UI).
    private static final ExecutorService databaseWriteExecutor =
            Executors.newSingleThreadExecutor();

    public TaskRepository(Application application) {
        // Obtiene una instancia de la base de datos (Singleton).
        TaskEaseDatabase db = TaskEaseDatabase.getDatabase(application);
        // Obtiene el DAO de la base de datos.
        mTaskDao = db.taskDao();
        // Obtiene la lista de tareas del DAO. Room gestiona la actualización de este LiveData.
        mAllTasks = mTaskDao.getAllTasks();
    }

    /**
     * Devuelve todas las tareas como LiveData.
     * El ViewModel observará este LiveData.
     * @return Un LiveData que contiene la lista de todas las tareas.
     */
    public LiveData<List<Task>> getAllTasks() {
        return mAllTasks;
    }

    /**
     * Inserta una tarea en la base de datos en un hilo secundario.
     * @param task La tarea a insertar.
     */
    public void insert(Task task) {
        databaseWriteExecutor.execute(() -> {
            mTaskDao.insert(task);
        });
    }

    /**
     * Actualiza una tarea en la base de datos en un hilo secundario.
     * @param task La tarea a actualizar.
     */
    public void update(Task task) {
        databaseWriteExecutor.execute(() -> {
            mTaskDao.update(task);
        });
    }

    /**
     * Elimina una tarea de la base de datos en un hilo secundario.
     * @param task La tarea a eliminar.
     */
    public void delete(Task task) {
        databaseWriteExecutor.execute(() -> {
            mTaskDao.delete(task);
        });
    }
}
