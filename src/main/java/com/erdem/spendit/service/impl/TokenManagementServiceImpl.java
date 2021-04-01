package com.erdem.spendit.service.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.erdem.spendit.model.ResetPasswordValidationUUID;
import com.erdem.spendit.model.StoredToken;
import com.erdem.spendit.service.TokenManagementService;

import lombok.extern.log4j.Log4j2;

/**
 * In this Application, UUID and JWT Tokens are stored in Map in memory. This is
 * purely for task purpose. Of course, a real application would be based on an
 * another Crud Class that implements TokenManagementService and stores tokens
 * in a real database.
 * 
 * @author Erdem Taskin
 *
 */
@Log4j2
@Service
public class TokenManagementServiceImpl implements TokenManagementService {

	private Map<String, StoredToken> storedTokens = new HashMap<>();
	private Map<String, ResetPasswordValidationUUID> storedUUIDs = new HashMap<>();

	@Override
	public boolean checkActiveJWTToken(String email, String token) {
		return this.storedTokens.get(email).getToken().equals(token);
	}

	@Override
	public boolean validateResetPasswordUUID(String uuid) {
		return storedUUIDs.get(getEmailbyUUID(uuid)).getExpiryTime().isAfter(Instant.now());
	}

	@Override
	public void saveLoggedInJWTToken(String email, String token) {
		this.storedTokens.put(email, new StoredToken(email, token));

	}

	@Override
	public void removeJWTToken(String email) {
		this.storedTokens.remove(email);
	}

	@Override
	public void removeResetPasswordUUID(String email) {
		this.storedUUIDs.remove(email);
	}

	@Override
	public void saveResetPasswordUUID(String email, String uuid) {
		this.storedUUIDs.put(email, new ResetPasswordValidationUUID(uuid));
		sendEmail(email, uuid);
	}

	private void sendEmail(String email, String uuid) {

		log.info("Email to:" + email);
		log.info("This link should be consumed for reset password");
		log.info("http://localhost:8080/public/reset_password/" + uuid);
	}

	@Override
	public String getEmailbyUUID(String uuid) {
		return this.storedUUIDs.entrySet().stream().filter(uuidObj -> uuidObj.getValue().getUuid().equals(uuid))
				.findFirst().get().getKey();
	}
}
