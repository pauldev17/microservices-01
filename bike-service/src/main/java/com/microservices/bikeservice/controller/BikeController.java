package com.microservices.bikeservice.controller;

import com.microservices.bikeservice.entity.Bike;
import com.microservices.bikeservice.service.BikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bike")
public class BikeController {

    @Autowired
    BikeService service;

    @GetMapping
    public ResponseEntity<List<Bike>> getAll() {
        List<Bike> bikes = service.getAll();
        if (bikes.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(bikes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bike> getById(@PathVariable("id") int id) {
        Bike bike = service.getById(id);
        if (bike == null)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(bike);
    }

    @PostMapping
    public ResponseEntity<Bike> save(@RequestBody Bike car) {
        Bike bikeNew = service.save(car);
        return ResponseEntity.ok(bikeNew);
    }

    @GetMapping("/byuser/{userId}")
    public ResponseEntity<List<Bike>> getByUserId(@PathVariable("userId") int userId) {
        List<Bike> bikes = service.byUserId(userId);
        //if (bikes.isEmpty())
            //return ResponseEntity.noContent().build();

        return ResponseEntity.ok(bikes);
    }

}
