package br.com.js.carhub.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CarTest {
	 @Test
	    void testCarGettersAndSetters() {
	       
	        Car car = new Car();
	        car.setId(1L);
	        car.setYear(2022);
	        car.setLicensePlate("ABC123");
	        car.setModel("Model X");
	        car.setColor("Red");

	        User user = Mockito.mock(User.class);
	        car.setUser(user);
	
	        assertEquals(1L, car.getId());
	        assertEquals(2022, car.getYear());
	        assertEquals("ABC123", car.getLicensePlate());
	        assertEquals("Model X", car.getModel());
	        assertEquals("Red", car.getColor());
	        assertEquals(user, car.getUser());
	    }
}
