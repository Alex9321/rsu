package rsu.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import rsu.dto.Vehicle;

import java.io.IOException;

public class JsonUtils {

	private static ObjectMapper mapper = new ObjectMapper();

	public static Vehicle getVehiclePositionFromJson(String vehiclePositionAsJson) throws IOException {
		return mapper.readValue(vehiclePositionAsJson, Vehicle.class);
	}

}
