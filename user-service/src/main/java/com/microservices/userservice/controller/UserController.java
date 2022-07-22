package com.microservices.userservice.controller;

import com.microservices.userservice.entity.User;
import com.microservices.userservice.model.Bike;
import com.microservices.userservice.model.Car;
import com.microservices.userservice.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService service;

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        List<User> users = service.getAll();
        if (users.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") int id) {
        User user = service.getById(id);
        if (user == null)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> save(@RequestBody User user) {
        User userNew = service.save(user);
        return ResponseEntity.ok(userNew);
    }

    @CircuitBreaker(name = "carsCB", fallbackMethod = "fallbackGetCars")
    @GetMapping("/cars/{userId}")
    public ResponseEntity<List<Car>> getCars(@PathVariable("userId") int userId) {
        User user = service.getById(userId);
        if (user == null)
            return ResponseEntity.noContent().build();

        List<Car> cars = service.getCars(userId);
        return ResponseEntity.ok(cars);
    }

    @CircuitBreaker(name = "carsCB", fallbackMethod = "fallbackSaveCar")
    @PostMapping("/savecar/{userId}")
    public ResponseEntity<Car> saveCar(@PathVariable("userId") int userId, @RequestBody Car car) {
        if (service.getById(userId) == null)
            return ResponseEntity.notFound().build();

        Car carNew = service.saveCar(userId, car);
        return ResponseEntity.ok(carNew);
    }

    @CircuitBreaker(name = "bikeCB", fallbackMethod = "fallbackGetBike")
    @GetMapping("/bikes/{userId}")
    public ResponseEntity<List<Bike>> getBikes(@PathVariable("userId") int userId) {
        User user = service.getById(userId);
        if (user == null)
            return ResponseEntity.noContent().build();

        List<Bike> bikes = service.getBikes(userId);
        return ResponseEntity.ok(bikes);
    }

    @CircuitBreaker(name = "bikeCB", fallbackMethod = "fallbackSaveBike")
    @PostMapping("/savebike/{userId}")
    public ResponseEntity<Bike> saveBike(@PathVariable("userId") int userId, @RequestBody Bike bike) {
        if (service.getById(userId) == null)
            return ResponseEntity.notFound().build();

        Bike bikeNew = service.saveBike(userId, bike);
        return ResponseEntity.ok(bikeNew);
    }

    @CircuitBreaker(name = "allCB", fallbackMethod = "fallbackGetAll")
    @GetMapping("/getAll/{userId}")
    public ResponseEntity<Map<String, Object>> getAll(@PathVariable("userId") int userId) {
        Map<String, Object> result = service.getUserAndVehicles(userId);
        return ResponseEntity.ok(result);
    }

    private ResponseEntity<List<Car>> fallbackGetCars(@PathVariable("userId") int userId, RuntimeException ex) {
        return new ResponseEntity("El usuario " + userId + " tiene los coches en el taller", HttpStatus.OK);
    }

    private ResponseEntity<Car> fallbackSaveCar(@PathVariable("userId") int userId, @RequestBody Car car, RuntimeException ex) {
        return new ResponseEntity("El usuario " + userId + " no tiene dinero para coches", HttpStatus.OK);
    }

    private ResponseEntity<List<Bike>> fallbackGetBike(@PathVariable("userId") int userId, RuntimeException ex) {
        return new ResponseEntity("El usuario " + userId + " tiene las motos en el taller", HttpStatus.OK);
    }

    private ResponseEntity<Bike> fallbackSaveBike(@PathVariable("userId") int userId, @RequestBody Bike bike, RuntimeException ex) {
        return new ResponseEntity("El usuario " + userId + " no tiene dinero paara motos", HttpStatus.OK);
    }

    private ResponseEntity<Map<String, Object>> fallbackGetAll(@PathVariable("userId") int userId) {
        return new ResponseEntity("El usuario " + userId + " tiene los vehiculos en el taller", HttpStatus.OK);
    }

}
