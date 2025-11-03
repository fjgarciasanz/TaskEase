package com.example.taskease;
import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class TaskRepository {
    private TaskDao mTaskDao;
    private LiveData<List<Task>> mAllTasks;

    // ExecutorService para ejecutar tareas en un hilo de fondo.
    // Usamos un hilo para operaciones simples de base de datos.
    private static final ExecutorService databaseWriteExecutor =
            Executors.newSingleThreadExecutor();

    // Constructor
    public TaskRepository(Application application) {
        // Obtenemos la instancia de la base de datos
        TaskEaseDatabase db = TaskEaseDatabase.getDatabase(application);

        // Obtenemos el DAO de la base de datos
        mTaskDao = db.taskDao();

        // Obtenemos la lista de tareas (Room maneja esto automáticamente en un hilo de fondo)
        mAllTasks = mTaskDao.getAllTasks();
    }

    // --- Métodos de la API pública ---
    // El ViewModel usará estos métodos

    /**
     * Obtiene todas las tareas de la base de datos.
     * Room ejecuta esto en un hilo separado por defecto porque devuelve LiveData.
     * @return LiveData<List<Task>>
     */
    public LiveData<List<Task>> getAllTasks() {
        return mAllTasks;
    }

    /**
     * Inserta una nueva tarea en la base de datos.
     * Se ejecuta en un hilo de fondo usando el ExecutorService.
     * (RF-03: Creación de tareas)
     * @param task La tarea a insertar.
     */
    public void insert(Task task) {
        databaseWriteExecutor.execute(() -> {
            mTaskDao.insert(task);
        });
    }

    /**
     * Actualiza una tarea existente.
     * Se ejecuta en un hilo de fondo.
     * (RF-04: Edición de tareas)
     * @param task La tarea a actualizar.
     */
    public void update(Task task) {
        databaseWriteExecutor.execute(() -> {
            mTaskDao.update(task);
        });
    }

    /**
     * Elimina una tarea.
     * Se ejecuta en un hilo de fondo.
     * (RF-05: Eliminación de tareas)
     * @param task La tarea a eliminar.
     */
    public void delete(Task task) {
        databaseWriteExecutor.execute(() -> {
            mTaskDao.delete(task);
        });
    }
}
