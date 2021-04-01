package com.erdem.spendit.model;

import lombok.Data;

@Data
public class ChangePasswordRequest {
	private String oldPassword;
	private String newPassword;
}