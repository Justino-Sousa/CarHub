package br.com.js.carhub.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.js.carhub.exception.CarNotFoundException;
import br.com.js.carhub.exception.InvalidFieldsException;
import br.com.js.carhub.exception.LicensePlateException;
import br.com.js.carhub.exception.MissingFieldsException;
import br.com.js.carhub.exception.UserNotFoundException;
import br.com.js.carhub.model.Car;
import br.com.js.carhub.model.LoggedData;
import br.com.js.carhub.model.User;
import br.com.js.carhub.model.dto.CarDTO;
import br.com.js.carhub.repository.CarRepository;
import br.com.js.carhub.repository.UserRepository;

@Service
public class CarService {

	@Autowired
	private CarRepository repository;

	@Autowired
	private LoggedData loggedData;

	@Autowired
	private UserRepository userRepository;

	public List<CarDTO> findAll() throws UserNotFoundException {
		User loggedUser = null;

		try {
			loggedUser = userRepository.findById(loggedData.getLogguedUserId()).get();
		} catch (Exception e) {
			throw new UserNotFoundException();
		}

		List<Car> cars = repository.findByUser(loggedUser);
		return cars.stream().map(this::mapToCarDTO).collect(Collectors.toList());
	}

	public CarDTO findById(Long carId) throws CarNotFoundException {

		CarDTO dto = null;
		try {
			User loggedUser = userRepository.findById(loggedData.getLogguedUserId())
					.orElseThrow(() -> new CarNotFoundException());
			dto = repository.findById(carId).filter(c -> c.getUser().getId() == loggedUser.getId())
					.map(this::mapToCarDTO).get();
		} catch (Exception e) {
			throw new CarNotFoundException();
		}
		return dto;
	}

	public CarDTO save(CarDTO carDTO)
			throws InvalidFieldsException, LicensePlateException, MissingFieldsException, UserNotFoundException {
		if (carDTO == null) {
			throw new InvalidFieldsException();
		}

		Car carInList = mapToCarEntity(carDTO);
		if (isThereCarWithPlate(Collections.singletonList(carInList))) {
			throw new LicensePlateException();
		}

		User loggedUser = null;
		try {
			loggedUser = userRepository.findById(loggedData.getLogguedUserId()).get();
			carDTO.setUser(loggedUser);
		} catch (Exception e) {
			throw new UserNotFoundException();
		}
		
		if (!isValidUserData(carDTO))
			throw new MissingFieldsException();

		Car car = mapToCarEntity(carDTO);
		car.setUser(loggedUser);
		Car savedCar = repository.save(car);
		return mapToCarDTO(savedCar);
	}

	public CarDTO update(Long carId, CarDTO updatedCarDTO) throws InvalidFieldsException, MissingFieldsException, UserNotFoundException, CarNotFoundException {

		if (updatedCarDTO == null) {
			throw new InvalidFieldsException();
		}
		
		Car car = null;
		try {
			car = repository.findById(carId).get();
		} catch (Exception e) {
			throw new CarNotFoundException();
		}

		
		User loggedUser = null;
		try {
			loggedUser = userRepository.findById(loggedData.getLogguedUserId()).get();
			updatedCarDTO.setUser(loggedUser);
		} catch (Exception e) {
			throw new UserNotFoundException();
		}

		if (!isValidUserData(updatedCarDTO))
			throw new MissingFieldsException();

		car.setColor(updatedCarDTO.getColor());
		car.setLicensePlate(updatedCarDTO.getLicensePlate());
		car.setModel(updatedCarDTO.getModel());
		car.setYear(updatedCarDTO.getYear());

		Car savedCar = repository.save(car);
		return (mapToCarDTO(savedCar));
	}

	public Car findByLicensePlate(String licensePlate) {
		return repository.findBylicensePlate(licensePlate);
	}

	public void deleteCar(Long carId) throws CarNotFoundException, UserNotFoundException {

		try {
			User loggedUser = userRepository.findById(loggedData.getLogguedUserId()).get();
			Optional<Car> car = repository.findById(carId).filter(c -> c.getUser().getId() == loggedUser.getId());
			if(car.isPresent()) {
				repository.delete(car.get());
			}else {
				throw new CarNotFoundException();
			}
			
		} catch (CarNotFoundException e) {
			throw new CarNotFoundException();
		}catch (Exception e) {
			throw new UserNotFoundException();
		}

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
