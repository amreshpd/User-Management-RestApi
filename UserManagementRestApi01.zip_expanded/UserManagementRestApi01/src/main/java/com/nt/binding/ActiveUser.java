package com.nt.binding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveUser {

	private String registeredEmail;
	private String tempPassword;
	private String newPassword;
	private String confirmPassword;
	
	
	
}
