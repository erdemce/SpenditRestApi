
package com.erdem.spendit.ExceptionHandling;

import java.rmi.ServerException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;


/**
 * to globalize our error handling logic using the @ControllerAdvice annotation
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<?> responseUsernameNotFoundException(UsernameNotFoundException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ServerException.class)
	public ResponseEntity<?> responseServerException(ServerException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> responseBadCredentialsException(BadCredentialsException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<?> responseExpiredJwtException(ExpiredJwtException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> ResponseForRestException(Exception e) {
		return new ResponseEntity<>("There is something Wrong", HttpStatus.FORBIDDEN);
	}
}