package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DbService dbService;

    @MockBean
    private TaskMapper taskMapper;

    @Test
    void testGetTasksEmptyList() throws Exception {
        //Given
        List<Task> taskList = new ArrayList<>();
        List<TaskDto> taskDtoList = new ArrayList<>();

        when(dbService.getAllTasks()).thenReturn(taskList);
        when(taskMapper.mapToTaskDtoList(taskList)).thenReturn(taskDtoList);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/tasks").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(0)));
    }

    @Test
    void testGetTasks() throws Exception {
        //Given
        List<Task> taskList = new ArrayList<>();
        taskList.add(new Task(1L, "Task title", "Task content"));

        List<TaskDto> taskDtoList = new ArrayList<>();
        taskDtoList.add(new TaskDto(1L, "Task Dto title", "Task Dto content"));

        when(dbService.getAllTasks()).thenReturn(taskList);
        when(taskMapper.mapToTaskDtoList(taskList)).thenReturn(taskDtoList);

        //When & Then
        mockMvc.perform(MockMvcRequestBuilders.
                        get("/v1/tasks").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //TaskDto fields
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", Matchers.is("Task Dto title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content", Matchers.is("Task Dto content")))
                //Task fields
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lists", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lists[0].id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lists[0].title", Matchers.is("Task title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lists[0].content", Matchers.is("Task content")));
    }

    @Test
    public void testGetTask() throws Exception {
        //Given
        Task task = new Task(1L, "Task title", "Task content");
        TaskDto taskDto = new TaskDto(1L, "Task Dto title", "Task Dto content");

        when(dbService.getTask(task.getId())).thenReturn(task);
        when(taskMapper.mapToTaskDto(task)).thenReturn(taskDto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(taskDto);

        //When & Then
        mockMvc.perform(MockMvcRequestBuilders.
                        get("/v1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("Task title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is("Task content")));
    }

    @Test
    public void testDeleteTask() throws Exception {
        //Given
        Task task = new Task(1L, "Task test title", "Task test content");

        dbService.deleteTask(task.getId());

        Gson gson = new Gson();
        String jsonContent = gson.toJson(task);

        //When & Then
        mockMvc.perform(MockMvcRequestBuilders.
                        delete("/v1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUpdateTask() throws Exception {
        //Given
        Task task = new Task(1L, "Task title", "Task content");
        TaskDto taskDto = new TaskDto(1L, "Task Dto title", "Task Dto content");

        when(taskMapper.mapToTask(taskDto)).thenReturn(task);
        when(dbService.saveTask(task)).thenReturn(task);
        when(taskMapper.mapToTaskDto(task)).thenReturn(taskDto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(taskDto);

        //When & Then
        mockMvc.perform(MockMvcRequestBuilders.
                        put("/v1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("Task title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is("Task content")));
    }

    @Test
    public void testCreateTask() throws Exception {
        //Given
        Task task = new Task(1L, "Task title", "Tast content");
        TaskDto taskDto = new TaskDto(1L, "Task Dto title", "Task Dto content");

        when(taskMapper.mapToTask(taskDto)).thenReturn(task);
        when(dbService.saveTask(task)).thenReturn(task);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(task);

        //When & Then
        mockMvc.perform(MockMvcRequestBuilders.
                        post("/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("Task title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is("Task content")));
    }

}
