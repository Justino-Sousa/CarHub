package br.com.js.carhub.model.dto;

import br.com.js.carhub.model.User;
import lombok.Data;

@Data
public class CarDTO {
	private Long id;
	private int year;
	private String licensePlate;
	private String model;
	private String color;
	private User user;
}

