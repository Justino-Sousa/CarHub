package br.com.js.carhub.exception;

public class LicensePlateException extends Exception{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getMessage() {
		return "License plate already exists";
	}
}
