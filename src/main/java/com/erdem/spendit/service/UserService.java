package com.erdem.spendit.service;

import java.util.Optional;
import com.erdem.spendit.model.AuthRequest;
import com.erdem.spendit.model.ChangePasswordRequest;
import com.erdem.spendit.model.ResetPasswordRequest;
import com.erdem.spendit.model.User;

public interface UserService {

	void authenticate(String email, String password) throws Exception;

	void updatePassword(String email, ChangePasswordRequest changePasswordRequest) throws Exception;

	boolean hasAlreadyRegistered(String email);

	Optional<User> findByEmail(String email);

	boolean save(AuthRequest newUser);

	void resetPassword(String email, ResetPasswordRequest resetPasswordRequest) throws Exception;

}
