package br.com.js.carhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.js.carhub.model.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long>{

}
