package br.com.js.carhub.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.js.carhub.model.User;
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

	public User save(User user) {	

		User savedUser = repository.save(new User(null, user.getFirstName(), user.getLastName(), user.getEmail(),
				user.getBirthday(), user.getLogin(), user.getPassword(), user.getPhone(), null, user.getRole()));

		user.getCars().forEach(car ->  car.setUser(savedUser));
		carRepository.saveAll(user.getCars());

		return savedUser;
	}
	
	public Optional<User> update(Long userId, User updatedUser) {
		return repository.findById(userId).map(t -> {
			t.setBirthday(updatedUser.getBirthday());
			t.setCars(updatedUser.getCars());
			t.setEmail(updatedUser.getEmail());
			t.setFirstName(updatedUser.getFirstName());
			t.setLastName(updatedUser.getLastName());
			t.setLogin(updatedUser.getLogin());
			t.setPassword(updatedUser.getPassword());
			t.setPhone(updatedUser.getPhone());
			
			return repository.save(t);
		});
	}
	
    public void delete(Long userId) {
    	repository.deleteById(userId);
    }
}
