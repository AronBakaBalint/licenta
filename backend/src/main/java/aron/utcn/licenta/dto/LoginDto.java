package aron.utcn.licenta.dto;

import lombok.Data;

@Data
public class LoginDto {

	private String username;
	private String password;
	
	@Override
	public String toString() {
		return "{ username: "+username+", password: "+password+"}";
	}
}
