package com.example.taskease;

import android.content.Context;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.taskease.util.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {

    private TaskDao taskDao;
    private TaskEaseDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, TaskEaseDatabase.class).build();
        taskDao = db.taskDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void insertAndGetTask() throws Exception {
        Task task = new Task("Pasear al perro", "Por el parque", 123L, false);
        taskDao.insert(task);
        List<Task> tasks = taskDao.getAllTasksSync();
        assertEquals(1, tasks.size());
        assertEquals("Pasear al perro", tasks.get(0).getTitle());
    }
}
