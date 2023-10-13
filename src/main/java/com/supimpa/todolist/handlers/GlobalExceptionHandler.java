package com.supimpa.todolist.handlers;

import java.util.Map;
import java.util.logging.Logger;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.core.Ordered;

import com.supimpa.todolist.exceptions.AlreadyExistsException;
import com.supimpa.todolist.exceptions.EntityNotFoundException;
import com.supimpa.todolist.exceptions.InvalidAttributeValue;
import com.supimpa.todolist.exceptions.InvalidEntityException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final String ERROR = "error";

	@ResponseBody
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(AlreadyExistsException.class)
	public Map<String, String> handleAlreadyExistsException(AlreadyExistsException e) {
		return Map.of(
			ERROR, e.getMessage()
		);
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	@ExceptionHandler({ InvalidEntityException.class, InvalidAttributeValue.class })
	public Map<String, String> handleInvalidEntityException(Exception e) {
		return Map.of(
			ERROR, e.getMessage()
		);
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(EntityNotFoundException.class)
	public Map<String, String> handleEntityNotFoundException(Exception e) {
		return Map.of(
			ERROR, e.getMessage()
		);
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public Map<String, String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		return Map.of(
			ERROR, e.getMostSpecificCause().getMessage()
		);
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public Map<String, String> handleException(Exception e) {
		Logger.getLogger(GlobalExceptionHandler.class.getName()).severe(e.getClass().getName());
		Logger.getLogger(GlobalExceptionHandler.class.getName()).severe(e.getMessage());

		return Map.of(
			ERROR, "Unexpected exception"
		);
	}
}
