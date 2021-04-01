package com.erdem.spendit.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoredToken {
	private String email;
	private String token;
}
