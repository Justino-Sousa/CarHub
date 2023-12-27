package br.com.js.carhub.model.dto;

import java.util.List;

import br.com.js.carhub.model.Car;
import br.com.js.carhub.model.LoginInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
	String firstName, lastName, email, login, phone;
	List<Car> cars;
	LoginInformation loginInformation;
	
}
