package com.nt.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "JRTP_USER_MASTER")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMaster {
	@Id
    @Column(name = "USER_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	@Column(name = "USER_NAME",length = 50)
	private String userName;
	@Column(name = "PASSWORD", length = 40)
	private String password;
	@Column(name = "EMAIL",length = 45)
	private String email;
	@Column(name = "GENDER",length = 15)
	private String Gender;
	private String name;
	@Column(name = "CONTACT_NUMBER",length = 15)
	private Long contactNumber;
	@Column(name = "ADHAR_NUMBER",length = 15)
	private Long adharNumber;
	@Column(name = "STATUS",length = 15)
	private String status;
	@Column(name = "DATE_OF_BIRTH")
	private LocalDate dateOfBirth;
	
	@CreationTimestamp()
	@Column(name = "CREATION_TIME",insertable = true,updatable = false)
	private LocalDateTime creationTime;
	@UpdateTimestamp
	@Column(name = "UPDATION_TIME",insertable = false,updatable = true)
	private LocalDateTime updationTime;
	@Column(name = "CREATED_By",length = 35)
	private String createdBy;
	@Column(name = "UPDATED_BY",length = 35)
	private String updatedBy;
}
