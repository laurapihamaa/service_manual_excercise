package com.etteplan.servicemanual.maintenancetask;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.etteplan.servicemanual.factorydevice.FactoryDevice;


@Entity
public class MaintenanceTask {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID",updatable = false, nullable = false)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "FACTORY_DEVICE_ID", nullable = false)
    @NotNull(message = "Device is required")
    private FactoryDevice targetDevice;
    
    @NotBlank(message = "Description is required")
    @Column(name = "REGISTRATION_TIME", nullable = false)
    private String registrationTime;
    
    @NotBlank(message = "Description is required")
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @NotNull(message = "Status is required")
    @ValidEnumValue(regexp = "CRITICAL|IMPORTANT|UNIMPORTANT")
    @Column(name = "SEVERITY", nullable = false)
    private Severity severity;
    
    @NotNull(message = "Status is required")
    @ValidEnumValue(regexp = "OPEN|CLOSED")
    @Column(name = "STATUS", nullable = false)
    private TaskStatus status;
    
    public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FactoryDevice getFactoryDevice() {
		return this.targetDevice;
	}

	public void setFactoryDevice(FactoryDevice factoryDevice) {
		this.targetDevice = factoryDevice;
		
	}

	public String getRegistrationTime() {
		return this.registrationTime;
	}

	public void setRegistrationTime(String registrationTime) {
		this.registrationTime = registrationTime;
		
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
		
	}

	public Severity getSeverity() {
		return this.severity;
	}

	public void setSeverity(Severity severity) {
		this.severity=severity;
		
	}

	public TaskStatus getStatus() {
		return this.status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
		
	}

}