package rsu.communication;

import rsu.Run;
import rsu.charts.ChartCreator;
import rsu.data.DataProcessor;
import rsu.dto.VehicleData;
import rsu.utils.JsonUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Receiver implements Runnable {

	private int dayPeriod;

	private Socket socket;

	private DataProcessor dataProcessor;

	private TrafficAnalyser trafficAnalyser;

	private ChartCreator chartCreator;

	public Receiver(int dayPeriod, Socket socket, DataProcessor dataProcessor, TrafficAnalyser trafficAnalyser,
			ChartCreator chartCreator) {
		this.dayPeriod = dayPeriod;
		this.socket = socket;
		this.dataProcessor = dataProcessor;
		this.trafficAnalyser = trafficAnalyser;
		this.chartCreator = chartCreator;
	}

	@Override
	public void run() {
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter clientWriter = new PrintWriter(socket.getOutputStream(), true);
			while (true) {
				String jsonMessage = inFromClient.readLine();
				if (jsonMessage == null) {
					socket.close();
					Map<String, List<VehicleData>> vehiclePositionsMap = dataProcessor.getTrackedVehicles();
					List<List<VehicleData>> orderedVehicles = new ArrayList<>(vehiclePositionsMap.values());
					if ("TRAIN".equals(Run.MODE)) {
						createTrainingData(orderedVehicles);
					}
					chartCreator.showChart(orderedVehicles);
					clearInMemoryData();
					break;
				}
				VehicleData vehicleData = JsonUtils.getVehiclePositionFromJson(jsonMessage);
				dataProcessor.addVehiclePosition(vehicleData);
//				if ("NORMAL".equals(Run.MODE)) {
					if (dataProcessor.getTrackedVehicles().containsKey(vehicleData.getVehicleId())) {
						trafficAnalyser.analyse(vehicleData, clientWriter, dayPeriod);
					}
					else {
						clientWriter.println("Safe");
					}
//				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createTrainingData(List<List<VehicleData>> orderedVehicles) throws Exception {
		trafficAnalyser.reTrain(orderedVehicles, dayPeriod);
	}

	public void clearInMemoryData() {
		dataProcessor.getAllVehicles().clear();
		dataProcessor.getTrackedVehicles().clear();
		dataProcessor.getToBeWarnedVehicles().clear();
	}
}