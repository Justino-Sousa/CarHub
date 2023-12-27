package br.com.js.carhub.model.dto;

import br.com.js.carhub.model.UserRole;
import lombok.Data;

@Data
public class RegisterDto {
	private String login;
	private String password;
	private UserRole role;
}
