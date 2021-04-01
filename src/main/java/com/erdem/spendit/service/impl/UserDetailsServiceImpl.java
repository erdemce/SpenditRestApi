package com.erdem.spendit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.erdem.spendit.model.User;
import com.erdem.spendit.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = userService.findByEmail(email).orElse(null);

		if (user == null) {
			throw new UsernameNotFoundException("Email or Passwort is wrong");
		}

		return user;
	}

}
