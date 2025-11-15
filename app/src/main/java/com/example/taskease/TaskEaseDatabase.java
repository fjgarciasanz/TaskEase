package com.example.taskease;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Clase principal de la base de datos de la aplicación, gestionada por Room.
 * Define la configuración de la base de datos y sirve como el punto de acceso principal a los datos.
 */
@Database(entities = {Task.class}, version = 1, exportSchema = false)
public abstract class TaskEaseDatabase extends RoomDatabase {

    // Método abstracto que Room implementará para devolver una instancia del DAO.
    public abstract TaskDao taskDao();

    // Instancia Singleton de la base de datos para evitar tener múltiples instancias abiertas al mismo tiempo.
    // 'volatile' asegura que la variable INSTANCE sea siempre leída desde la memoria principal.
    private static volatile TaskEaseDatabase INSTANCE;

    /**
     * Método estático que devuelve la instancia única (Singleton) de la base de datos.
     * Utiliza un enfoque de "double-checked locking" para ser seguro en entornos multihilo.
     * @param context El contexto de la aplicación.
     * @return La instancia Singleton de TaskEaseDatabase.
     */
    public static TaskEaseDatabase getDatabase(final Context context) {
        // Si la instancia no ha sido creada todavía, entra en el bloque sincronizado.
        if (INSTANCE == null) {
            synchronized (TaskEaseDatabase.class) {
                // Vuelve a comprobar dentro del bloque por si otro hilo la creó mientras esperaba.
                if (INSTANCE == null) {
                    // Crea la base de datos utilizando el constructor de Room.
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TaskEaseDatabase.class, "taskease_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
