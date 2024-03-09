package com.etteplan.servicemanual.maintenancetask;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.etteplan.servicemanual.factorydevice.FactoryDevice;


@Repository
public interface MaintenanceTaskRepository extends JpaRepository<MaintenanceTask, Long> {
	
	@Query("SELECT mt FROM MaintenanceTask mt WHERE mt.targetDevice.id = :deviceId")
    List<MaintenanceTask> findByTargetDevice(Long deviceId);

}
