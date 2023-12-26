package br.com.js.carhub.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.js.carhub.exception.EmailException;
import br.com.js.carhub.exception.InvalidFieldsException;
import br.com.js.carhub.exception.LicensePlateException;
import br.com.js.carhub.exception.LoginException;
import br.com.js.carhub.exception.MissingFieldsException;
import br.com.js.carhub.exception.UserNotFoundException;
import br.com.js.carhub.model.Car;
import br.com.js.carhub.model.User;
import br.com.js.carhub.model.UserRole;
import br.com.js.carhub.model.dto.UserDTO;
import br.com.js.carhub.repository.CarRepository;
import br.com.js.carhub.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private CarRepository carRepository;

	@Autowired
	private CarService carService;

	public List<User> findAll() {
		return repository.findAll();
	}

	public UserDTO findById(Long userId) throws UserNotFoundException {
		User user = null;
		try {
			user = repository.findById(userId).get();
		} catch (Exception e) {
			throw new UserNotFoundException();
		}
		return mapUserToUserDTO(user);
	}

	public UserDTO save(UserDTO userDto) throws InvalidFieldsException, LoginException, EmailException,
			LicensePlateException, MissingFieldsException {

		if (userDto == null) {
			throw new InvalidFieldsException();
		}

		if (repository.findByLogin(userDto.getLogin()) != null) {
			throw new LoginException();
		}

		if (repository.findByEmail(userDto.getEmail()) != null) {
			throw new EmailException();
		}

		if (carService.isThereCarWithPlate(userDto.getCars())) {
			throw new LicensePlateException();
		}

		if (!isValidUserData(userDto))
			throw new MissingFieldsException();

		String encryptedPassword = new BCryptPasswordEncoder().encode(userDto.getPassword());

		User savedUser = repository.save(
				new User(null, userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), userDto.getBirthday(),
						userDto.getLogin(), encryptedPassword, userDto.getPhone(), null, UserRole.USER));

		userDto.getCars().forEach(car -> car.setUser(savedUser));
		carRepository.saveAll(userDto.getCars());

		List<Car> savedCars = carRepository.findByUser(savedUser);

		return new UserDTO(savedUser.getId(), savedUser.getFirstName(), savedUser.getLastName(), savedUser.getEmail(),
				savedUser.getBirthday(), savedUser.getLogin(), savedUser.getPassword(), savedUser.getPhone(),
				savedCars);
	}

	public UserDTO update(Long userId, UserDTO updatedUser)
			throws InvalidFieldsException, UserNotFoundException, LoginException, EmailException {

		if (updatedUser == null) {
			throw new InvalidFieldsException();
		}

		User usr = repository.findById(userId).orElse(null);

		if (usr == null) {
			throw new UserNotFoundException();
		}

		if (usr.getUsername().equals(updatedUser.getLogin()) && usr.getId() != userId) {
			throw new LoginException();
		}

		if (usr.getEmail().equals(updatedUser.getEmail()) && usr.getId() != userId) {
			throw new EmailException();
		}

		User user = repository.findById(userId).map(t -> {

			if (updatedUser.getPassword() != null && !t.getPassword().equals(updatedUser.getPassword())) {
				String encryptedPassword = new BCryptPasswordEncoder().encode(updatedUser.getPassword());
				t.setPassword(encryptedPassword);
			}
			t.setBirthday(updatedUser.getBirthday());
			t.setEmail(updatedUser.getEmail());
			t.setFirstName(updatedUser.getFirstName());
			t.setLastName(updatedUser.getLastName());
			t.setLogin(updatedUser.getLogin());
			t.setPhone(updatedUser.getPhone());

			return repository.save(t);
		}).orElseThrow(() -> new UserNotFoundException());

		return mapUserToUserDTO(user);

	}

	public void delete(Long userId) throws UserNotFoundException {

		Optional<User> user = repository.findById(userId);
		if (user.isPresent()) {
			repository.deleteById(userId);
		} else {
			throw new UserNotFoundException();
		}

	}

	public UserDetails findByLogin(String login) throws UserNotFoundException {
		try {
			return repository.findByLogin(login);
		} catch (Exception e) {
			throw new UserNotFoundException();
		}
	}

	public User findByEmail(String email) throws UserNotFoundException {
		try {
			return repository.findByEmail(email);
		} catch (Exception e) {
			throw new UserNotFoundException();
		}

	}

	private boolean isValidUserData(UserDTO user) {
		return user != null && isNotEmpty(user.getFirstName()) && isNotEmpty(user.getLastName())
				&& isNotEmpty(user.getEmail()) && isNotEmpty(user.getLogin()) && isNotEmpty(user.getPassword())
				&& isNotEmpty(user.getPhone());
	}

	private boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	public static UserDTO mapUserToUserDTO(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setFirstName(user.getFirstName());
		userDTO.setLastName(user.getLastName());
		userDTO.setEmail(user.getEmail());
		userDTO.setBirthday(user.getBirthday());
		userDTO.setLogin(user.getLogin());
		userDTO.setPassword(user.getPassword());
		userDTO.setPhone(user.getPhone());
		userDTO.setCars(user.getCars());
		return userDTO;
	}

	public static User mapUserDTOToUser(UserDTO userDTO) {
		User user = new User();
		user.setId(userDTO.getId());
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setEmail(userDTO.getEmail());
		user.setBirthday(userDTO.getBirthday());
		user.setLogin(userDTO.getLogin());
		user.setPassword(userDTO.getPassword());
		user.setPhone(userDTO.getPhone());
		user.setCars(userDTO.getCars());
		return user;
	}
}
