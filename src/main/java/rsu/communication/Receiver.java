package rsu.communication;

import rsu.charts.ChartCreator;
import rsu.classification.DataSetCreator;
import rsu.dao.WayDao;
import rsu.data.DataProcessor;
import rsu.dto.Vehicle;
import rsu.utils.JsonUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Receiver implements Runnable {

	private Socket socket;

	private DataProcessor dataProcessor;

	private WayDao wayDao;

	private DataSetCreator dataSetCreator;

	public Receiver(Socket socket, DataProcessor dataProcessor, WayDao wayDao, DataSetCreator dataSetCreator) {
		this.socket = socket;
		this.dataProcessor = dataProcessor;
		this.wayDao = wayDao;
		this.dataSetCreator = dataSetCreator;
	}

	@Override
	public void run() {
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (true) {
				String jsonMessage = inFromClient.readLine();
				if (jsonMessage == null) {
					socket.close();
					Map<String, List<Vehicle>> vehiclePositionsMap = dataProcessor.getTrackedVehicles();
					List<List<Vehicle>> orderedVehicles = new ArrayList<>(vehiclePositionsMap.values());
					dataProcessor.getAllVehicles().clear();
					dataProcessor.getTrackedVehicles().clear();
//					dataSetCreator.createTrainingData(orderedVehicles);
					dataSetCreator.createTestData(orderedVehicles);
					new ChartCreator(orderedVehicles);
					break;
				}
				Vehicle vehicle = JsonUtils.getVehiclePositionFromJson(jsonMessage);
				vehicle.setDistance(wayDao.getDistanceFromStart(vehicle.getPosition()));
				dataProcessor.addVehiclePosition(vehicle);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}