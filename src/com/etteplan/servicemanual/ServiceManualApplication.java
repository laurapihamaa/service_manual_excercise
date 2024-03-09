package com.etteplan.servicemanual;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import main.java.com.etteplan.servicemanual.factorydevice.FactoryDevice;
import main.java.com.etteplan.servicemanual.factorydevice.FactoryDeviceRepository;

@SpringBootApplication 
@EnableJpaRepositories(basePackages = {"com.etteplan.servicemanual.factorydevice"})
@EntityScan("com.etteplan.servicemanual.factorydevice")
public class ServiceManualApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ServiceManualApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(final FactoryDeviceRepository repository) {
        return (args) -> {
        	

        	String csvFile = "/servicemanual-java/seeddata.csv";
            String line = "";
            String csvSplitBy = ",";

            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                List<FactoryDevice> devices = new ArrayList<>();

                while ((line = br.readLine()) != null) {
                    // Splitting the line by comma
                    String[] data = line.split(csvSplitBy);

                    // Creating FactoryDevice object from CSV data
                    FactoryDevice device = new FactoryDevice(data[0], Integer.parseInt(data[1]), data[2]);

                    // Adding the FactoryDevice object to the list
                    devices.add(device);
                }

                // Save all FactoryDevice objects to the database
                repository.saveAll(devices);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

}