package rsu.communication;

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

	private Socket socket;

	private DataProcessor dataProcessor;

	private TrafficAnalyser trafficAnalyser;

	private ChartCreator chartCreator;

	public Receiver(Socket socket, DataProcessor dataProcessor, TrafficAnalyser trafficAnalyser, ChartCreator chartCreator) {
		this.socket = socket;
		this.dataProcessor = dataProcessor;
		this.trafficAnalyser = trafficAnalyser;
		this.chartCreator = chartCreator;
	}

	@Override
	public void run() {
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String settings[] = inFromClient.readLine().split(",");
			String scenario = settings[0];
			String mode = settings[1];
			String period = settings[2];
			PrintWriter clientWriter = new PrintWriter(socket.getOutputStream(), true);
			clientWriter.println("Safe");
			while (true) {
				String jsonMessage = inFromClient.readLine();
				if (jsonMessage == null) {
					socket.close();
					Map<String, List<VehicleData>> vehiclePositionsMap = dataProcessor.getTrackedVehicles();
					List<List<VehicleData>> orderedVehicles = new ArrayList<>(vehiclePositionsMap.values());
					if ("train".equals(mode)) {
						createTrainingData(orderedVehicles, Integer.parseInt(period), scenario);
					}
					chartCreator.showChart(orderedVehicles);
					clearInMemoryData();
					break;
				}
				VehicleData vehicleData = JsonUtils.getVehiclePositionFromJson(jsonMessage);
				dataProcessor.addVehiclePosition(vehicleData, scenario);
				if ("run".equals(mode)) {
					if (dataProcessor.getTrackedVehicles().containsKey(vehicleData.getVehicleId())) {
						trafficAnalyser.analyse(vehicleData, clientWriter, Integer.parseInt(period));
					}
					else {
						clientWriter.println("Safe");
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createTrainingData(List<List<VehicleData>> orderedVehicles, int dayPeriod, String scenario) throws Exception {
		trafficAnalyser.reTrain(orderedVehicles, dayPeriod, scenario);
	}

	public void clearInMemoryData() {
		dataProcessor.getAllVehicles().clear();
		dataProcessor.getTrackedVehicles().clear();
		dataProcessor.getToBeWarnedVehicles().clear();
	}
}