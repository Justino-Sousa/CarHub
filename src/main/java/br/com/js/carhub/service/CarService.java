package br.com.js.carhub.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.js.carhub.model.Car;
import br.com.js.carhub.repository.CarRepository;

@Service
public class CarService {
	
	@Autowired
	private CarRepository repository;

	public List<Car> findAll() {
		return repository.findAll();
	}

	public Optional<Car> findById(Long CarId) {
		return repository.findById(CarId);
	}

	public Car save(Car Car) {
		return repository.save(Car);
	}
	
	public Optional<Car> update(Long CarId, Car updatedCar) {
		return repository.findById(CarId).map(t -> {
			t.setColor(updatedCar.getColor());
			t.setLicensePlate(updatedCar.getLicensePlate());
			t.setModel(updatedCar.getModel());
			t.setUser(updatedCar.getUser());
			t.setYear(updatedCar.getYear());

			return repository.save(t);
		});
	}
	
    public void deleteCar(Long CarId) {
    	repository.deleteById(CarId);
    }
}
