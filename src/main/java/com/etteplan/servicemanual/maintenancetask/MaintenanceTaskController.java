package com.etteplan.servicemanual.maintenancetask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;
 

@RestController
public class MaintenanceTaskController {
    
    @Autowired
    private MaintenanceTaskService taskService;
    
    /**
     * Retrieves all stored maintenance tasks 
     * 
     * @return a list of all maintenance tasks
     */
    
    @GetMapping("/tasks")
    public List<MaintenanceTask> getAllTasks() {
        return taskService.getAllTasks();
    }
    
    /**
     * 
     * Retrieves a maintenance task with the given id
     * 
     * @param id The ID of the maintenance task
     * @return ResponseEntity containing the requested maintenance task if task is found
     */
    
    @GetMapping("/tasks/getTask/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable("id") @NonNull Long id) {
    	
    	MaintenanceTask task = taskService.getTaskById(id);
        if (task != null) {
            return ResponseEntity.ok(task);
        } else {
        	return ResponseEntity.badRequest().body("No task is found");
        }
    }
    
    /**
     * 
     * Post a new maintenance task to the database
     * 
     * @param task The maintenance task to be posted
     * @return ResponseEntity containing the created task if the task has all of the required attributes
     * 
     */
    @PostMapping("/tasks/postTask")
    public ResponseEntity<?>  createTask(@Valid @RequestBody MaintenanceTask task, BindingResult bindingResult) {
    	
    	 if (bindingResult.hasErrors()) {
    		 StringBuilder errorMessageBuilder = new StringBuilder();
    		 errorMessageBuilder.append("Following errors found: ");
    		 errorMessageBuilder.append("\n");
             bindingResult.getFieldErrors().forEach(fieldError -> {
            	 errorMessageBuilder.append(fieldError.getDefaultMessage()).append("\n");
             });
             return ResponseEntity.badRequest().body(errorMessageBuilder.toString());
         }
    	
    	return ResponseEntity.ok(taskService.createTask(task));
    	

    }
    
    /**
     * 
     * Update an existing task attribute
     * 
     * @param id The task ID
     * @param task The task with the updated attributes
     * @return ResponseEntity containing the updated task if task is found
     */
    @PutMapping("/tasks/updateTasks/{id}")
    public ResponseEntity<?> updateTask(@PathVariable("id") @NonNull Long id, @RequestBody MaintenanceTask task) {
    	
    	MaintenanceTask task2 = taskService.getTaskById(id);

        if (task2 != null) {
        	MaintenanceTask updatedTask = taskService.updateTask(id, task);
            return ResponseEntity.ok(updatedTask);
        } else {
        	return ResponseEntity.badRequest().body("task not found");
        }
        
    }
    
    /**
     * 
     * Delete an existing task
     * 
     * @param id The ID of the task to be deleted
     * @return String of succesfull deletion if task was found
     */
    @DeleteMapping("/tasks/deleteTasks/{id}")
    public String deleteTask(@PathVariable("id") @NonNull Long id) {
    	
    	MaintenanceTask task = taskService.getTaskById(id);
    	
    	if(task==null)
    		return "task with id not found";
    	else {
    		taskService.deleteTask(id);
    		return "deleted succesfully";
    	}
    }
    
    /**
     * 
     * Filter tasks based on device ids
     * 
     * @param deviceId the ID of the device
     * @return ResponseEntity containing the list of tasks with the given factory device id
     */
    @GetMapping("/tasks/filter")
    public ResponseEntity<?>getFilteredTasks(@RequestParam(name = "deviceId") Long deviceId) {
    	
    	if(taskService.getTasksFilteredByTarget(deviceId).isEmpty()) {
    		return ResponseEntity.badRequest().body("no tasks exist with this device id");
    	}

        return ResponseEntity.ok(taskService.getTasksFilteredByTarget(deviceId));
    }
}
