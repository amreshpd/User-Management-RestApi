package com.nt.binding;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount {
	private Integer userId;
	private String name;
	private String email;
	private Long contactNumber;
	private String gender="Female";
	private LocalDate dob=LocalDate.now();
	private Long adharNumber;
}
