package com.microservices.carservice.service;

import com.microservices.carservice.entity.Car;
import com.microservices.carservice.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    @Autowired
    CarRepository repository;

    public List<Car> getAll() {
        return repository.findAll();
    }

    public Car getById(int id) {
        return repository.findById(id).orElse(null);
    }

    public Car save(Car car) {
        Car carNew = repository.save(car);
        return carNew;
    }

    public List<Car> byUserId(int id) {
        return repository.findByUserId(id);
    }
}
