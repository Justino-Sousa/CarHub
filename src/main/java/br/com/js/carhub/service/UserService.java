package br.com.js.carhub.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.js.carhub.exception.MissingFieldsException;
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

	public List<User> findAll() {
		return repository.findAll();
	}

	public Optional<User> findById(Long userId) {
		return repository.findById(userId);
	}

	public UserDTO save(UserDTO userDto) throws MissingFieldsException {

		if (!isValidUserData(userDto))
			throw new MissingFieldsException();

		String encryptedPassword = new BCryptPasswordEncoder().encode(userDto.getPassword());

		User savedUser = repository.save(
				new User(null, userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), userDto.getBirthday(),
						userDto.getLogin(), encryptedPassword, userDto.getPhone(), null, UserRole.USER));
	
		userDto.getCars().forEach(car -> car.setUser(savedUser));
		carRepository.saveAll(userDto.getCars());
		
		List<Car> savedCars =  carRepository.findByUser(savedUser);
		 
		return new UserDTO(savedUser.getId(), savedUser.getFirstName(), savedUser.getLastName(), savedUser.getEmail(),
				savedUser.getBirthday(), savedUser.getLogin(), savedUser.getPassword(), savedUser.getPhone(),
				savedCars);
	}

	public Optional<User> update(Long userId, UserDTO updatedUser) {
		
		return repository.findById(userId).map(t -> {
			String encryptedPassword = new BCryptPasswordEncoder().encode(updatedUser.getPassword());
			t.setBirthday(updatedUser.getBirthday());
			t.setCars(updatedUser.getCars());
			t.setEmail(updatedUser.getEmail());
			t.setFirstName(updatedUser.getFirstName());
			t.setLastName(updatedUser.getLastName());
			t.setLogin(updatedUser.getLogin());
			t.setPassword(encryptedPassword);
			t.setPhone(updatedUser.getPhone());

			return repository.save(t);
		});
	}

	public void delete(Long userId) {
		repository.deleteById(userId);
	}

	public UserDetails findByLogin(String login) {
		return repository.findByLogin(login);
	}

	public User findByEmail(String email) {
		return repository.findByEmail(email);
	}

	private boolean isValidUserData(UserDTO user) {
		return user != null 
				&& isNotEmpty(user.getFirstName()) 
				&& isNotEmpty(user.getLastName())
				&& isNotEmpty(user.getEmail()) 
				&& isNotEmpty(user.getLogin()) 
				&& isNotEmpty(user.getPassword())
				&& isNotEmpty(user.getPhone());
	}

	private boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}
}
