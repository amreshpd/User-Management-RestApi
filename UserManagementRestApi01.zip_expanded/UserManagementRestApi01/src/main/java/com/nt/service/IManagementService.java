package com.nt.service;

import java.util.List;

import com.nt.binding.ActiveUser;
import com.nt.binding.LoginCredential;
import com.nt.binding.RecoverPassword;
import com.nt.binding.UserAccount;

public interface IManagementService {

	public String registerUser(UserAccount user) throws Exception;
	public String activateUserAccount(ActiveUser user);
	public String login(LoginCredential credential);
	public List<UserAccount> listUser();
	public UserAccount showUserById(Integer id);
	public UserAccount showUserByEmailAndName(String email,String name);
	
	public String updateUser(UserAccount user);
	public String deleteUserById(Integer id);
	public String changeUserStatus(Integer userId,String status);
	public String recoverPassword(RecoverPassword recover) throws Exception;
	
}
