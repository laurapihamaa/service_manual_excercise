package com.etteplan.servicemanual.maintenancetask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etteplan.servicemanual.factorydevice.FactoryDevice;
import com.etteplan.servicemanual.factorydevice.FactoryDeviceRepository;
import com.etteplan.servicemanual.maintenancetask.MaintenanceTask;
import com.etteplan.servicemanual.maintenancetask.MaintenanceTaskRepository;
import com.etteplan.servicemanual.maintenancetask.MaintenanceTaskService;

@Service
public class MaintenanceTaskImpl implements MaintenanceTaskService{
	
	@Autowired
    private MaintenanceTaskRepository maintenanceTaskRepository;
	
	@Autowired
    private FactoryDeviceRepository factoryDeviceRepository;
	
	/*
	 * 
	 * Retrieve all tasks from repository in sorted order
	 * 
	 * @return tasks The tasks sorted in list
	 * 
	 */

	@Override
	public List<MaintenanceTask> getAllTasks() {
		
		try {
			List<MaintenanceTask> tasks = maintenanceTaskRepository.findAll();
			Collections.sort(tasks, new CustomComparator());
			return tasks;
		}catch (Exception e) {
			throw new RuntimeException("Error retrieveing all tasks");
		}
	}
	
	/*
	 * 
	 * Retrieve task from repository based on id
	 * 
	 * @param id The ID of the task to retrieve
	 * @return MaintenanceTask of the given ID
	 * 
	 */

	@Override
	public MaintenanceTask getTaskById(Long id) {
		
		try {
			Optional<MaintenanceTask> taskOptional = maintenanceTaskRepository.findById(id);
			return taskOptional.orElse(null);
		}catch(Exception e) {
			throw new RuntimeException("Error getting task by id");
		}
        
	}
	
	/*
	 * 
	 * Create a new task to the repository
	 * 
	 * @param task The Maintenance task to store
	 * @return MaintenanceTask the saved task
	 * 
	 */

	@Override
	public MaintenanceTask createTask(MaintenanceTask task) {
		
		try {
			return maintenanceTaskRepository.saveAndFlush(task);
		}catch(Exception e) {
			throw new RuntimeException("Error getting task by id");
		}
	}
	
	/*
	 * 
	 * Update a task to the repository
	 * 
	 * @param task The Maintenance task to store
	 * @param id The ID of the task to update
	 * @return MaintenanceTask the updated task
	 * 
	 */

	@Override
	public MaintenanceTask updateTask(Long id, MaintenanceTask task) {
		
		try {
		
			MaintenanceTask maintenanceTask = maintenanceTaskRepository.findById(id) .get(); 

			if (Objects.nonNull(task.getFactoryDevice())){ 
			maintenanceTask.setFactoryDevice(task.getFactoryDevice()); 
			} 

			if (Objects.nonNull(task.getRegistrationTime()) && !"".equalsIgnoreCase(task.getRegistrationTime())) { 
			maintenanceTask.setRegistrationTime(task.getRegistrationTime()); 
			} 

			if (Objects.nonNull(task.getDescription()) && !"".equalsIgnoreCase(task.getDescription())) { 
			maintenanceTask.setDescription(task.getDescription()); 
			} 
    
			if (Objects.nonNull(task.getSeverity())) {
			maintenanceTask.setSeverity(task.getSeverity()); 
			}
    
			if (Objects.nonNull(task.getStatus())) { 
			maintenanceTask.setStatus(task.getStatus()); 
			}

			return maintenanceTaskRepository.save(maintenanceTask);
		}catch(Exception e) {
			throw new RuntimeException("Error updating task");
			}
	}
	
	/*
	 * 
	 * Delete task from repository based on id
	 * 
	 * @param id The ID of the task to delete
	 * 
	 */

	@Override
	public void deleteTask(Long id) {
		
		try {
			maintenanceTaskRepository.deleteById(id);
		}catch(Exception e) {
			throw new RuntimeException("Error deleting task");
		}
	}
	
	/*
	 * 
	 * Retrieve all tasks from repository with given device ID
	 * 
	 * @param deviceId the ID of the devices
	 * @return tasks The tasks with the device
	 * 
	 */

	@Override
	public List<MaintenanceTask> getTasksFilteredByTarget(Long deviceId) {
		
		try {
			FactoryDevice targetDevice = factoryDeviceRepository.findById(deviceId).orElse(null);
			if (targetDevice == null) {
				return new ArrayList<>(); 
			}
			return maintenanceTaskRepository.findByTargetDevice(deviceId);
		}catch(Exception e) {
			throw new RuntimeException("Error filtering tasks");
		}

	}
}
