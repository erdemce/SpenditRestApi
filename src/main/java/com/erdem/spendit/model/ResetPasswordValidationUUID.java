package com.erdem.spendit.model;

import java.time.Duration;
import java.time.Instant;

import lombok.Data;

@Data
public class ResetPasswordValidationUUID {

	public static final int RESET_PASSWORD_UUID_VALIDITY = 15; // valid for 15 minute

	private String uuid;

	private Instant expiryTime;

	public ResetPasswordValidationUUID(String uuid) {
		this.uuid = uuid;
		this.expiryTime = calculateExpirationTime(RESET_PASSWORD_UUID_VALIDITY);
	}

	private Instant calculateExpirationTime(final int expiryTime) {
		Duration duration = Duration.ofMinutes(expiryTime);
		return Instant.now().plus(duration);
	}

}