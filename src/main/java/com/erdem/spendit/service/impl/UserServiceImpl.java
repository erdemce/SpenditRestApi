package com.erdem.spendit.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.erdem.spendit.model.AuthRequest;
import com.erdem.spendit.model.ChangePasswordRequest;
import com.erdem.spendit.model.ResetPasswordRequest;
import com.erdem.spendit.model.User;
import com.erdem.spendit.service.UserService;

/**
 * In this Application, users are stored in Map<String, User> in memory. This is
 * purely for task purpose. Of course, a real application would be based on an
 * another Crud Class that implements UserService and stores users in a real
 * database.
 * 
 * @author Erdem Taskin
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private AuthenticationManager authManager;

	private Map<String, User> users = new HashMap<>();

	@Override
	public void authenticate(String email, String password) throws Exception {
		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		} catch (DisabledException e) {
			throw new DisabledException("USER_DISABLED", e);

		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Email or Password is wrong", e);
		}
	}

	@Override
	public boolean save(AuthRequest newUser) {
		users.put(newUser.getEmail(),
				new User(newUser.getEmail(), new BCryptPasswordEncoder().encode(newUser.getPassword())));
		return true;
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return users.values().stream().filter(u -> Objects.equals(email, u.getEmail())).findFirst();
	}

	@Override
	public void updatePassword(String email, ChangePasswordRequest changePasswordRequest) throws Exception {
		Optional<User> user = findByEmail(email);
		if (user != null) {
			authenticate(email, changePasswordRequest.getOldPassword());
			user.get().setPassword(new BCryptPasswordEncoder().encode(changePasswordRequest.getNewPassword()));
		} else {
			throw new UsernameNotFoundException("This Email can not be found");
		}
	}

	@Override
	public void resetPassword(String email, ResetPasswordRequest resetPasswordRequest) throws Exception {
		Optional<User> user = findByEmail(email);
		if (user != null) {
			user.get().setPassword(new BCryptPasswordEncoder().encode(resetPasswordRequest.getNewPassword()));
		} else {
			throw new UsernameNotFoundException("This Email can not be found");
		}

	}

	@Override
	public boolean hasAlreadyRegistered(String email) {
		return this.users.containsKey(email);
	}

}
