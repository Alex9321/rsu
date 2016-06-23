package rsu.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import rsu.dto.VehicleData;

import java.io.IOException;

public class JsonUtils {

	private static ObjectMapper mapper = new ObjectMapper();

	public static VehicleData getVehiclePositionFromJson(String vehiclePositionAsJson) throws IOException {
		return mapper.readValue(vehiclePositionAsJson, VehicleData.class);
	}

}
