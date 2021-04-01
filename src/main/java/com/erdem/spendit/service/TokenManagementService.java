package com.erdem.spendit.service;

public interface TokenManagementService {

	boolean checkActiveJWTToken(String email, String token);

	boolean validateResetPasswordUUID(String uuid);

	void saveLoggedInJWTToken(String email, String token);

	void saveResetPasswordUUID(String email, String uuid);

	void removeJWTToken(String email);

	void removeResetPasswordUUID(String email);

	String getEmailbyUUID(String uuid);

}
