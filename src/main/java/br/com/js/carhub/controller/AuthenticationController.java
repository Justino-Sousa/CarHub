package br.com.js.carhub.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.js.carhub.model.LoggedData;
import br.com.js.carhub.model.LoginInformation;
import br.com.js.carhub.model.User;
import br.com.js.carhub.model.dto.AuthenticationDTO;
import br.com.js.carhub.model.dto.TokenResponseDTO;
import br.com.js.carhub.model.dto.UserInfoDTO;
import br.com.js.carhub.repository.UserRepository;
import br.com.js.carhub.service.LoginInformationService;
import br.com.js.carhub.service.TokenService;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private LoggedData loggedData;

    @Autowired
    private LoginInformationService informationService;

    @Autowired
    private UserRepository userRepository;

    public static boolean tokenExpired = false;

    @PostMapping("/signin")
    public ResponseEntity<Object> login(@RequestBody AuthenticationDTO data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.getLogin(), data.getPassword());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((User) auth.getPrincipal());

            User user = (User) userRepository.findByLogin(data.getLogin());
            informationService.updateInformation(new LoginInformation(null, LocalDateTime.now(), LocalDateTime.now(), user.getId()));

            return ResponseEntity.ok(new TokenResponseDTO(token));
        } catch (Exception e) {
            if (tokenExpired) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(getErrorResponse("Unauthorized - invalid session", HttpStatus.UNAUTHORIZED.value()));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(getErrorResponse("Unauthorized", HttpStatus.UNAUTHORIZED.value()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<Object> me() {

        UserInfoDTO infoDTO = new UserInfoDTO();
        try {
            Long id = loggedData.getLogguedUserId();
            User user = null;
            if (userRepository.findById(id).isPresent()) {
                user = userRepository.findById(id).get();
            }

            LoginInformation inf = informationService.getInformationByUser(user);

            infoDTO.setCars(user.getCars());
            infoDTO.setEmail(user.getEmail());
            infoDTO.setFirstName(user.getFirstName());
            infoDTO.setLastName(user.getLastName());
            infoDTO.setLogin(user.getLogin());
            infoDTO.setPhone(user.getPhone());
            infoDTO.setLoginInformation(inf);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(getErrorResponse("Unauthorized - invalid session", HttpStatus.UNAUTHORIZED.value()));
        }

        return ResponseEntity.ok(infoDTO);
    }

    private Object getErrorResponse(String message, int errorCode) {
        return Map.of("message", message, "errorCode", errorCode);
    }
}
