package com.nt.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.binding.ActiveUser;
import com.nt.binding.LoginCredential;
import com.nt.binding.RecoverPassword;
import com.nt.binding.UserAccount;
import com.nt.service.IManagementService;

@RestController
@RequestMapping("/user-api")
public class UserManagementOperationController {
	@Autowired
	private IManagementService userService;

	@PostMapping("/save")
	public ResponseEntity<String> saveUser(@RequestBody UserAccount account) {
		// use service
		try {
			String registerUser = userService.registerUser(account);
			return new ResponseEntity<String>(registerUser, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/activate")
	public ResponseEntity<String> activateUser(@RequestBody ActiveUser user) {
		try {
			String msg = userService.activateUserAccount(user);
			return new ResponseEntity<String>(msg, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/login")
	public ResponseEntity<String> performLogin(@RequestBody LoginCredential login) {
		try {
			String loginMsg = userService.login(login);
			return new ResponseEntity<String>(loginMsg, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/report")
	public ResponseEntity<?> showUser() {
		try {
			List<UserAccount> list = userService.listUser();
			return new ResponseEntity<List<UserAccount>>(list, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/find/{id}")
	public ResponseEntity<?> showUserById(@PathVariable Integer id) {
		try {
			UserAccount showMsg = userService.showUserById(id);
			return new ResponseEntity<UserAccount>(showMsg, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/find/{email}/{name}")
	public ResponseEntity<?> showUserByEmailAndname(@PathVariable String email, @PathVariable String name) {
		try {
			UserAccount msg = userService.showUserByEmailAndName(email, name);
			return new ResponseEntity<UserAccount>(msg, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update")
	public ResponseEntity<String> updateUser(@RequestBody UserAccount user) {
		try {
			String updateMsg = userService.updateUser(user);
			return new ResponseEntity<String>(updateMsg, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteByUserId(@PathVariable Integer id) {
		try {
			String deleteMsg = userService.deleteUserById(id);
			return new ResponseEntity<String>(deleteMsg, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PatchMapping("/change/{id}/{status}")
	public ResponseEntity<String> changeUserByStatus(@PathVariable Integer id, @PathVariable String status) {
		try {
			String changeMsg = userService.changeUserStatus(id, status);
			return new ResponseEntity<String>(changeMsg, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/recover")
	public ResponseEntity<?> recoverPassword(@RequestBody RecoverPassword recover) {
		try {
			String recoverMsg = userService.recoverPassword(recover);
			return new ResponseEntity<String>(recoverMsg, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
