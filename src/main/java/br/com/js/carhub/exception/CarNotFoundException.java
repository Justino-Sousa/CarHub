package br.com.js.carhub.exception;

public class CarNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getMessage() {
		return "car not found";
	}

}
