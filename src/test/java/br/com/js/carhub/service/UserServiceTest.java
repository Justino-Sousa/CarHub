package br.com.js.carhub.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.js.carhub.exception.MissingFieldsException;
import br.com.js.carhub.model.Car;
import br.com.js.carhub.model.User;
import br.com.js.carhub.model.dto.UserDTO;
import br.com.js.carhub.repository.CarRepository;
import br.com.js.carhub.repository.UserRepository;

@SpringBootTest
 class UserServiceTest {

	
	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;
	
	@Mock
	private CarRepository carRepository;

	@Test
	 void testFindAll() {

		List<User> users = Arrays.asList(new User(), new User());
		when(userRepository.findAll()).thenReturn(users);

		List<User> result = userService.findAll();

		assertEquals(users, result);
	}

	@Test
	 void testFindById() {

		User user = new User();
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		Optional<User> result = userService.findById(1L);

		assertTrue(result.isPresent());
		assertEquals(user, result.get());
	}

	@Test
	 void testFindById_NotFound() {

		when(userRepository.findById(1L)).thenReturn(Optional.empty());

		Optional<User> result = userService.findById(1L);

		assertFalse(result.isPresent());
	}

	@Test
	 void testSave() throws MissingFieldsException {

		UserDTO userDTO = createSampleUserDTO();
		User savedUser = createSampleUser();
		when(userRepository.save(any())).thenReturn(savedUser);
		when(carRepository.saveAll(any())).thenReturn(userDTO.getCars());

		UserDTO result = userService.save(userDTO);

		assertEquals(savedUser.getId(), result.getId());
		assertEquals(savedUser.getFirstName(), result.getFirstName());
		assertEquals(savedUser.getLastName(), result.getLastName());
		assertEquals(savedUser.getEmail(), result.getEmail());
		assertEquals(savedUser.getBirthday(), result.getBirthday());
		assertEquals(savedUser.getLogin(), result.getLogin());
		assertEquals(savedUser.getPassword(), result.getPassword());
		assertEquals(savedUser.getPhone(), result.getPhone());
	}

	@Test
	 void testUpdate() {

		UserDTO updatedUserDTO = createSampleUserDTO();
		User existingUser = createSampleUser();
		when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
		when(userRepository.save(any())).thenReturn(existingUser);

		Optional<User> result = userService.update(1L, updatedUserDTO);

		assertTrue(result.isPresent());
		assertEquals(existingUser.getId(), result.get().getId());
		assertEquals(existingUser.getFirstName(), result.get().getFirstName());
		assertEquals(existingUser.getLastName(), result.get().getLastName());
		assertEquals(existingUser.getEmail(), result.get().getEmail());
		assertEquals(existingUser.getBirthday(), result.get().getBirthday());
		assertEquals(existingUser.getLogin(), result.get().getLogin());
		assertEquals(existingUser.getPassword(), result.get().getPassword());
		assertEquals(existingUser.getPhone(), result.get().getPhone());
	}

	@Test
	 void testUpdate_NotFound() {

		when(userRepository.findById(1L)).thenReturn(Optional.empty());

		Optional<User> result = userService.update(1L, createSampleUserDTO());

		assertFalse(result.isPresent());
	}

	@Test
	 void testDelete() {
		userService.delete(1L);
		verify(userRepository, times(1)).deleteById(1L);
	}

	@Test
	 void testFindByLogin() {
		UserDetails userDetails = createSampleUser();
		when(userRepository.findByLogin("username")).thenReturn(userDetails);
		UserDetails result = userService.findByLogin("username");
		assertEquals(userDetails, result);
	}

	@Test
	 void testFindByEmail() {

		User user = createSampleUser();
		when(userRepository.findByEmail("user@example.com")).thenReturn(user);
		User result = userService.findByEmail("user@example.com");
		assertEquals(user, result);
	}

	private UserDTO createSampleUserDTO() {
		Car car = new Car();
		car.setId(1L);
		car.setYear(2022);
		car.setLicensePlate("ABC123");
		car.setModel("Model X");
		car.setColor("Black");

		UserDTO userDTO = new UserDTO();
		userDTO.setFirstName("John");
		userDTO.setLastName("Doe");
		userDTO.setEmail("john.doe@example.com");
		userDTO.setBirthday(LocalDate.of(1990, 1, 1));
		userDTO.setLogin("john.doe");
		userDTO.setPassword("password");
		userDTO.setPhone("123456789");
		userDTO.setCars(Arrays.asList(car));

		return userDTO;
	}

	private User createSampleUser() {
		User user = new User();
		user.setId(1L);
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setEmail("john.doe@example.com");
		user.setBirthday(LocalDate.of(1990, 1, 1));
		user.setLogin("john.doe");
		user.setPassword(new BCryptPasswordEncoder().encode("password"));
		user.setPhone("123456789");
		user.setCars(Arrays.asList(new Car()));

		return user;
	}
}
