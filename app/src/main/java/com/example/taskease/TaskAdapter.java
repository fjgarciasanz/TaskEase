package com.example.taskease;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

// ListAdapter es una extensión de RecyclerView.Adapter para listas dinámicas, optimizado con DiffUtil.
public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskViewHolder> {
    // Interfaz para comunicar los clics del adaptador a la actividad (MainActivity).
    private OnTaskClickListener listener;

    public TaskAdapter() {
        super(DIFF_CALLBACK);
    }

    // DiffUtil ayuda a RecyclerView a calcular las diferencias entre dos listas (la antigua y la nueva)
    // y a realizar animaciones eficientes (solo actualiza los elementos que han cambiado).
    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            // Comprueba si dos elementos son el mismo (por su ID).
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            // Comprueba si el contenido de dos elementos es idéntico.
            return Objects.equals(oldItem.getTitle(), newItem.getTitle()) &&
                    Objects.equals(oldItem.getDescription(), newItem.getDescription()) &&
                    oldItem.getDate() == newItem.getDate() &&
                    oldItem.isCompleted() == newItem.isCompleted();
        }
    };

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Se llama cuando RecyclerView necesita crear un nuevo ViewHolder.
        // Infla el layout del item de la tarea.
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        // Se llama para mostrar los datos en una posición específica.
        // Vincula los datos de la tarea con las vistas del ViewHolder.
        holder.bind(getItem(position));
    }

    // Método para obtener una tarea en una posición específica, útil para el gesto de swipe.
    public Task getTaskAt(int position) {
        return getItem(position);
    }

    // ViewHolder: Contenedor para las vistas de un solo item de la lista.
    class TaskViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final TextView textViewDescription;
        private final CheckBox checkBoxCompleted;
        private final View statusBar;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            // Referencia a las vistas dentro del layout del item.
            textViewTitle = itemView.findViewById(R.id.textview_title);
            textViewDescription = itemView.findViewById(R.id.textview_description);
            checkBoxCompleted = itemView.findViewById(R.id.checkbox_completed);
            statusBar = itemView.findViewById(R.id.status_bar);

            // Listener para el CheckBox.
            checkBoxCompleted.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                // Asegurarse de que la posición es válida.
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    Task task = getItem(position);
                    // Crea una copia de la tarea con el estado de "completado" actualizado.
                    Task updatedTask = new Task(task.getTitle(), task.getDescription(), task.getDate(), checkBoxCompleted.isChecked());
                    updatedTask.setId(task.getId()); // Mantiene el ID original.
                    listener.onTaskCheckedChanged(updatedTask);
                }
            });

            // Listener para el clic en toda la tarjeta de la tarea.
            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onTaskClick(getItem(position));
                }
            });
        }

        // Método para vincular los datos de una tarea a las vistas de este ViewHolder.
        public void bind(Task task) {
            textViewTitle.setText(task.getTitle());
            textViewDescription.setText(formatDate(task.getDate()));
            checkBoxCompleted.setChecked(task.isCompleted());

            // Lógica para actualizar la apariencia en función del estado de la tarea.
            if (task.isCompleted()) {
                // Si está completada: tacha el texto y pone la barra de estado en verde.
                textViewTitle.setPaintFlags(textViewTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                statusBar.setBackgroundColor(Color.parseColor("#4CAF50")); // Verde
            } else {
                // Si está pendiente: quita el tachado y pone la barra en gris.
                textViewTitle.setPaintFlags(textViewTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                statusBar.setBackgroundColor(Color.parseColor("#757575")); // Gris
            }
        }
    }

    // Método de utilidad para formatear un timestamp (long) a una fecha legible (dd/MM/yyyy).
    private String formatDate(long timestamp) {
        if (timestamp == 0) return "Sin fecha";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    // Interfaz para los eventos de clic.
    public interface OnTaskClickListener {
        void onTaskClick(Task task); // Para editar.
        void onTaskCheckedChanged(Task task); // Para cambiar el estado de completado.
    }

    // Método para que la actividad se registre como listener de los clics.
    public void setOnTaskClickListener(OnTaskClickListener listener) {
        this.listener = listener;
    }
}
