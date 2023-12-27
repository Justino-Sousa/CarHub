package br.com.js.carhub.repository;

import java.util.List;

import br.com.js.carhub.model.LoginInformation;

public interface CustomQuery {
	List<LoginInformation> findByUser(Long id);
}
