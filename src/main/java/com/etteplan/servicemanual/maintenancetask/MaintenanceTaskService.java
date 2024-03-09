package com.etteplan.servicemanual.maintenancetask;

import java.util.List;

import org.springframework.stereotype.Service;

import com.etteplan.servicemanual.factorydevice.FactoryDevice;
import com.etteplan.servicemanual.maintenancetask.MaintenanceTask;

@Service
public interface MaintenanceTaskService {
    
    
    List<MaintenanceTask> getAllTasks();
    
    MaintenanceTask getTaskById(Long id);
    
    MaintenanceTask createTask(MaintenanceTask task);
    
    MaintenanceTask updateTask(Long id, MaintenanceTask task);
    
    void deleteTask(Long id);

	List<MaintenanceTask> getTasksFilteredByTarget(Long deviceId);
}
