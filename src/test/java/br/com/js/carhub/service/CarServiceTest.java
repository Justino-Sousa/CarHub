package br.com.js.carhub.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.js.carhub.model.Car;
import br.com.js.carhub.model.LoggedData;
import br.com.js.carhub.model.User;
import br.com.js.carhub.model.dto.CarDTO;
import br.com.js.carhub.model.dto.UserDTO;
import br.com.js.carhub.repository.CarRepository;
import br.com.js.carhub.repository.UserRepository;

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

	@InjectMocks
	private UserService userService;

	@Test
	void testFindAll() throws Exception {

		User loggedUser = new User();
		when(loggedData.getLogguedUserId()).thenReturn(1L);
		when(userRepository.findById(1L)).thenReturn(Optional.of(loggedUser));
		when(carRepository.findByUser(loggedUser)).thenReturn(Collections.emptyList());

		assertDoesNotThrow(() -> carService.findAll());

	}

	@Test
	void testFindById() throws Exception {

		Long carId = 1L;

		UserDTO userDTO = new UserDTO();
		userDTO.setBirthday(LocalDate.now());
		userDTO.setCars(new ArrayList<>());
		userDTO.setEmail("jsnjunior@test.com");
		userDTO.setFirstName("Justino");
		userDTO.setLastName("Sousa");
		userDTO.setId(1L);
		userDTO.setLogin("jsnjunior");
		userDTO.setPassword("password");
		userDTO.setPhone("2002020");

		@SuppressWarnings("static-access")
		User user = userService.mapUserDTOToUser(userDTO);

		CarDTO carDTO = new CarDTO();
		carDTO.setColor("branco");
		carDTO.setId(1L);
		carDTO.setLicensePlate("PLA202K");
		carDTO.setModel("HB20");
		carDTO.setUser(user);

		Car car = carService.mapToCarEntity(carDTO);

		when(loggedData.getLogguedUserId()).thenReturn(1L);
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(carRepository.findById(carId)).thenReturn(Optional.of(car));

		assertDoesNotThrow(() -> carService.findById(carId));

	}

	@Test
	void testSave() throws Exception {

		UserDTO userDTO = new UserDTO();
		userDTO.setBirthday(LocalDate.now());
		userDTO.setCars(new ArrayList<>());
		userDTO.setEmail("jsnjunior@test.com");
		userDTO.setFirstName("Justino");
		userDTO.setLastName("Sousa");
		userDTO.setId(1L);
		userDTO.setLogin("jsnjunior");
		userDTO.setPassword("password");
		userDTO.setPhone("2002020");

		@SuppressWarnings("static-access")
		User user = userService.mapUserDTOToUser(userDTO);

		CarDTO carDTO = new CarDTO();
		carDTO.setColor("branco");
		carDTO.setId(1L);
		carDTO.setLicensePlate("PLA202K");
		carDTO.setModel("HB20");
		carDTO.setUser(user);

		when(loggedData.getLogguedUserId()).thenReturn(1L);
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(carRepository.save(any())).thenReturn(new Car());

		assertDoesNotThrow(() -> carService.save(carDTO));

	}

	@Test
	void testUpdate() throws Exception {

		UserDTO userDTO = new UserDTO();
		userDTO.setBirthday(LocalDate.now());
		userDTO.setCars(new ArrayList<>());
		userDTO.setEmail("jsnjunior@test.com");
		userDTO.setFirstName("Justino");
		userDTO.setLastName("Sousa");
		userDTO.setId(1L);
		userDTO.setLogin("jsnjunior");
		userDTO.setPassword("password");
		userDTO.setPhone("2002020");

		@SuppressWarnings("static-access")
		User user = userService.mapUserDTOToUser(userDTO);

		CarDTO carDTO = new CarDTO();
		carDTO.setColor("branco");
		carDTO.setId(1L);
		carDTO.setLicensePlate("PLA202K");
		carDTO.setModel("HB20");
		carDTO.setUser(user);

		Car car = carService.mapToCarEntity(carDTO);
		Long carId = 1L;

		when(loggedData.getLogguedUserId()).thenReturn(1L);
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(carRepository.findById(carId)).thenReturn(Optional.of(car));
		when(carRepository.save(any())).thenReturn(new Car());

		assertDoesNotThrow(() -> carService.update(carId, carDTO));

	}

	@Test
	void testDeleteCar() throws Exception {

		Long carId = 1L;

		UserDTO userDTO = new UserDTO();
		userDTO.setBirthday(LocalDate.now());
		userDTO.setCars(new ArrayList<>());
		userDTO.setEmail("jsnjunior@test.com");
		userDTO.setFirstName("Justino");
		userDTO.setLastName("Sousa");
		userDTO.setId(1L);
		userDTO.setLogin("jsnjunior");
		userDTO.setPassword("password");
		userDTO.setPhone("2002020");

		@SuppressWarnings("static-access")
		User user = userService.mapUserDTOToUser(userDTO);

		CarDTO carDTO = new CarDTO();
		carDTO.setColor("branco");
		carDTO.setId(1L);
		carDTO.setLicensePlate("PLA202K");
		carDTO.setModel("HB20");
		carDTO.setUser(user);

		Car car = carService.mapToCarEntity(carDTO);

		when(loggedData.getLogguedUserId()).thenReturn(1L);
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(carRepository.findById(carId)).thenReturn(Optional.of(car));

		assertDoesNotThrow(() -> carService.deleteCar(carId));

	}

}