package com.etteplan.servicemanual.factorydevice;

import org.springframework.data.jpa.repository.JpaRepository;

import com.etteplan.servicemanual.factorydevice.FactoryDevice;

public interface FactoryDeviceRepository extends JpaRepository<FactoryDevice, Long> {
    
}