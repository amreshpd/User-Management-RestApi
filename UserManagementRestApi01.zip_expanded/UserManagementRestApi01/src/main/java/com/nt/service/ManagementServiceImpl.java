package com.nt.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.nt.binding.ActiveUser;
import com.nt.binding.LoginCredential;
import com.nt.binding.RecoverPassword;
import com.nt.binding.UserAccount;
import com.nt.entity.UserMaster;
import com.nt.repository.IUserMasterRepository;
import com.nt.utils.EmailUtils;

@Service
public class ManagementServiceImpl implements IManagementService{
	@Autowired
	private IUserMasterRepository masterRepo;
	@Autowired
    private Environment env;
	@Autowired
	private EmailUtils emailUtils;
	@Override
	public String registerUser(UserAccount user) throws Exception{
		//convert UserAccount obj data to UserMaster obj(Entity obj data)
		UserMaster master=new UserMaster();
		BeanUtils.copyProperties(user, master);
		//set random String of 6 chars password
		String tempPwd=generateRandomPassword(6);
		master.setPassword(tempPwd);
		master.setStatus("inActive");
		//save object
		UserMaster saveMaster = masterRepo.save(master);
		// TODO 
		//perform send the mail operation
		String subject="User Registration Success";
		String body=readEmailMessageBody(env.getProperty("mailbody.registeruser.location"), user.getName(), tempPwd);
		emailUtils.sendMailMessage(user.getEmail(), subject, body);
		return saveMaster!=null?"User id registerd with id: "+saveMaster.getUserId():"Problem is registered with id";
	}

/*	@Override
	public String activateUserAccount(ActiveUser user) {
		// convert userActive to entity obj (UserMaster obj)data
		
		UserMaster master=new UserMaster();
		master.setEmail(user.getRegisteredEmail());
		master.setPassword(user.getTempPassword());
		//check the user available or not by using email and temp password
		Example<UserMaster> example = Example.of(master);
		List<UserMaster> list = masterRepo.findAll(example);
		
		// if valid email and temp is given the enduser supplied real password to update the record
		if(list.size()!=0) {
			UserMaster entity = list.get(0);
			//set password
			entity.setPassword(user.getConfirmPassword());
			//change the user account status to active status
			entity.setStatus("Active");
			//update the object
			UserMaster updatedEntity = masterRepo.save(entity);
			return "user is activated with new password";
		}
		return "user is not found for activation";
	}*/
	@Override
	public String activateUserAccount(ActiveUser user) {
		// convert userActive to entity obj (UserMaster obj)data
		UserMaster entity = masterRepo.findByEmailAndPassword(user.getRegisteredEmail(), user.getTempPassword());
		if(entity==null) {
			return "User is not found for activation";
		}else {
			//set password			
			entity.setPassword(user.getConfirmPassword());
			//change the user account status to active status
			entity.setStatus("Active");
			//update the object
		 masterRepo.save(entity);
			return "user id is activated with new password";
		}
	}

	@Override
	public String login(LoginCredential credential) {
		//convert login credential to usermaster obj(Entity obj)
		UserMaster master=new UserMaster();
		BeanUtils.copyProperties(credential, master);
		//prepare example object
		Example<UserMaster> example = Example.of(master);
		List<UserMaster> listEntities = masterRepo.findAll(example);
		if(listEntities.size()==0) {
			return "Invalid LoginCredentials";
		}else {
			UserMaster entity= listEntities.get(0);
			if(entity.getStatus().equalsIgnoreCase("Active")) {
				return "Valid credentials and login successfully";
			}else {
				return "login credentials is not active";
			}		
		}
	}

	@Override
	public List<UserAccount> listUser() {
		// load all entities and  convert to userAccount obj
		List<UserAccount> listUser = masterRepo.findAll().stream().map(entity->{
			UserAccount user=new UserAccount();
			BeanUtils.copyProperties(entity, user);
			return user;
		}).toList();
		return listUser;
	}

	@Override
	public UserAccount showUserById(Integer id) {
		// load the user by user id
		Optional<UserMaster> opt = masterRepo.findById(id);
		UserAccount account=null;
		if(opt.isPresent()) {
			account=new UserAccount();
			BeanUtils.copyProperties(opt.get(), account);
		}
		return account;
	}

	@Override
	public UserAccount showUserByEmailAndName(String email, String name) {
		// use the custom find(-,-) method
		UserMaster master = masterRepo.findByNameAndEmail(name, email);
		UserAccount account=null;
		if(master!=null) {
			account=new UserAccount();
			BeanUtils.copyProperties(master, account);
		}
		return account;
	}

	@Override
	public String updateUser(UserAccount user) {
		// use the customer findBy(-,-) method
		Optional<UserMaster> opt = masterRepo.findById(user.getUserId());
		if(opt.isPresent()) {
			//get entity obj
			UserMaster entity = opt.get();
			BeanUtils.copyProperties(user, entity);
			masterRepo.save(entity);
			return "User Details are updated";
		}else
		return "User details are not found for updation";
	}

	@Override
	public String deleteUserById(Integer id) {
		//load the obj
		Optional<UserMaster> opt = masterRepo.findById(id);
		if(opt.isPresent()) {
			masterRepo.deleteById(id);
			return "User Id is deeleted";
		}else		
		return "User id not found for deletion";
	}

	@Override
	public String changeUserStatus(Integer userId, String status) {
		Optional<UserMaster> opt = masterRepo.findById(userId);
		if(opt.isPresent()) {
			UserMaster master = opt.get();
			// change the status
			master.setStatus(status);
			masterRepo.save(master);
			return "User status is changed";			
		}else		
		return "User not found for changing status";
	}

	@Override
	public String recoverPassword(RecoverPassword recover) throws Exception{
		UserMaster master = masterRepo.findByNameAndEmail(recover.getUserName(), recover.getEmail());
		if(master!=null) {
			String pwd = master.getPassword();
	      	//TODO
			String subject="mail for password recovery";
			String mailBody=readEmailMessageBody(env.getProperty("mailbody.recoverpwd.location"), recover.getUserName(), pwd);
			emailUtils.sendMailMessage(recover.getEmail(), subject, mailBody);
			return pwd;
		}		
		return "User and email is not found";
	}
	
	//helper method for same class
	private String generateRandomPassword(int length) {
		// a list of character to choose from in the form of String
		String alphaNumericStr="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123654987@#$";
		// creating a stringBuffer size of alphanumericStr
		StringBuilder randomWord=new StringBuilder();
		int i;
		for(i=0;i<length;i++) {
			int ch=(int)(alphaNumericStr.length()*Math.random());
			//adding random character one by one at the end of randomWord
			randomWord.append(alphaNumericStr.charAt(ch));
		}
		return randomWord.toString();
	}
	
	private String readEmailMessageBody(String fileName,String fullName,String pwd)throws Exception{
		String mailBody=null;
		String url=" ";
		try(FileReader reader=new FileReader(fileName);
				BufferedReader br=new BufferedReader(reader)){
			//read file content to stringBuffer object line by line
			StringBuffer buffer=new StringBuffer();
			String line=null;
			do {
				line=br.readLine();
				buffer.append(line);
			}while(line!=null);
			mailBody=buffer.toString();
			mailBody.replace("{FULL-NAME}", fullName);
			mailBody.replace("{PWD}", pwd);
			mailBody.replace("{URL}", url);
						
		}//try
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}//catch end
		return mailBody;
	}//method
   
}
