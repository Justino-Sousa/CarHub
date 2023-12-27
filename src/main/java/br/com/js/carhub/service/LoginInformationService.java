package br.com.js.carhub.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.js.carhub.model.LoginInformation;
import br.com.js.carhub.model.User;
import br.com.js.carhub.repository.LoginInformationRepository;
import br.com.js.carhub.repository.UserRepository;

@Service
public class LoginInformationService {

	@Autowired
	LoginInformationRepository informationRepository;
	
	@Autowired
	UserRepository userRepository;
	
	public void updateInformation(LoginInformation information) {
		List<LoginInformation> informationList = informationRepository.findByUser(userRepository.findById(information.getIdUser()).get().getId());
		if(!informationList.isEmpty()) {
			LoginInformation log = informationList.get(0);
			log.setLastUserlogin(information.getLastUserlogin());
			informationRepository.save(log);
		}else {
			informationRepository.save(information);
		}
	}
	
	public LoginInformation getInformationByUser(User user) {
		LoginInformation response = null;
		if(!informationRepository.findByUser(user.getId()).isEmpty()) {
			response = informationRepository.findByUser(user.getId()).get(0);
		}
		return response;
	}
}
