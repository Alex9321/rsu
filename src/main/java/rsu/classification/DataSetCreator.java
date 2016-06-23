package rsu.classification;

import org.jfree.data.xy.XYSeries;
import org.springframework.stereotype.Component;
import rsu.charts.ChartCreator;
import rsu.dto.VehicleData;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataSetCreator {

	public void createTrainingData(List<List<VehicleData>> vehicleModel) throws IOException {

		ChartCreator chartCreator = new ChartCreator();

		StringBuilder sb;
		List<String> lines = new ArrayList<>();
		File file = new File("/Users/alex/School/rsu/src/main/resources/weka/driverIntentTrainingModel.arff");
		lines.add("@relation intent");
		lines.add("\n");
		for (int i = 1; i <= 25; i++) {
			lines.add("@attribute speed" + i + " numeric");
		}
		lines.add("@attribute stops {yes, no}");
		lines.add("\n");
		lines.add("@data");
		for (List<VehicleData> vehicleDatas : vehicleModel) {
			XYSeries xySeries = chartCreator.createSpeedSeries(vehicleDatas);
			for (int j = 1; j <= 10; j++) {
				sb = new StringBuilder();
				for (int i = 40; i < 65; i++) {
					sb.append(String.format("%.2f", chartCreator.interpolate(xySeries, i) - ((float) j / 2)));
					sb.append(", ");
				}
				sb.append("yes");
				lines.add(sb.toString());
			}
			for (int j = 1; j <= 10; j++) {
				sb = new StringBuilder();
				for (int i = 40; i < 65; i++) {
					sb.append(String.format("%.2f", chartCreator.interpolate(xySeries, i) + ((float) (i / 30) + j / 2)));
					sb.append(", ");
				}
				sb.append("no");
				lines.add(sb.toString());
			}
		}
		Files.write(Paths.get(file.toURI()), lines, StandardCharsets.UTF_8);
	}

	public void createTestData(List<List<VehicleData>> vehicleModel) throws IOException {

		ChartCreator chartCreator = new ChartCreator();

		StringBuilder sb;
		List<String> lines = new ArrayList<>();
		File file = new File("/Users/alex/School/rsu/src/main/resources/weka/driverIntentTestModel.arff");
		lines.add("@relation intent");
		lines.add("\n");
		for (int i = 1; i <= 25; i++) {
			lines.add("@attribute speed" + i + " numeric");
		}
		lines.add("@attribute stops {yes, no}");
		lines.add("\n");
		lines.add("@data");
		for (List<VehicleData> vehicleDatas : vehicleModel) {
			XYSeries xySeries = chartCreator.createSpeedSeries(vehicleDatas);
			for (int j = 1; j <= 20; j++) {
				sb = new StringBuilder();
				for (int i = 40; i < 65; i++) {
					sb.append(String.format("%.2f", j + 12.2));
					sb.append(", ");
				}
				sb.append("no");
				lines.add(sb.toString());
			}
		}
		for (List<VehicleData> vehicleDatas : vehicleModel) {
			XYSeries xySeries = chartCreator.createSpeedSeries(vehicleDatas);
			for (int j = 1; j <= 20; j++) {
				sb = new StringBuilder();
				for (int i = 40; i < 65; i++) {
					sb.append(String.format("%.2f", j + 10.5));
					sb.append(", ");
				}
				sb.append("no");
				lines.add(sb.toString());
			}
		}
		Files.write(Paths.get(file.toURI()), lines, StandardCharsets.UTF_8);
	}

}
