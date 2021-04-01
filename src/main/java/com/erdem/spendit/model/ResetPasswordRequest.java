package com.erdem.spendit.model;

import lombok.Data;

@Data
public class ResetPasswordRequest {
	private String newPassword;
	private String uuid;
}
