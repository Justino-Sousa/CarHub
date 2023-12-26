package br.com.js.carhub.exception;

public class LoginException extends Exception {

	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Login already exists";
	}
}
