package com.microservices.bikeservice.service;

import com.microservices.bikeservice.entity.Bike;
import com.microservices.bikeservice.repository.BikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BikeService {
    @Autowired
    BikeRepository repository;

    public List<Bike> getAll() {
        return repository.findAll();
    }

    public Bike getById(int id) {
        return repository.findById(id).orElse(null);
    }

    public Bike save(Bike bike) {
        Bike bikeNew = repository.save(bike);
        return bikeNew;
    }

    public List<Bike> byUserId(int id) {
        return repository.findByUserId(id);
    }
}
