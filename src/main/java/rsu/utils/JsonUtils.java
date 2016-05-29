package rsu.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import rsu.dto.VehiclePosition;

import java.io.IOException;

public class JsonUtils {

	private static ObjectMapper mapper = new ObjectMapper();

	public static VehiclePosition getVehiclePositionFromJson(String vehiclePositionAsJson) throws IOException {
		return mapper.readValue(vehiclePositionAsJson, VehiclePosition.class);
	}

}
