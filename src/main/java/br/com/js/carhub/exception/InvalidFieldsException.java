package br.com.js.carhub.exception;

public class InvalidFieldsException extends Exception {

	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Invalid fields";
	}
}
