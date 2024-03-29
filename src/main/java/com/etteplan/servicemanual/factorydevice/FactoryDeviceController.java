package com.etteplan.servicemanual.factorydevice;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class FactoryDeviceController {

    private final FactoryDeviceRepository repository;

    FactoryDeviceController(FactoryDeviceRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/factorydevices")
    List<FactoryDevice> all() {
        return repository.findAll();
    }

    @GetMapping("/factorydevices/{id}")
    FactoryDevice one(@PathVariable Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new FactoryDeviceNotFoundException(id));
    }
    
    @PostMapping("/factorydevices/saveAll")
    List<FactoryDevice> saveAll(@RequestBody List<FactoryDevice> devices) {
        return repository.saveAll(devices);
    }
}