package com.microservices.userservice.service;

import com.microservices.userservice.entity.User;
import com.microservices.userservice.feignclients.BikeFeignClient;
import com.microservices.userservice.feignclients.CarFeignClient;
import com.microservices.userservice.model.Bike;
import com.microservices.userservice.model.Car;
import com.microservices.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    UserRepository repository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CarFeignClient carFeignClient;

    @Autowired
    BikeFeignClient bikeFeignClient;

    public List<User> getAll() {
        return repository.findAll();
    }

    public User getById(int id) {
        return repository.findById(id).orElse(null);
    }

    public User save(User user) {
        User userNew = repository.save(user);
        return userNew;
    }

    public List<Car> getCars(int userId) {
        List<Car> cars = restTemplate.getForObject("http://localhost:8002/car/byuser" + userId, List.class);
        return cars;
    }

    public List<Bike> getBikes(int userId) {
        List<Bike> bikes = restTemplate.getForObject("http://localhost:8003/bike/byuser" + userId, List.class);
        return bikes;
    }

    public Car saveCar(int userId, Car car) {
        car.setUserId(userId);
        Car carNew = carFeignClient.save(car);
        return carNew;
    }

    public Bike saveBike(int userId, Bike bike) {
        bike.setUserId(userId);
        Bike bikeNew = bikeFeignClient.save(bike);
        return bikeNew;
    }

    public Map<String, Object> getUserAndVehicles(int userId) {
        Map<String, Object> result = new HashMap<>();
        User user = repository.findById(userId).orElse(null);
        if (user == null) {
            result.put("Mensaje", "No existe el usuario");
            return result;
        }
        result.put("User", user);
        List<Car> cars = carFeignClient.getCars(userId);
        if (cars.isEmpty())
            result.put("Cars", "El usuario no tiene coches");

        List<Bike> bikes = bikeFeignClient.getBikes(userId);
        if (bikes.isEmpty())
            result.put("Bikes", "El usuario no tiene motos");

        return result;
    }


}
