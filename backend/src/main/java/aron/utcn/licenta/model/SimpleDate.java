package aron.utcn.licenta.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleDate {

	private Integer day;
	private Integer month;
	private Integer year;
}
