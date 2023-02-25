package com.crud.tasks.trello.service;

import com.crud.tasks.controller.TaskNotFoundException;
import com.crud.tasks.domain.Task;
import com.crud.tasks.repository.TaskRepository;
import com.crud.tasks.service.DbService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DbServiceTestSuite {

    @InjectMocks
    private DbService dbService;

    @Mock
    private TaskRepository repository;

    @Test
    void getAllTasksTest() {
        //Given
        Task task = new Task(1L, "Test", "Zadanie");
        List<Task> taskList = new ArrayList<>();
        taskList.add(task);

        when(repository.findAll()).thenReturn(taskList);

        //When
        List<Task> theList = dbService.getAllTasks();

        //Then
        assertEquals(1, theList.size());
        assertNotNull(theList);
    }

    @Test
    void getTaskTest() {
        //Given
        Task task = new Task(1L, "Test", "Zadanie");
        List<Task> taskList = new ArrayList<>();
        taskList.add(task);

        when(repository.findById(1L)).thenReturn(Optional.of(task));

        //When
        Optional<Task> theTask = null;
        try {
            theTask = Optional.ofNullable(dbService.getTask(1L));
        } catch (TaskNotFoundException e) {
            throw new RuntimeException(e);
        }

        //Then
        assertEquals(Optional.of(task), theTask);
    }

    @Test
    void saveTaskTest(){
        //Given
        Task task = new Task(1L, "Test", "Zadanie");
        when(repository.save(task)).thenReturn(task);

        //When
        Task savedTask = dbService.saveTask(task);

        //Then
        assertEquals(task.getId(), savedTask.getId());
        assertEquals(task.getTitle(), savedTask.getTitle());
        assertEquals(task.getContent(), savedTask.getContent());
    }

    @Test
    void deleteTaskTest() {
        //Given
        Task task = new Task(1L, "Test", "Zadanie");
        Long id = task.getId();

        when(repository.findById(id)).thenReturn(Optional.of(task));
        dbService.deleteTask(id);

        //When
        Optional<Task> taskOptional;
        try {
            taskOptional = Optional.ofNullable(dbService.getTask(id));
        } catch (TaskNotFoundException e) {
            throw new RuntimeException(e);
        }

        //Then
        assertTrue(taskOptional.isPresent());
    }
}
