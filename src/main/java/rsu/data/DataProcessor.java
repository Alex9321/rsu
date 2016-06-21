package rsu.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rsu.dao.WayDao;
import rsu.dao.model.MapMatchingResult;
import rsu.dto.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataProcessor {

	@Autowired
	private WayDao wayDao;

	private Map<String, List<Vehicle>> allVehicles = new HashMap<>();

	private Map<String, List<Vehicle>> trackedVehicles = new HashMap<>();

	public void addVehiclePosition(Vehicle vehicle) {
		List<Vehicle> vehicles = allVehicles.get(vehicle.getVehicleId());
		if (vehicles == null) {
			vehicles = new ArrayList<>();
		}
		vehicles.add(vehicle);
		if (vehicles.size() > 1) {
			if (trackedVehicles.containsKey(vehicle.getVehicleId())) {
				addToTrackedVehicles(vehicle);
			}
			else {
				MapMatchingResult mapMatchingResult = wayDao.mapMatching(vehicles.get(vehicles.size() - 2).getPosition(), vehicles.get(vehicles.size() - 1).getPosition());
				if (mapMatchingResult.getWayM() == 46331812) {
					addToTrackedVehicles(vehicle);
				}
			}
		}
		allVehicles.put(vehicle.getVehicleId(), vehicles);
	}

	private void addToTrackedVehicles(Vehicle vehicle) {
		List<Vehicle> vehicles = trackedVehicles.get(vehicle.getVehicleId());
		if (vehicles == null) {
			vehicles = new ArrayList<>();
		}
		vehicles.add(vehicle);
		trackedVehicles.put(vehicle.getVehicleId(), vehicles);
	}

	public Map<String, List<Vehicle>> getAllVehicles() {
		return allVehicles;
	}

	public Map<String, List<Vehicle>> getTrackedVehicles() {
		return trackedVehicles;
	}

}
