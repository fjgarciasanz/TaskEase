package com.example.taskease;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_table")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String description;

    // Almacenaremos la fecha como un Long (timestamp)
    // Es más fácil de ordenar y manejar que un String
    private long date;

    // True si está completada, false si está pendiente
    private boolean completed;

    // Constructor
    // Nota: No incluimos el 'id' en el constructor
    // porque se genera automáticamente (autoGenerate = true)
    public Task(String title, String description, long date, boolean completed) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.completed = completed;
    }

    // --- Getters y Setters ---
    // Room necesita setters y getters para funcionar

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
