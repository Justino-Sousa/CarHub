package br.com.js.carhub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.js.carhub.exception.EmailException;
import br.com.js.carhub.exception.InvalidFieldsException;
import br.com.js.carhub.exception.LicensePlateException;
import br.com.js.carhub.exception.LoginException;
import br.com.js.carhub.exception.MissingFieldsException;
import br.com.js.carhub.exception.UserNotFoundException;
import br.com.js.carhub.model.User;
import br.com.js.carhub.model.dto.UserDTO;
import br.com.js.carhub.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }
    
    @GetMapping("/{userId}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) throws UserNotFoundException {
			return ResponseEntity.ok(userService.findById(userId));	
	}

    @PostMapping
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO data) throws InvalidFieldsException, LoginException, EmailException, LicensePlateException, MissingFieldsException {
            return ResponseEntity.ok(userService.save(data));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody UserDTO data) throws InvalidFieldsException, UserNotFoundException, LoginException, EmailException{
    	return ResponseEntity.ok(userService.update(userId, data)); 
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) throws UserNotFoundException {
            userService.delete(userId);
    }

}