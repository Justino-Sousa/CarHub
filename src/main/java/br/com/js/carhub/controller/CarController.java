package br.com.js.carhub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.js.carhub.exception.CarNotFoundException;
import br.com.js.carhub.exception.InvalidFieldsException;
import br.com.js.carhub.exception.LicensePlateException;
import br.com.js.carhub.exception.MissingFieldsException;
import br.com.js.carhub.exception.UserNotFoundException;
import br.com.js.carhub.model.dto.CarDTO;
import br.com.js.carhub.service.CarService;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/cars")
@CrossOrigin(origins = "*")
public class CarController {

	@Autowired
	private CarService carService;

	@GetMapping
	public ResponseEntity<List<CarDTO>> getAllCars() throws UserNotFoundException{
		return ResponseEntity.ok(carService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CarDTO> getCarById(@PathVariable Long id) throws CarNotFoundException {
		return ResponseEntity.ok(carService.findById(id));

	}

	@PostMapping
	public ResponseEntity<CarDTO> createCar(@RequestBody CarDTO car) throws InvalidFieldsException, LicensePlateException, MissingFieldsException, UserNotFoundException  {
		return ResponseEntity.ok(carService.save(car));
	}

	@PutMapping("/{id}")
	public ResponseEntity<CarDTO> updateCar(@PathVariable Long id, @RequestBody CarDTO updatedCar)
			throws EntityNotFoundException, Exception {
		return ResponseEntity.ok(carService.update(id, updatedCar));
	}

	@DeleteMapping("/{id}")
	public void deleteCar(@PathVariable Long id) throws UserNotFoundException, CarNotFoundException {
		carService.deleteCar(id);
	}

}