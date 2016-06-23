package rsu.communication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rsu.charts.ChartCreator;
import rsu.classification.Classifier;
import rsu.data.DataProcessor;
import rsu.dto.VehicleData;

import java.io.PrintWriter;
import java.util.List;

@Component
public class SituationHandler {

	@Autowired
	private DataProcessor dataProcessor;

	@Autowired
	private Classifier classifier;

	@Autowired
	private ChartCreator chartCreator;

	public void analyse(VehicleData vehicle, PrintWriter clientWriter) throws Exception {
		boolean sentWarning = false;
		if (vehicle.getDistanceFromA() >= 65) {
			List<VehicleData> vehicleDataTrace = dataProcessor.getTrackedVehicles().get(vehicle.getVehicleId());
			if (vehicleDataTrace != null && !classifier.stops(chartCreator.createSpeedSeries(vehicleDataTrace))) {
				for (List<VehicleData> vehicleDataData : dataProcessor.getToBeWarnedVehicles().values()) {
					VehicleData lastData = vehicleDataData.get(vehicleDataData.size() - 1);
					float distanceFromB = lastData.getDistanceFromB();
					if (distanceFromB > 30 && distanceFromB < 80) {
						sentWarning = true;
						clientWriter.println(lastData.getVehicleId());
						System.out.println("Warned vehicle: " + vehicle.getVehicleId());
					}
				}
			}
		}
		if (!sentWarning) {
			clientWriter.println("Nu");
		}
	}

}
