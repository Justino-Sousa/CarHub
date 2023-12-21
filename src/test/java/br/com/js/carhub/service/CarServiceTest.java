package br.com.js.carhub.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.js.carhub.exception.MissingFieldsException;
import br.com.js.carhub.model.Car;
import br.com.js.carhub.model.LoggedData;
import br.com.js.carhub.model.User;
import br.com.js.carhub.model.dto.CarDTO;
import br.com.js.carhub.repository.CarRepository;
import br.com.js.carhub.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
class CarServiceTest {

	@Mock
	private CarRepository carRepository;

	@Mock
	private LoggedData loggedData;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private CarService carService;

	@Test
	void testFindAll() throws Exception {

		User user = new User();
		user.setId(1L);

		Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		Mockito.when(carRepository.findByUser(any(User.class))).thenReturn(new ArrayList<>());

		List<CarDTO> result = carService.findAll();

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	@Test
	void testFindById() throws Exception {

		User user = new User();
		user.setId(1L);

		Car car = new Car();
		car.setId(2L);
		car.setUser(user);

		Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		Mockito.when(carRepository.findById(anyLong())).thenReturn(Optional.of(car));

		Optional<CarDTO> result = carService.findById(2L);

		assertTrue(result.isPresent());
		assertEquals(2L, result.get().getId());
	}

	@Test
	void testSaveValidData() throws Exception {

		CarDTO carDTO = new CarDTO();
		carDTO.setColor("Blue");
		carDTO.setLicensePlate("ABC123");
		carDTO.setModel("Sedan");
		carDTO.setYear(2022);

		User user = new User();
		user.setId(1L);

		Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		Mockito.when(carRepository.save(any(Car.class))).thenReturn(new Car());

		CarDTO result = carService.save(carDTO);

		assertNotNull(result);

	}

	@Test
	void testSaveInvalidData() {

		CarDTO carDTO = new CarDTO();

		assertThrows(MissingFieldsException.class, () -> carService.save(carDTO));
	}

	@Test
	void testUpdateValidData() throws Exception {

		Long carId = 1L;
		CarDTO updatedCarDTO = new CarDTO();
		updatedCarDTO.setColor("Red");
		updatedCarDTO.setLicensePlate("XYZ789");
		updatedCarDTO.setModel("SUV");
		updatedCarDTO.setYear(2023);

		Car existingCar = new Car();
		existingCar.setId(carId);
		existingCar.setColor("Blue");
		existingCar.setLicensePlate("ABC123");
		existingCar.setModel("Sedan");
		existingCar.setYear(2022);

		User user = new User();
		user.setId(1L);

		Mockito.when(loggedData.getLogguedUserId()).thenReturn(1L);
		Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		Mockito.when(carRepository.findById(anyLong())).thenReturn(Optional.of(existingCar));
		Mockito.when(carRepository.save(any(Car.class))).thenReturn(existingCar);

		Optional<CarDTO> result = carService.update(carId, updatedCarDTO);

		assertTrue(result.isPresent());
		assertEquals(updatedCarDTO.getColor(), result.get().getColor());
		assertEquals(updatedCarDTO.getLicensePlate(), result.get().getLicensePlate());
		assertEquals(updatedCarDTO.getModel(), result.get().getModel());
		assertEquals(updatedCarDTO.getYear(), result.get().getYear());

	}

	@Test
	void testUpdateInvalidData() {

		Long carId = 1L;
		CarDTO updatedCarDTO = new CarDTO();

		assertThrows(EntityNotFoundException.class, () -> carService.update(carId, updatedCarDTO));
	}

	@Test
	void testFindByLicensePlate() {
		// Arrange
		String licensePlate = "ABC123";
		Car existingCar = new Car();
		existingCar.setId(1L);
		existingCar.setColor("Blue");
		existingCar.setLicensePlate(licensePlate);
		existingCar.setModel("Sedan");
		existingCar.setYear(2022);

		Mockito.when(carRepository.findBylicensePlate(anyString())).thenReturn(existingCar);

		Car result = carService.findByLicensePlate(licensePlate);

		assertNotNull(result);
		assertEquals(existingCar.getId(), result.getId());
		assertEquals(existingCar.getColor(), result.getColor());
		assertEquals(existingCar.getLicensePlate(), result.getLicensePlate());
		assertEquals(existingCar.getModel(), result.getModel());
		assertEquals(existingCar.getYear(), result.getYear());

	}

	@Test
	void testFindByLicensePlateNotFound() {

		String licensePlate = "XYZ789";
		Mockito.when(carRepository.findBylicensePlate(anyString())).thenReturn(null);

		Car result = carService.findByLicensePlate(licensePlate);

		assertNull(result);
	}

	@Test
	void testDeleteCar() throws EntityNotFoundException, Exception {

		Long carId = 1L;
		Long loggedUserId = 123L;
		User loggedUser = new User();
		loggedUser.setId(loggedUserId);

		Car carToDelete = new Car();
		carToDelete.setId(carId);
		carToDelete.setUser(loggedUser);

		Mockito.when(userRepository.findById(loggedUserId)).thenReturn(Optional.of(loggedUser));
		Mockito.when(carRepository.findById(carId)).thenReturn(Optional.of(carToDelete));

		assertDoesNotThrow(() -> carService.deleteCar(carId));

		Mockito.verify(carRepository, Mockito.times(1)).deleteById(carId);
	}

	@Test
	void testDeleteCarNotFound() throws EntityNotFoundException, Exception {
		Long carId = 1L;
		Long loggedUserId = 123L;
		User loggedUser = new User();
		loggedUser.setId(loggedUserId);

		Mockito.when(userRepository.findById(loggedUserId)).thenReturn(Optional.of(loggedUser));
		Mockito.when(carRepository.findById(carId)).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> carService.deleteCar(carId));
	}

