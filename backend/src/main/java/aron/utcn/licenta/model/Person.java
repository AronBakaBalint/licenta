package aron.utcn.licenta.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(unique = true, length = 20)
	private String username;
	private String name;
	private String email;
	private Double balance = 0.0;
	private String password;
	
	public boolean hasEnoughMoney(double amount) {
		return balance > amount;
	}
	
	public void pay(double amount) {		
		balance -= amount;
	}
	
}
