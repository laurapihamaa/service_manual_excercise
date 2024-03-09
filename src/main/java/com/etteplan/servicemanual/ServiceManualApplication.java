package com.etteplan.servicemanual;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.beans.factory.annotation.Value;

import com.etteplan.servicemanual.factorydevice.FactoryDevice;
import com.etteplan.servicemanual.factorydevice.FactoryDeviceRepository;

@SpringBootApplication(scanBasePackages={
		"com.etteplan.servicemanual.maintenancetask", "com.etteplan.servicemanual.factorydevice"})
@EnableJpaRepositories(basePackages = {"com.etteplan.servicemanual.factorydevice", "com.etteplan.servicemanual.maintenancetask"})
@EntityScan({"com.etteplan.servicemanual.factorydevice", "com.etteplan.servicemanual.maintenancetask"})
public class ServiceManualApplication {
	
    public static void main(final String[] args) {
        SpringApplication.run(ServiceManualApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(final FactoryDeviceRepository repository) {
        return (args) -> {

            /**
             * Remove this. Temporary device storage before proper data storage is implemented.
             */
        	
        	/*
            final List<FactoryDevice> devices = Arrays.asList(
                new FactoryDevice("Device X", 2001, "type 10"),
                new FactoryDevice("Device Y", 2012, "type 3"),
                new FactoryDevice("Device Z", 1985, "type 1")
            );*/
        	
        	try {
        		List<FactoryDevice> devices = readDevicesFromCsv();
        		
        		if(!(devices.isEmpty())) {
        			repository.saveAll(devices);
        		}else {
        			System.out.println("No devices found in the CSV file.");
        		}
        	}catch(IOException e){
        			System.out.println("Error reading file: " + e.getMessage());
        	} 
        	
        };
    }
    
    private List<FactoryDevice> readDevicesFromCsv() throws IOException {
    	
    	ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource("seeddata.csv").getFile());
    	
        List<FactoryDevice> devices = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        	
        	//skip header line
        	reader.readLine();
        	
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0].trim();
                int year = Integer.parseInt(parts[1].trim());
                String type = parts[2].trim();
                devices.add(new FactoryDevice(name, year, type));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return devices;
    }

}