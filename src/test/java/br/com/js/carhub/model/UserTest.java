package br.com.js.carhub.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UserTest {
	@Test
    void testUserGettersAndSetters() {
     
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        user.setLogin("john_doe");
        user.setPassword("password");
        user.setPhone("123456789");

        Car car = Mockito.mock(Car.class);
        user.setCars(Collections.singletonList(car));

        UserRole role = UserRole.ADMIN;
        user.setRole(role);

        assertEquals(1L, user.getId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals(LocalDate.of(1990, 1, 1), user.getBirthday());
        assertEquals("john_doe", user.getLogin());
        assertEquals("password", user.getPassword());
        assertEquals("123456789", user.getPhone());
        assertEquals(Collections.singletonList(car), user.getCars());
        assertEquals(role, user.getRole());
    }

}
