package br.com.js.carhub.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.js.carhub.exception.MissingFieldsException;
import br.com.js.carhub.model.User;
import br.com.js.carhub.model.dto.UserDTO;
import br.com.js.carhub.service.CarService;
import br.com.js.carhub.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    
    @GetMapping("/{userId}")
	public ResponseEntity<Object> getUserById(@PathVariable Long userId) {

		Optional<User> userOptional = userService.findById(userId);

		if (userOptional.isPresent()) {
			User user = userOptional.get();

			UserDTO userDTO = new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
					user.getBirthday(), user.getLogin(), user.getPassword(), user.getPhone(), user.getCars());

			return ResponseEntity.ok(userDTO);
		} else {
			return ResponseEntity.badRequest().body(getErrorResponse("User not found", HttpStatus.NOT_FOUND.value()));
		}
	}


    @PostMapping
    public ResponseEntity<Object> register(@RequestBody UserDTO data) {
        if (data == null) {
            return ResponseEntity.badRequest().body(getErrorResponse("Invalid fields", HttpStatus.BAD_REQUEST.value()));
        }

        if (userService.findByLogin(data.getLogin()) != null) {
            return ResponseEntity.badRequest().body(getErrorResponse("Login already exists", HttpStatus.BAD_REQUEST.value()));
        }

        if (userService.findByEmail(data.getEmail()) != null) {
            return ResponseEntity.badRequest().body(getErrorResponse("Email already exists", HttpStatus.BAD_REQUEST.value()));
        }

        if (carService.isThereCarWithPlate(data.getCars())) {
            return ResponseEntity.badRequest().body(getErrorResponse("License plate already exists", HttpStatus.BAD_REQUEST.value()));
        }

        try {
            UserDTO verifiedDto = userService.save(data);
            return ResponseEntity.ok(verifiedDto);
        } catch (MissingFieldsException e) {
            return ResponseEntity.badRequest().body(getErrorResponse("Missing fields", HttpStatus.BAD_REQUEST.value()));
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId, @RequestBody UserDTO data) {
        if (data == null) {
            return ResponseEntity.badRequest().body(getErrorResponse("Invalid fields", HttpStatus.BAD_REQUEST.value()));
        }

        User usr = userService.findById(userId).orElse(null);
        if (usr == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(getErrorResponse("User not found", HttpStatus.NOT_FOUND.value()));
        }

        if (usr.getUsername().equals(data.getLogin()) && usr.getId() != userId) {
            return ResponseEntity.badRequest().body(getErrorResponse("Login already exists", HttpStatus.BAD_REQUEST.value()));
        }

        if (usr.getEmail().equals(data.getEmail()) && usr.getId() != userId) {
            return ResponseEntity.badRequest().body(getErrorResponse("Email already exists", HttpStatus.BAD_REQUEST.value()));
        }

        try {
            User user = userService.update(userId, data).orElse(null);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(getErrorResponse("User not found", HttpStatus.NOT_FOUND.value()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(getErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        try {
            userService.delete(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(getErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    private Object getErrorResponse(String message, int errorCode) {
        return Map.of("message", message, "errorCode", errorCode);
    }
}