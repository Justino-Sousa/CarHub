package br.com.js.carhub.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import br.com.js.carhub.repository.UserRepository;

@Component
public class LoggedData {

	@Autowired
	UserRepository repository;

	private User user;

	public Object getLogguedUser() throws Exception {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			user = (User) repository.findByLogin(((UserDetails) principal).getUsername());

		} else {
			throw new Exception("Unauthorized - invalid session");
		}

		return user;
	}
	
	public Long getLogguedUserId() throws Exception {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Long id = null;
		if (principal instanceof UserDetails) {
			user = (User) repository.findByLogin(((UserDetails) principal).getUsername());
			id = user.getId();

		} else {
			throw new Exception("Unauthorized - invalid session");
		}

		return id;
	}

}
