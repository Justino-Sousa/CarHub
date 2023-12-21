package br.com.js.carhub.model.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import br.com.js.carhub.model.Car;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	
	private Long id;	
	private String firstName;
	private String lastName;
	private String email;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate birthday;
	private String login;
	private String password;
	private String phone;
	private List<Car> cars;

}
