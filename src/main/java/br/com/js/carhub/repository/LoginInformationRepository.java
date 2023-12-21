package br.com.js.carhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.js.carhub.model.LoginInformation;

@Repository
public interface LoginInformationRepository extends CustomQuery, JpaRepository<LoginInformation, Long>{
}
