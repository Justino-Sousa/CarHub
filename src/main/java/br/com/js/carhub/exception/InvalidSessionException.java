package br.com.js.carhub.exception;

public class InvalidSessionException extends Exception {

	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Unauthorized - invalid session";
	}
}
