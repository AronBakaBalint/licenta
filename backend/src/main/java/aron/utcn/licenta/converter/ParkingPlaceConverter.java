package aron.utcn.licenta.converter;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import aron.utcn.licenta.dto.ParkingPlaceDto;
import aron.utcn.licenta.model.ParkingPlace;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ParkingPlaceConverter implements BaseConverter<ParkingPlace, ParkingPlaceDto>{

	private final Environment environment;
	
	@Override
	public ParkingPlace convertToEntity(ParkingPlaceDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParkingPlaceDto convertToDto(ParkingPlace entity) {
		ParkingPlaceDto parkingPlaceDto = new ParkingPlaceDto();
		parkingPlaceDto.setId(entity.getId());
		parkingPlaceDto.setOccupierCarPlate(entity.getOccupierCarPlate());
		parkingPlaceDto.setColor(getColorByStatus(entity.getStatus()));
		parkingPlaceDto.setStatus(entity.getStatus());
		return parkingPlaceDto;
	}
	
	private int getIntFromColor(int Red, int Green, int Blue){
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }
	
	private Integer getColorByStatus(String status) {
		int red = Integer.parseInt(environment.getProperty("parkingspot."+status+".red"));
		int green = Integer.parseInt(environment.getProperty("parkingspot."+status+".green"));
		int blue = Integer.parseInt(environment.getProperty("parkingspot."+status+".blue"));
		return getIntFromColor(red, green, blue);
	}
}
