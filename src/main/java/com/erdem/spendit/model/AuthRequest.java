package com.erdem.spendit.model;

import lombok.Data;

@Data
public class AuthRequest {
	private String email;
	private String password;
}