package br.com.js.carhub.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.js.carhub.exception.MissingFieldsException;
import br.com.js.carhub.model.Car;
import br.com.js.carhub.model.LoggedData;
import br.com.js.carhub.model.User;
import br.com.js.carhub.model.dto.CarDTO;
import br.com.js.carhub.repository.CarRepository;
import br.com.js.carhub.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CarService {

	@Autowired
	private CarRepository repository;

	@Autowired
	private LoggedData loggedData;

	@Autowired
	private UserRepository userRepository;

	public List<CarDTO> findAll() throws EntityNotFoundException, Exception {
		User loggedUser = userRepository.findById(loggedData.getLogguedUserId())
				.orElseThrow(() -> new EntityNotFoundException("User not found"));

		List<Car> cars = repository.findByUser(loggedUser);
		return cars.stream().map(this::mapToCarDTO).collect(Collectors.toList());
	}

	public Optional<CarDTO> findById(Long carId) throws EntityNotFoundException, Exception {
		User loggedUser = userRepository.findById(loggedData.getLogguedUserId())
				.orElseThrow(() -> new EntityNotFoundException("User not found"));

		return repository.findById(carId).filter(c -> c.getUser().getId() == loggedUser.getId()).map(this::mapToCarDTO);
	}

	public CarDTO save(CarDTO carDTO) throws EntityNotFoundException, Exception {

		if (!isValidUserData(carDTO))
			throw new MissingFieldsException();

		User loggedUser = userRepository.findById(loggedData.getLogguedUserId())
				.orElseThrow(() -> new EntityNotFoundException("User not found"));

		Car car = mapToCarEntity(carDTO);
		car.setUser(loggedUser);
		Car savedCar = repository.save(car);
		return mapToCarDTO(savedCar);
	}

	public Optional<CarDTO> update(Long carId, CarDTO updatedCarDTO) throws EntityNotFoundException, Exception {
		
		if (!isValidUserData(updatedCarDTO))
			throw new MissingFieldsException();
		
		User loggedUser = userRepository.findById(loggedData.getLogguedUserId())
				.orElseThrow(() -> new EntityNotFoundException("User not found"));
		
		Car car = mapToCarEntity(updatedCarDTO);
		car.setUser(loggedUser);
		if(car.getUser().getId() == loggedUser.getId()) {
			car.setColor(updatedCarDTO.getColor());
			car.setLicensePlate(updatedCarDTO.getLicensePlate());
			car.setModel(updatedCarDTO.getModel());
			car.setUser(loggedUser);
			car.setYear(updatedCarDTO.getYear());
		}
		
		Car savedCar = repository.save(car);
		return Optional.ofNullable(mapToCarDTO(savedCar));
	}

	public Car findByLicensePlate(String licensePlate) {
		return repository.findBylicensePlate(licensePlate);
	}

	public void deleteCar(Long carId) throws EntityNotFoundException, Exception {
		User loggedUser = userRepository.findById(loggedData.getLogguedUserId())
				.orElseThrow(() -> new EntityNotFoundException("User not found"));

		repository.findById(carId).filter(c -> c.getUser().getId() == loggedUser.getId())
				.ifPresent(c -> repository.deleteById(carId));
	}

	public CarDTO mapToCarDTO(Car car) {
		CarDTO carDTO = new CarDTO();
		carDTO.setId(car.getId());
		carDTO.setYear(car.getYear());
		carDTO.setLicensePlate(car.getLicensePlate());
		carDTO.setModel(car.getModel());
		carDTO.setColor(car.getColor());
		carDTO.setUser(car.getUser());

		return carDTO;
	}

	public Car mapToCarEntity(CarDTO carDTO) {
		Car car = new Car();
		car.setYear(carDTO.getYear());
		car.setLicensePlate(carDTO.getLicensePlate());
		car.setModel(carDTO.getModel());
		car.setColor(carDTO.getColor());
		car.setUser((carDTO.getUser()));
		return car;
	}

	public boolean isThereCarWithPlate(List<Car> carList) {
		List<String> foundPlates = new ArrayList<String>();
		for (Car car : carList) {
			if (repository.findBylicensePlate(car.getLicensePlate()) != null) {
				foundPlates.add(car.getLicensePlate());
			}
		}

		if (!foundPlates.isEmpty()) {
			return true;
		}
		return false;
	}

	private boolean isValidUserData(CarDTO car) {
		return car != null && isNotEmpty(car.getColor()) && isNotEmpty(car.getLicensePlate())
				&& isNotEmpty(car.getModel()) && (car.getUser()) != null && (car.getUser()) != null;
	}

	private boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

}
