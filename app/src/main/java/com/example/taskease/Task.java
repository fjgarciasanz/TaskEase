package com.example.taskease;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Clase que representa el modelo de datos para una Tarea.
 * Esta es una entidad de Room, lo que significa que se mapeará a una tabla en la base de datos.
 */
@Entity(tableName = "task_table")
public class Task {

    // Clave primaria de la tabla. Se genera automáticamente.
    @PrimaryKey(autoGenerate = true)
    private int id;

    // Título de la tarea.
    private String title;

    // Descripción opcional de la tarea.
    private String description;

    // Fecha de creación de la tarea, almacenada como un timestamp (long).
    // Esto facilita la ordenación.
    private long date;

    // Estado de la tarea: true si está completada, false si está pendiente.
    private boolean completed;

    /**
     * Constructor de la clase Task.
     * El 'id' no se incluye aquí porque Room se encarga de generarlo automáticamente.
     */
    public Task(String title, String description, long date, boolean completed) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.completed = completed;
    }

    // --- Getters y Setters ---
    // Room necesita estos métodos para crear y manipular los objetos Task.

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
