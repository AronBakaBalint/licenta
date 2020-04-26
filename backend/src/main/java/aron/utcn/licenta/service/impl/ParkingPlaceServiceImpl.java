package aron.utcn.licenta.service.impl;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import aron.utcn.licenta.converter.ParkingPlaceToDtoConverter;
import aron.utcn.licenta.converter.UnconfirmedReservationToDtoConverter;
import aron.utcn.licenta.dto.ParkingPlaceDto;
import aron.utcn.licenta.dto.UnconfirmedReservationDto;
import aron.utcn.licenta.model.ParkingPlace;
import aron.utcn.licenta.repository.ParkingPlaceRepository;
import aron.utcn.licenta.service.ArduinoService;
import aron.utcn.licenta.service.ParkingPlaceService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ParkingPlaceServiceImpl implements ParkingPlaceService {

	private final Environment environment;
	
	private final ArduinoService arduinoService;
	
	private final ParkingPlaceRepository parkingPlaceRepository;
	
	private final ParkingPlaceToDtoConverter parkingPlaceToDtoConverter;
	
	private final UnconfirmedReservationToDtoConverter unconfirmedReservationToDtoConverter;
	
	@Override
	public List<ParkingPlaceDto> getAllParkingPlaces() {
		List<ParkingPlace> parkingPLaces = parkingPlaceRepository.getAllParkingPlaces();
		return parkingPLaces.stream().map(p -> parkingPlaceToDtoConverter.convertParkingPlaceToDto(p)).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void save(ParkingPlace parkingPlace) {
		parkingPlaceRepository.save(parkingPlace);
	}

	@Override
	public ParkingPlaceDto findById(int id) {
		return parkingPlaceToDtoConverter.convertParkingPlaceToDto(parkingPlaceRepository.findById(id));
	}

	@Override
	public List<UnconfirmedReservationDto> findUnconfirmedReservations(int userId) {
		List<ParkingPlace> reservedPlaces = parkingPlaceRepository.findReservationsByUser(userId);
		reservedPlaces = reservedPlaces.stream().filter(rp->rp.getStatus().equals("reserved")).collect(Collectors.toList());
		return reservedPlaces.stream().map(unconfirmedReservationToDtoConverter::convertUnconfirmedReservationToDto).collect(Collectors.toList());
	}

	@Override
	public List<UnconfirmedReservationDto> findAllReservations(int userId) {
		List<ParkingPlace> reservedPlaces = parkingPlaceRepository.findReservationsByUser(userId);
		return reservedPlaces.stream().map(unconfirmedReservationToDtoConverter::convertUnconfirmedReservationToDto).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void handleScannedPlate(String licensePlate) {
		ParkingPlace parkingPlace = parkingPlaceRepository.findByPlate(licensePlate);
		if(parkingPlace.isOccupied()) {
			parkingPlace.setFree();
			Date departureTime = new Date();
			Date arrivalTime = parkingPlace.getArrivalTime();
			float price = calculatePrice(arrivalTime, departureTime);
			System.out.println(price);
			DecimalFormat df = new DecimalFormat("#.##");
			df.setRoundingMode(RoundingMode.CEILING);
			arduinoService.displayOnLCD(df.format(price)+ " lei");
		} else {
			parkingPlace.setOccupied();
			parkingPlace.setArrivalTime(new Date());
		}
	}
	
	private float calculatePrice(Date arrival, Date departure) {
		long diffInMillies = Math.abs(departure.getTime() - arrival.getTime());
	    long diff = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	    float pricePerHour = Float.parseFloat(environment.getProperty("parking.price_per_hour"));
	    return diff * pricePerHour / 3600;
	}

}