	@Test
	void testMapToCarDTO() {

		Car car = new Car();
		car.setId(1L);
		car.setYear(2022);
		car.setLicensePlate("ABC123");
		car.setModel("Toyota");
		car.setColor("Red");
		User user = new User();
		user.setId(1L);
		car.setUser(user);
		CarDTO carDTO = carService.mapToCarDTO(car);

		assertNotNull(carDTO);
		assertEquals(car.getId(), carDTO.getId());
		assertEquals(car.getYear(), carDTO.getYear());
		assertEquals(car.getLicensePlate(), carDTO.getLicensePlate());
		assertEquals(car.getModel(), carDTO.getModel());
		assertEquals(car.getColor(), carDTO.getColor());
		assertEquals(car.getUser(), carDTO.getUser());
	}

	@Test
	void testMapToCarEntity() {

		CarDTO carDTO = new CarDTO();
		carDTO.setYear(2022);
		carDTO.setLicensePlate("ABC123");
		carDTO.setModel("Toyota");
		carDTO.setColor("Red");
		User user = new User();
		user.setId(1L);
		carDTO.setUser(user);

		Car car = carService.mapToCarEntity(carDTO);

		assertNotNull(car);
		assertNull(car.getId()); // As it's a new entity
		assertEquals(carDTO.getYear(), car.getYear());
		assertEquals(carDTO.getLicensePlate(), car.getLicensePlate());
		assertEquals(carDTO.getModel(), car.getModel());
		assertEquals(carDTO.getColor(), car.getColor());
		assertEquals(carDTO.getUser(), car.getUser());
	}

	@Test
	void testIsThereCarWithPlate() {

		List<Car> carList = new ArrayList<>();
		Car car1 = new Car();
		car1.setLicensePlate("ABC123");
		carList.add(car1);

		when(carRepository.findBylicensePlate("ABC123")).thenReturn(car1);

		assertTrue(carService.isThereCarWithPlate(carList));
		assertFalse(carService.isThereCarWithPlate(new ArrayList<>()));
	}

}