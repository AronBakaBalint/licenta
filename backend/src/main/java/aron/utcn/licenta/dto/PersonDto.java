package aron.utcn.licenta.dto;

import java.util.List;

import lombok.Data;

@Data
public class PersonDto {

	private String username;
	private List<String> licensePlates;
	
}
