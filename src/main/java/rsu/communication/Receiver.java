package rsu.communication;

import rsu.charts.ChartCreator;
import rsu.classification.Classifier;
import rsu.classification.DataSetCreator;
import rsu.dao.WayDao;
import rsu.data.DataProcessor;
import rsu.dto.VehicleData;
import rsu.utils.JsonUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Receiver implements Runnable {

	private Socket socket;

	private DataProcessor dataProcessor;

	private WayDao wayDao;

	private DataSetCreator dataSetCreator;

	private Classifier classifier;

	private SituationHandler situationAnalyser;

	private ChartCreator chartCreator;

	public Receiver(Socket socket, DataProcessor dataProcessor, WayDao wayDao, DataSetCreator dataSetCreator, Classifier classifier, SituationHandler situationAnalyser,
			ChartCreator chartCreator) {
		this.socket = socket;
		this.dataProcessor = dataProcessor;
		this.wayDao = wayDao;
		this.dataSetCreator = dataSetCreator;
		this.classifier = classifier;
		this.situationAnalyser = situationAnalyser;
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
					createTrainingData(orderedVehicles);
					chartCreator.showChart(orderedVehicles);
					dataProcessor.getToBeWarnedVehicles().clear();
					dataProcessor.getTrackedVehicles().clear();
					dataProcessor.getAllVehicles().clear();
					break;
				}
				VehicleData vehicleData = JsonUtils.getVehiclePositionFromJson(jsonMessage);
				vehicleData.setDistanceFromA(wayDao.getDistanceFromA(vehicleData.getPosition()));
				vehicleData.setDistanceFromB(wayDao.getDistanceFromB(vehicleData.getPosition()));
				dataProcessor.addVehiclePosition(vehicleData);
				if (dataProcessor.getTrackedVehicles().containsKey(vehicleData.getVehicleId())) {
					situationAnalyser.analyse(vehicleData, clientWriter);
				}
				else {
					clientWriter.println("Nu");
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createTrainingData(List<List<VehicleData>> orderedVehicles) throws IOException {
		dataProcessor.getAllVehicles().clear();
		dataProcessor.getTrackedVehicles().clear();
		dataSetCreator.createTestData(orderedVehicles);
	}
}