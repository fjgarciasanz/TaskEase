package com.example.taskease;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskViewHolder> {
    private OnTaskClickListener listener;

    // Usamos ListAdapter para mejor rendimiento.
    // Requiere un DiffUtil.Callback
    public TaskAdapter() {
        super(DIFF_CALLBACK);
    }

    // El DiffUtil calcula las diferencias en la lista y anima los cambios
    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getDate() == newItem.getDate() &&
                    oldItem.isCompleted() == newItem.isCompleted();
        }
    };

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos el layout de la fila (task_item.xml)
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        // Obtenemos la tarea actual
        Task currentTask = getItem(position);

        // Rellenamos las vistas con los datos de la tarea
        holder.textViewTitle.setText(currentTask.getTitle());
        holder.textViewDescription.setText(formatDate(currentTask.getDate())); // Mostramos la fecha
        holder.checkBoxCompleted.setChecked(currentTask.isCompleted());

        // Aplicamos el tachado si está completada (RF-06)
        updateStrikeThrough(holder.textViewTitle, currentTask.isCompleted());
    }

    // Método para obtener la tarea en una posición (útil para swipe-to-delete)
    public Task getTaskAt(int position) {
        return getItem(position);
    }

    // --- ViewHolder ---
    // Contiene las vistas de cada fila
    class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private CheckBox checkBoxCompleted;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textview_title);
            textViewDescription = itemView.findViewById(R.id.textview_description);
            checkBoxCompleted = itemView.findViewById(R.id.checkbox_completed);

            // Listener para el CheckBox (RF-06)
            checkBoxCompleted.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    Task task = getItem(position);
                    // Actualizamos el estado de la tarea (creamos una copia)
                    Task updatedTask = new Task(task.getTitle(), task.getDescription(), task.getDate(), checkBoxCompleted.isChecked());
                    updatedTask.setId(task.getId()); // ¡Muy importante mantener el ID!
                    listener.onTaskCheckedChanged(updatedTask);
                }
            });

            // Listener para el clic en el item (para editar)
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onTaskClick(getItem(position));
                }
            });
        }
    }

    // --- Helper Methods ---

    // Formatea el timestamp (long) a un String legible
    private String formatDate(long timestamp) {
        if (timestamp == 0) return "Sin fecha";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    // Tacha o quita el tachado del texto
    private void updateStrikeThrough(TextView textView, boolean isCompleted) {
        if (isCompleted) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    // --- Interfaz para Clicks ---
    // La MainActivity implementará esta interfaz
    public interface OnTaskClickListener {
        void onTaskClick(Task task); // Para editar
        void onTaskCheckedChanged(Task task); // Para marcar/desmarcar
    }

    public void setOnTaskClickListener(OnTaskClickListener listener) {
        this.listener = listener;
    }
}
