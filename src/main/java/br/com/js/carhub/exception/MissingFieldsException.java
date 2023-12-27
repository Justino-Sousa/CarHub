package br.com.js.carhub.exception;

public class MissingFieldsException extends Exception {

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getMessage() {
		return "Missing fields";
	}
}
