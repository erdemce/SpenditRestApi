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

	@GetMapping("/auth/private")
	public ResponseEntity<String> privateArea() {

		return new ResponseEntity<>("You are in the private area now", HttpStatus.ACCEPTED);
	}

	@PostMapping("/public/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {

		userService.authenticate(authRequest.getEmail(), authRequest.getPassword());

		UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());

		String token = jwtTokenUtil.generateToken(userDetails);
		tokenManagementService.saveLoggedInJWTToken(authRequest.getEmail(), token);

		return new ResponseEntity<>(new AuthResponse(token), HttpStatus.ACCEPTED);
	}

	@PostMapping("/public/register")
	public ResponseEntity<?> signUp(@RequestBody AuthRequest authRequest) throws Exception {
		if (userService.hasAlreadyRegistered(authRequest.getEmail())) {
			return new ResponseEntity<>("Already User", HttpStatus.CONFLICT);
		} else if (userService.save(authRequest)) {
			return new ResponseEntity<>("User created", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

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