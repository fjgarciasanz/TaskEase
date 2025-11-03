package com.example.taskease;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {Task.class}, version = 1, exportSchema = false)
public abstract class TaskEaseDatabase extends RoomDatabase {

    // El 'abstract' para que Room implemente el DAO
    public abstract TaskDao taskDao();

    // Singleton para evitar múltiples instancias de la base de datos
    private static volatile TaskEaseDatabase INSTANCE;

    public static TaskEaseDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskEaseDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TaskEaseDatabase.class, "taskease_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
