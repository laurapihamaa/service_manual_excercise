package com.etteplan.servicemanual.maintenancetask;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.etteplan.servicemanual.factorydevice.FactoryDevice;
import com.etteplan.servicemanual.factorydevice.FactoryDeviceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class MaintenanceTaskControllerTest {
	
	/*
	 * 
	 * the tests use the actual database in this case as it is only for 
	 * practice purposes
	 * 
	 */
	
    @Autowired
    private MockMvc mvc;
    
    @Autowired
    private MaintenanceTaskRepository maintenanceTaskRepository;
    
    @Autowired
    private MaintenanceTaskController maintenanceTaskController;
    
    @Autowired
    private FactoryDeviceRepository factoryDeviceRespository;

    
    /**
     * 
     * create a task for testing purposes
     * 
     * @return MaintenanceTask created for testing
     * @throws IOException
     */
    public static MaintenanceTask createTestTask () throws IOException{
    	
    	FactoryDevice device1 = new FactoryDevice("test_name", 2000, "test_type");  	
    	MaintenanceTask task = new MaintenanceTask();
        task.setDescription("Sample Task Description");
        task.setRegistrationTime("2023-04-22");
        task.setSeverity(Severity.CRITICAL);
        task.setStatus(TaskStatus.OPEN);
        task.setFactoryDevice(device1);
        
        return task;
    }
    
    /*
     * 
     * fetch all tasks from database and ecpect status to be ok
     * 
     */
    
    @Test
    public void getAllTasks() throws Exception {
    	
        mvc.perform(MockMvcRequestBuilders.get("/tasks").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
    
    /*
     * 
     * test fetching task using its id. The test should return status ok, content of the task and the
     * task id to be correct
     * 
     */
    
    @Test
    @Transactional 
    public void testGetTaskById() throws Exception {
        // Mocked task for testing
        MaintenanceTask task = createTestTask();
        factoryDeviceRespository.save(task.getFactoryDevice());
        MaintenanceTask savedTask = maintenanceTaskRepository.save(task);
        

        // Perform GET request to fetch task by ID
        mvc.perform(get("/tasks/getTask/{id}", savedTask.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(savedTask.getId().intValue()));
    }
    
    /*
     * 
     * test getting task when the task is not present in database
     * test should return a client error
     * 
     */

    @Test
    public void testGetTaskById_NotFound() throws Exception {
    	
    	mvc.perform(get("/tasks/getTask/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
    
    /*
     * 
     * create a task and test posting it to database
     * the test should return the created task as json content,
     * status ok and a created id.
     * 
     */
    
    @Test
    @Transactional 
    public void testCreateTask() throws Exception {

    	MaintenanceTask task = createTestTask();
        factoryDeviceRespository.save(task.getFactoryDevice());

        // Convert MaintenanceTask object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String taskJson = objectMapper.writeValueAsString(task);

        // Perform POST request to create task
        MvcResult result = mvc.perform(post("/tasks/postTask")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists()) // Assert that the returned task has an ID
                .andExpect(jsonPath("$.description").value("Sample Task Description"))
                .andReturn();
        
        String responseBody = result.getResponse().getContentAsString();

        // Assert that the task was saved to the database
        MaintenanceTask savedTask = maintenanceTaskRepository.getById(objectMapper.readTree(responseBody).get("id").asLong());
        assertEquals("Sample Task Description", savedTask.getDescription());
    }

    /*
     * test sending an updated description for a task and saving it to database
     * this test could also cover other updates (status, criticality etc.)
     * test should return the updated task and status ok
     * 
     */
    
    @Test
    @Transactional 
    public void testUpdateTask() throws Exception {
        // Create a sample MaintenanceTask object to update
    	MaintenanceTask task = createTestTask();
    	factoryDeviceRespository.save(task.getFactoryDevice());
    	
        MaintenanceTask savedTask = maintenanceTaskRepository.save(task);

        // Update the description of the existing task
        savedTask.setDescription("Updated Task Description");

        // Convert the updated MaintenanceTask object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String updatedTaskJson = objectMapper.writeValueAsString(savedTask);

        // Perform PUT request to update task
        mvc.perform(put("/tasks/updateTasks/{id}", savedTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedTaskJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(savedTask.getId())) // Assert that the returned task ID matches
                .andExpect(jsonPath("$.description").value("Updated Task Description")); // Assert updated description

        // Assert that the task was updated in the database
        MaintenanceTask updatedTask = maintenanceTaskRepository.findById(savedTask.getId()).orElse(null);
        assertNotNull(updatedTask);
        assertEquals("Updated Task Description", updatedTask.getDescription());
    }
    
    /*
     * 
     * test updating a task with an ID that doesnt exist in the database
     * the test should return a client error
     * 
     */
    
    @Test
    @Transactional 
    public void testUpdateTask_NotFound() throws Exception {
        // Create a sample MaintenanceTask object to update
    	MaintenanceTask task = createTestTask();
    	factoryDeviceRespository.save(task.getFactoryDevice());
        MaintenanceTask savedTask = maintenanceTaskRepository.save(task);

        // Update the description of the existing task
        savedTask.setDescription("Updated Task Description");

        // Convert the updated MaintenanceTask object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String updatedTaskJson = objectMapper.writeValueAsString(savedTask);

        // Perform PUT request to update task
        mvc.perform(put("/tasks/updateTasks/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedTaskJson))
        		.andExpect(status().is4xxClientError());
        
        //assert that object was not updated and is not found
        assertFalse(maintenanceTaskRepository.existsById(9999L));
    }
    
    /*
     * test deleting the task
     * the test should return status ok
     */
    
    @Test
    @Transactional 
    public void testDeleteTask() throws Exception {
        // Create a sample MaintenanceTask object to delete
    	MaintenanceTask task = createTestTask();
    	factoryDeviceRespository.save(task.getFactoryDevice());
        MaintenanceTask savedTask = maintenanceTaskRepository.save(task);

        // Perform DELETE request to delete task
        mvc.perform(delete("/tasks/deleteTasks/{id}", savedTask.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("deleted succesfully"));

        // Assert that the task was deleted from the database
        assertFalse(maintenanceTaskRepository.existsById(savedTask.getId()));
    }
    
    /*
     * 
     * test deleting a task which is not present in database
     * the test should return client error
     * 
     */
    
    @Test
    public void testDeleteTask_NotFound() throws Exception {
    	
    	mvc.perform(delete("/tasks/deleteTasks/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().string("task with id not found"));
    }
    
    /*
     * Test method to verify filtering of maintenance tasks by FactoryDevice ID.
     * 
     * This test creates two sample MaintenanceTask objects, each associated with a different FactoryDevice.
     * It then performs a GET request to filter tasks by the ID of one of the FactoryDevices.
     * 
     * The test asserts that only tasks associated with the specified FactoryDevice ID are returned in the response.
     * 
     */
    
    @Test
    @Transactional
    public void testGetFilteredTasks() throws Exception {
        // Create a sample MaintenanceTask object with a specific device ID
    	MaintenanceTask task1 = createTestTask();
    	factoryDeviceRespository.save(task1.getFactoryDevice());
        MaintenanceTask savedTask = maintenanceTaskRepository.save(task1);

        // Create another sample MaintenanceTask object with a different device ID
        MaintenanceTask task2 = createTestTask();
    	factoryDeviceRespository.save(task2.getFactoryDevice());
        MaintenanceTask savedTask2 = maintenanceTaskRepository.save(task2);
        
        int deviceId1 = savedTask.getFactoryDevice().getId().intValue();

        mvc.perform(get("/tasks/filter")
                .param("deviceId", String.valueOf(deviceId1)) // Filter tasks by FactoryDevice ID
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].factoryDevice.id", everyItem(equalTo(deviceId1))));
       
    }
    
    /*
     * Test method to verify filtering of maintenance tasks by false FactoryDevice ID.
     * 
     * 
     * The test asserts that a client error is returned
     * 
     */
    
    @Test
    @Transactional
    public void testGetFilteredTasks_devicesNotFound() throws Exception {

        mvc.perform(get("/tasks/filter?deviceId=9999")
                .contentType(MediaType.APPLICATION_JSON))
        		.andExpect(status().is4xxClientError())
        		.andExpect(content().string("no tasks exist with this device id")); 
       
    }
    

}
