package br.com.js.carhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import br.com.js.carhub.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	UserDetails findByLogin(String login);
}
