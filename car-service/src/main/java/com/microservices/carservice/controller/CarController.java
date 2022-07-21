package com.microservices.carservice.controller;

import com.microservices.carservice.entity.Car;
import com.microservices.carservice.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car")
public class CarController {

    @Autowired
    CarService service;

    @GetMapping
    public ResponseEntity<List<Car>> getAll() {
        List<Car> cars = service.getAll();
        if (cars.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getById(@PathVariable("id") int id) {
        Car car = service.getById(id);
        if (car == null)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(car);
    }

    @PostMapping
    public ResponseEntity<Car> save(@RequestBody Car car) {
        Car carNew = service.save(car);
        return ResponseEntity.ok(carNew);
    }

    @GetMapping("/byuser/{userId}")
    public ResponseEntity<List<Car>> getByUserId(@PathVariable("userId") int userId) {
        List<Car> cars = service.byUserId(userId);
        //if (cars.isEmpty())
            //return ResponseEntity.noContent().build();

        return ResponseEntity.ok(cars);
    }

}
