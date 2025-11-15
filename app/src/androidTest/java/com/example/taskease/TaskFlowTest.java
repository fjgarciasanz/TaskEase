package com.example.taskease;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TaskFlowTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void createNewTask_displaysItInList() {
        String taskTitle = "Comprar leche";
        String taskDescription = "En el supermercado de la esquina";

        // 1. Pulsar el botón flotante para añadir una tarea.
        onView(withId(R.id.fab_add_task)).perform(click());

        // 2. Escribir el título y la descripción.
        onView(withId(R.id.edit_text_title)).perform(typeText(taskTitle));
        onView(withId(R.id.edit_text_description)).perform(typeText(taskDescription), closeSoftKeyboard());

        // --- ARREGLO CLAVE Y FINAL ---
        // 3. Hacer clic en el nuevo y fiable botón de guardar.
        onView(withId(R.id.button_save)).perform(click());

        // 4. Verificar que la nueva tarea se muestra en la lista.
        onView(withText(taskTitle)).check(matches(isDisplayed()));
    }
}
