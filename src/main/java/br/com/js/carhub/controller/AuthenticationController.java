package br.com.js.carhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.js.carhub.model.User;
import br.com.js.carhub.model.dto.AuthenticationDTO;
import br.com.js.carhub.model.dto.RegisterDto;
import br.com.js.carhub.model.dto.TokenResponseDTO;
import br.com.js.carhub.repository.UserRepository;
import br.com.js.carhub.service.TokenService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TokenService tokenService;

	@PostMapping("/login")
	public ResponseEntity<TokenResponseDTO> login(@RequestBody AuthenticationDTO data) {

		var usernamePassword = new UsernamePasswordAuthenticationToken(data.getLogin(), data.getPassword());
		var auth = this.authenticationManager.authenticate(usernamePassword);	
		var token = tokenService.generateToken((User) auth.getPrincipal());
		return ResponseEntity.ok(new TokenResponseDTO(token));
	}
	
	@PostMapping("/register")
	public ResponseEntity<AuthenticationDTO> register(@RequestBody RegisterDto data) {
		if(this.userRepository.findByLogin(data.getLogin()) != null) return ResponseEntity.badRequest().build();
		String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());
		User user = new User(data.getLogin(), encryptedPassword, data.getRole());
		this.userRepository.save(user);
		return ResponseEntity.ok().build();
	}

}
