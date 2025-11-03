package com.example.taskease;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface TaskDao {
    // RF-03: Creación de tareas
    @Insert
    void insert(Task task);

    // RF-04: Edición de tareas
    @Update
    void update(Task task);

    // RF-05: Eliminación de tareas
    @Delete
    void delete(Task task);

    // RF-02: Visualización de tareas
    // Usamos LiveData para que la lista se actualice sola (MVVM)
    // Ordenamos por fecha (puedes cambiar 'DESC' a 'ASC')
    @Query("SELECT * FROM task_table ORDER BY date DESC")
    LiveData<List<Task>> getAllTasks();

    // Query de ejemplo si quisieras borrar todas las tareas
    @Query("DELETE FROM task_table")
    void deleteAllTasks();
}
