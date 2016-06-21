package rsu.classification;

import org.springframework.stereotype.Component;
import rsu.dto.Vehicle;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataSetCreator {

	public void createTrainingData(List<List<Vehicle>> vehicleModel) throws IOException {
		StringBuilder sb;
		List<String> lines = new ArrayList<>();
		File file = new File("/Users/alex/School/rsu/src/main/resources/weka/driverIntentTrainingModel.arff");
		lines.add("@relation intent");
		lines.add("\n");
		lines.add("@attribute speed1 numeric");
		lines.add("@attribute speed2 numeric");
		lines.add("@attribute speed3 numeric");
		lines.add("@attribute speed4 numeric");
		lines.add("@attribute speed5 numeric");
		lines.add("@attribute speed6 numeric");
		lines.add("@attribute stops {yes, no}");
		lines.add("\n");
		lines.add("@data");
		for (List<Vehicle> vehicles : vehicleModel) {
			sb = new StringBuilder();
			for (int i = 0; i < 6; i++) {
				sb.append(vehicles.get(i).getSpeed());
				sb.append(", ");
			}
			sb.append("yes");
			lines.add(sb.toString());
		}
		for (List<Vehicle> vehicles : vehicleModel) {
			sb = new StringBuilder();
			for (int i = 0; i < 6; i++) {
				sb.append(vehicles.get(i).getSpeed() + 1.5);
				sb.append(", ");
			}
			sb.append("no");
			lines.add(sb.toString());
		}
		Files.write(Paths.get(file.toURI()), lines, StandardCharsets.UTF_8);
	}

	public void createTestData(List<List<Vehicle>> vehicleModel) throws IOException {
		StringBuilder sb;
		List<String> lines = new ArrayList<>();
		File file = new File("/Users/alex/School/rsu/src/main/resources/weka/driverIntentTestModel.arff");
		lines.add("@relation intent");
		lines.add("\n");
		lines.add("@attribute speed1 numeric");
		lines.add("@attribute speed2 numeric");
		lines.add("@attribute speed3 numeric");
		lines.add("@attribute speed4 numeric");
		lines.add("@attribute speed5 numeric");
		lines.add("@attribute speed6 numeric");
		lines.add("@attribute stops {yes, no}");
		lines.add("\n");
		lines.add("@data");
		for (List<Vehicle> vehicles : vehicleModel) {
			sb = new StringBuilder();
			for (int i = 0; i < 6; i++) {
				sb.append(vehicles.get(i).getSpeed());
				if (i != 5) {
					sb.append(", ");
				}
			}
			lines.add(sb.toString());
		}
		for (List<Vehicle> vehicles : vehicleModel) {
			sb = new StringBuilder();
			for (int i = 0; i < 6; i++) {
				sb.append(vehicles.get(i).getSpeed() + 1.5);
				if (i != 5) {
					sb.append(", ");
				}
			}
			lines.add(sb.toString());
		}
		Files.write(Paths.get(file.toURI()), lines, StandardCharsets.UTF_8);
	}

}
