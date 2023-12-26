package br.com.js.carhub.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.js.carhub.exception.UserNotFoundException;
import br.com.js.carhub.model.User;
import br.com.js.carhub.model.dto.UserDTO;
import br.com.js.carhub.repository.CarRepository;
import br.com.js.carhub.repository.UserRepository;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarService carService;

    @InjectMocks
    private UserService userService;


    @Test
    void testFindAll() {
       
        List<User> userList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(userList);

        
        List<User> result = userService.findAll();
        assertEquals(userList, result);
    }

    @Test
    void testFindById() throws UserNotFoundException {
       
        Long userId = 1L;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        
        UserDTO result = userService.findById(userId);
        assertNotNull(result);
       
    }

    @Test
    void testSave() {
    	
        UserDTO userDTO = new UserDTO();     
        userDTO.setBirthday(LocalDate.now());
        userDTO.setCars(new ArrayList<>());
        userDTO.setEmail("jsnjunior@test.com");
        userDTO.setFirstName("Justino");
        userDTO.setLastName("Sousa");
        userDTO.setId(1L);
        userDTO.setLogin("jsnjunior");
        userDTO.setPassword("password");
        userDTO.setPhone("2002020");
        
        @SuppressWarnings("static-access")
		User user = userService.mapUserDTOToUser(userDTO);
        
        when(userRepository.findByLogin(userDTO.getLogin())).thenReturn(null);
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(null);
        when(carService.isThereCarWithPlate(userDTO.getCars())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        assertDoesNotThrow(() -> userService.save(userDTO));
       
    }

    @Test
    void testUpdate() {
    	
    	 UserDTO userDTO = new UserDTO();     
         userDTO.setBirthday(LocalDate.now());
         userDTO.setCars(new ArrayList<>());
         userDTO.setEmail("jsnjunior@test.com");
         userDTO.setFirstName("Justino");
         userDTO.setLastName("Sousa");
         userDTO.setId(1L);
         userDTO.setLogin("jsnjunior");
         userDTO.setPassword("password");
         userDTO.setPhone("2002020");
         
        @SuppressWarnings("static-access")
 		User user = userService.mapUserDTOToUser(userDTO);
       
        Long userId = 1L;
        UserDTO updatedUser = new UserDTO(); 
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        
        assertDoesNotThrow(() -> userService.update(userId, updatedUser));
       
    }

    @Test
    void testDelete() {
       
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        
        assertDoesNotThrow(() -> userService.delete(userId));
       
    }

    @Test
    void testFindByLogin() {
       
        String login = "jsnjunior";
        when(userRepository.findByLogin(login)).thenReturn(mock(UserDetails.class));

        
        assertDoesNotThrow(() -> userService.findByLogin(login));
       
    }

    @Test
    void testFindByEmail() {

        String email = "jsnjunior@test.com";
        when(userRepository.findByEmail(email)).thenReturn(new User());
        assertDoesNotThrow(() -> userService.findByEmail(email));

    }



}