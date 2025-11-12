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

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskViewHolder> {
    private OnTaskClickListener listener;

    public TaskAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return Objects.equals(oldItem.getTitle(), newItem.getTitle()) &&
                    Objects.equals(oldItem.getDescription(), newItem.getDescription()) &&
                    oldItem.getDate() == newItem.getDate() &&
                    oldItem.isCompleted() == newItem.isCompleted();
        }
    };

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public Task getTaskAt(int position) {
        return getItem(position);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final TextView textViewDescription;
        private final CheckBox checkBoxCompleted;
        private final View statusBar;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textview_title);
            textViewDescription = itemView.findViewById(R.id.textview_description);
            checkBoxCompleted = itemView.findViewById(R.id.checkbox_completed);
            statusBar = itemView.findViewById(R.id.status_bar);

            checkBoxCompleted.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    Task task = getItem(position);
                    Task updatedTask = new Task(task.getTitle(), task.getDescription(), task.getDate(), checkBoxCompleted.isChecked());
                    updatedTask.setId(task.getId());
                    listener.onTaskCheckedChanged(updatedTask);
                }
            });

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onTaskClick(getItem(position));
                }
            });
        }

        public void bind(Task task) {
            textViewTitle.setText(task.getTitle());
            textViewDescription.setText(formatDate(task.getDate()));
            checkBoxCompleted.setChecked(task.isCompleted());

            if (task.isCompleted()) {
                textViewTitle.setPaintFlags(textViewTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                statusBar.setBackgroundColor(Color.parseColor("#4CAF50")); // Verde
            } else {
                textViewTitle.setPaintFlags(textViewTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                statusBar.setBackgroundColor(Color.parseColor("#757575")); // Gris
            }
        }
    }

    private String formatDate(long timestamp) {
        if (timestamp == 0) return "Sin fecha";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
        void onTaskCheckedChanged(Task task);
    }

    public void setOnTaskClickListener(OnTaskClickListener listener) {
        this.listener = listener;
    }
}
