package br.com.js.carhub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.js.carhub.model.Car;
import br.com.js.carhub.model.User;

@Repository
public interface CarRepository extends JpaRepository<Car, Long>{
	List<Car> findByUser(User user);
	Car findBylicensePlate(String string);
}
