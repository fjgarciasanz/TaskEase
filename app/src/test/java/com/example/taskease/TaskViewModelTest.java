package com.example.taskease;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias para el TaskViewModel.
 */
@RunWith(MockitoJUnitRunner.class)
public class TaskViewModelTest {

    // Regla para que las operaciones de LiveData se ejecuten de forma síncrona en el mismo hilo.
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    // Un "doble de prueba" (mock) del Repositorio.
    @Mock
    private TaskRepository mockRepository;

    private TaskViewModel taskViewModel;

    @Before
    public void setUp() {
        // --- ARREGLO DEFINITIVO ---
        // Antes de crear el ViewModel, configuramos el comportamiento del mock.
        // Cuando se llame a getAllTasks() en el mock, devolverá un LiveData vacío en lugar de null.
        // Esto evita el NullPointerException durante la construcción del ViewModel.
        when(mockRepository.getAllTasks()).thenReturn(new MutableLiveData<List<Task>>(Collections.emptyList()));

        // Ahora creamos el ViewModel con el mock ya configurado.
        taskViewModel = new TaskViewModel(mockRepository);
    }

    @Test
    public void insert_llamaAlRepositorio() {
        // 1. Preparación (Arrange)
        Task task = new Task("Hacer la compra", "", 0, false);

        // 2. Acción (Act)
        taskViewModel.insert(task);

        // 3. Verificación (Assert)
        // Verificamos que se llamó al método 'insert' del Repositorio.
        verify(mockRepository).insert(task);
    }

    @Test
    public void delete_llamaAlRepositorio() {
        // 1. Preparación
        Task task = new Task("Lavar el coche", "", 0, false);
        task.setId(1);

        // 2. Acción
        taskViewModel.delete(task);

        // 3. Verificación
        verify(mockRepository).delete(task);
    }
}
