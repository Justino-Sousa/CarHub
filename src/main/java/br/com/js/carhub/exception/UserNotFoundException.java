package br.com.js.carhub.exception;

public class UserNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "user not found";
	}
}

