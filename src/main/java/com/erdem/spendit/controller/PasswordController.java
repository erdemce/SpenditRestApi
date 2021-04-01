package com.erdem.spendit.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.erdem.spendit.model.AuthRequest;
import com.erdem.spendit.model.ChangePasswordRequest;
import com.erdem.spendit.model.ResetPasswordRequest;
import com.erdem.spendit.model.User;
import com.erdem.spendit.service.TokenManagementService;
import com.erdem.spendit.service.UserService;

@RestController
public class PasswordController {

	@Autowired
	private UserService userService;

	@Autowired
	private TokenManagementService tokenManagementService;

	@PostMapping("/auth/change_password")
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
		User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			userService.updatePassword(loginUser.getEmail(), changePasswordRequest);
			return new ResponseEntity<>("Password Successfuly Changed", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/public/forgot_password")
	public ResponseEntity<?> sendResetPasswordValidationLink(@RequestBody AuthRequest authRequest) throws Exception {

		if (userService.hasAlreadyRegistered((authRequest.getEmail()))) {
			String uuid = UUID.randomUUID().toString();
			tokenManagementService.saveResetPasswordUUID(authRequest.getEmail(), uuid);

			return new ResponseEntity<>("Reset password Link is sent your email", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("There is no account related to this email", HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/public/reset_password")
	public ResponseEntity<?> resetPasswordWithToken(@RequestBody ResetPasswordRequest resetPasswordRequest)
			throws Exception {
		if (tokenManagementService.validateResetPasswordUUID(resetPasswordRequest.getUuid())) {
			userService.resetPassword(tokenManagementService.getEmailbyUUID(resetPasswordRequest.getUuid()),
					resetPasswordRequest);
			return new ResponseEntity<>("Your password is reset successfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("You haven't got a Authority to reset Password", HttpStatus.UNAUTHORIZED);
		}
	}

}
