package br.com.js.carhub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorResponse handleUserNotFoundException(UserNotFoundException ex) {
		return new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
	}

	@ExceptionHandler(CarNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorResponse handleCarNotFound(CarNotFoundException ex) {
		return new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
	}

	@ExceptionHandler(InvalidFieldsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse handleInvalidFieldsException(InvalidFieldsException ex) {
		return new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler(MissingFieldsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse handleMissingFieldsException(MissingFieldsException ex) {
		return new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler(LicensePlateException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse handleLicensePlateException(LicensePlateException ex) {
		return new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler(EmailException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse handleEmailException(EmailException ex) {
		return new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler(LoginException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse handleLoginException(LoginException ex) {
		return new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
	}
	
	@ExceptionHandler(UnauthorizedErrorException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public ErrorResponse handleUnauthorizedException(UnauthorizedErrorException ex) {
		return new ErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
	}

}