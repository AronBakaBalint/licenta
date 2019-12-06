package aron.utcn.licenta.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginRequestModel {
	 private String email;
	 private String password;
	
}
