package br.com.js.carhub.model.dto;

import lombok.Data;

@Data
public class AuthenticationDTO {
	private String login;
	private String password;
}
