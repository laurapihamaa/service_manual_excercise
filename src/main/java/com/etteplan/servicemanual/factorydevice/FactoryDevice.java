package com.etteplan.servicemanual.factorydevice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class FactoryDevice {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "FACTORY_DEVICE_ID")
    private Long id;
    
    @Column(name = "FACTORY_DEVICE_NAME")
    private String name;
    
    @Column(name = "FACTORY_DEVICE_YEAR")
    private int year;
    
    @Column(name = "FACTORY_DEVICE_TYPE")
    private String type;

    protected FactoryDevice() {}

    /**
     * Constructor of FactoryDevice class
     * 
     * @param name Name of the device
     * @param year Year of the device
     * @param type Type of the device
     */
    public FactoryDevice(String name, int year, String type) {
        this.name = name;
        this.year = year;
        this.type = type;
    }

    public Long getId() {
        return this.id;
    }

    /**
     * 
     * @return name of the device
     */
    public String getName() {
        return this.name;
    }

    public int getYear() {
        return this.year;
    }

    public String getType() {
        return this.type;
    }
}