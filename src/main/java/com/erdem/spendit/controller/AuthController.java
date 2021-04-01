package com.erdem.spendit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.erdem.spendit.config.JwtTokenUtil;
import com.erdem.spendit.model.AuthRequest;
import com.erdem.spendit.model.AuthResponse;
import com.erdem.spendit.model.User;
import com.erdem.spendit.service.TokenManagementService;
import com.erdem.spendit.service.UserService;

@RestController
public class AuthController {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UserService userService;

	@Autowired
	private TokenManagementService tokenManagementService;
	
	
	/**
	 * Private end point for the sake of test.
	 * Only authenticated user can reach this end point
	 */
	@GetMapping("/auth/private")
	public ResponseEntity<String> privateArea() {

		return new ResponseEntity<>("You are in the private area now", HttpStatus.ACCEPTED);
	}
	
	/**
	 * Public End point to log in. 
	 * @param authRequest
	 * @return a JWT token valid 1 hour and  should be used in the header of all request
	 * @throws Exception if there is a problem with authentication.
	 * And this Exception will be handled by our Exception handling structure.
	 */
	@PostMapping("/public/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {

		userService.authenticate(authRequest.getEmail(), authRequest.getPassword());

		UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());

		String token = jwtTokenUtil.generateToken(userDetails);
		//In order to check the active user, we are adding our token in memory Tokens Map.
		///It is important for the logout process with JWT
		tokenManagementService.saveLoggedInJWTToken(authRequest.getEmail(), token);

		return new ResponseEntity<>(new AuthResponse(token), HttpStatus.ACCEPTED);
	}
	
	/**
	 * In this public end point anyone can create an user account with email and password.
	 * @param authRequest
	 * @return
	 */
	@PostMapping("/public/register")
	public ResponseEntity<?> signUp(@RequestBody AuthRequest authRequest){
		if (userService.hasAlreadyRegistered(authRequest.getEmail())) {
			return new ResponseEntity<>("Already User", HttpStatus.CONFLICT);
		} else if (userService.save(authRequest)) {
			return new ResponseEntity<>("User created", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * In this private end point user can logged out. we remove the related token from our jwt token list in memory.
	 * even if jwt token is not expired; user can not use this token again and need log in again.
	 * Since there is no way to destroy the given JWT token, we are using this architecture for the log out process  
	 * @return
	 */
	@PostMapping("/auth/logout")
	public ResponseEntity<?> logout() {
		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
			User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			tokenManagementService.removeJWTToken(loginUser.getEmail());
			return new ResponseEntity<>("You are successfully logged Out", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("UnAutharized", HttpStatus.FORBIDDEN);
		}
	}
}