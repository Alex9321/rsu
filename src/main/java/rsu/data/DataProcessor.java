package rsu.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rsu.dao.WayDao;
import rsu.dao.model.MapMatchingResult;
import rsu.dto.VehicleData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataProcessor {

	@Autowired
	private WayDao wayDao;

	private Map<String, List<VehicleData>> allVehicles = new HashMap<>();

	private Map<String, List<VehicleData>> trackedVehicles = new HashMap<>();

	private Map<String, List<VehicleData>> toBeWarnedVehicles = new HashMap<>();

	public void addVehiclePosition(VehicleData vehicleData) {
		vehicleData.setDistanceFromA(wayDao.getDistanceFromA(vehicleData.getPosition()));
		vehicleData.setDistanceFromB(wayDao.getDistanceFromB(vehicleData.getPosition()));
		List<VehicleData> vehicleDatas = allVehicles.get(vehicleData.getVehicleId());
		if (vehicleDatas == null) {
			vehicleDatas = new ArrayList<>();
		}
		vehicleDatas.add(vehicleData);
		if (vehicleDatas.size() > 1) {
			if (trackedVehicles.containsKey(vehicleData.getVehicleId())) {
				addToTrackedVehicles(vehicleData);
			}
			else {
				MapMatchingResult mapMatchingResult = wayDao
						.mapMatching(vehicleDatas.get(vehicleDatas.size() - 2).getPosition(), vehicleDatas.get(vehicleDatas.size() - 1).getPosition());
				if (mapMatchingResult.isSuccess()) {
					float heading = wayDao.getHeading(vehicleDatas.get(vehicleDatas.size() - 2).getPosition(), vehicleDatas.get(vehicleDatas.size() - 1).getPosition());
					if (heading < 4) {
						addToTrackedVehicles(vehicleData);
					}
					else {
						addToToBeWarnedVehicles(vehicleData);
					}
				}
			}
		}
		allVehicles.put(vehicleData.getVehicleId(), vehicleDatas);
	}

	private void addToTrackedVehicles(VehicleData vehicleData) {
		List<VehicleData> vehicleDatas = trackedVehicles.get(vehicleData.getVehicleId());
		if (vehicleDatas == null) {
			vehicleDatas = new ArrayList<>();
		}
		vehicleDatas.add(vehicleData);
		trackedVehicles.put(vehicleData.getVehicleId(), vehicleDatas);
	}

	private void addToToBeWarnedVehicles(VehicleData vehicleData) {
		List<VehicleData> vehicleDatas = toBeWarnedVehicles.get(vehicleData.getVehicleId());
		if (vehicleDatas == null) {
			vehicleDatas = new ArrayList<>();
		}
		vehicleDatas.add(vehicleData);
		toBeWarnedVehicles.put(vehicleData.getVehicleId(), vehicleDatas);
	}

	public Map<String, List<VehicleData>> getAllVehicles() {
		return allVehicles;
	}

	public Map<String, List<VehicleData>> getTrackedVehicles() {
		return trackedVehicles;
	}

	public Map<String, List<VehicleData>> getToBeWarnedVehicles() {
		return toBeWarnedVehicles;
	}

}
