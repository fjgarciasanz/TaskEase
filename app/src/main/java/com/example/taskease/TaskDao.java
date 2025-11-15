package com.example.taskease;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * DAO (Data Access Object) para la tabla de tareas.
 * Define los métodos para interactuar con la base de datos.
 * Room se encarga de generar la implementación de esta interfaz.
 */
@Dao
public interface TaskDao {

    /**
     * Inserta una tarea en la base de datos.
     * @param task La tarea a insertar.
     */
    @Insert
    void insert(Task task);

    /**
     * Actualiza una tarea existente en la base de datos.
     * @param task La tarea con los datos actualizados.
     */
    @Update
    void update(Task task);

    /**
     * Elimina una tarea de la base de datos.
     * @param task La tarea a eliminar.
     */
    @Delete
    void delete(Task task);

    /**
     * Obtiene todas las tareas de la tabla, ordenadas por fecha de forma descendente.
     * Devuelve un objeto LiveData, lo que permite que la UI se actualice automáticamente
     * cuando los datos cambian.
     * @return Un LiveData que contiene la lista de todas las tareas.
     */
    @Query("SELECT * FROM task_table ORDER BY date DESC")
    LiveData<List<Task>> getAllTasks();

    /**
     * Elimina todas las tareas de la tabla.
     * (Este método no se usa actualmente en la app, pero es un buen ejemplo).
     */
    @Query("DELETE FROM task_table")
    void deleteAllTasks();
}
