package br.com.js.carhub.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.js.carhub.exception.MissingFieldsException;
import br.com.js.carhub.model.Car;
import br.com.js.carhub.model.dto.CarDTO;
import br.com.js.carhub.service.CarService;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping
    public ResponseEntity<Object> getAllCars() {
        try {
            List<CarDTO> cars = carService.findAll();
            return ResponseEntity.ok(cars);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(getErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCarById(@PathVariable Long id) {
        try {
            CarDTO car = carService.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Car not found with ID: " + id));
            return ResponseEntity.ok(car);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(getErrorResponse("Car not found with ID: " + id, HttpStatus.NOT_FOUND.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(getErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @PostMapping
    public ResponseEntity<Object> createCar(@RequestBody CarDTO car) {
        if (car == null) {
            return ResponseEntity.badRequest().body(getErrorResponse("Invalid fields", HttpStatus.BAD_REQUEST.value()));
        }

        Car carInList = carService.mapToCarEntity(car);
        if (carService.isThereCarWithPlate(Collections.singletonList(carInList))) {
            return ResponseEntity.badRequest().body(getErrorResponse("License plate already exists", HttpStatus.BAD_REQUEST.value()));
        }

        try {
            CarDTO createdCar = carService.save(car);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCar);
        } catch (MissingFieldsException e) {
            return ResponseEntity.badRequest().body(getErrorResponse("Missing fields", HttpStatus.BAD_REQUEST.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(getErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCar(@PathVariable Long id, @RequestBody CarDTO updatedCar) {
        if (updatedCar == null) {
            return ResponseEntity.badRequest().body(getErrorResponse("Invalid fields", HttpStatus.BAD_REQUEST.value()));
        }

        Car carInList = carService.mapToCarEntity(updatedCar);
        if (carService.isThereCarWithPlate(Collections.singletonList(carInList))) {
            return ResponseEntity.badRequest().body(getErrorResponse("License plate already exists", HttpStatus.BAD_REQUEST.value()));
        }
        try {
            Optional<CarDTO> updated = carService.update(id, updatedCar);
            return ResponseEntity.ok(updated.get());
        } catch (MissingFieldsException e) {
            return ResponseEntity.badRequest().body(getErrorResponse("Missing fields", HttpStatus.BAD_REQUEST.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(getErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCar(@PathVariable Long id) {
        try {
            carService.deleteCar(id);
            return ResponseEntity.ok("Car removed successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(getErrorResponse("Car not found with ID: " + id, HttpStatus.NOT_FOUND.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(getErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    private Object getErrorResponse(String message, int errorCode) {
        return Map.of("message", message, "errorCode", errorCode);
    }
}