package rsu.classification;

import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DataSetManager {

	@Autowired
	ChartCreator chartCreator;

	public void createTrainingData(List<List<VehicleData>> vehicleModel) throws IOException {

		StringBuilder sb;
		List<String> lines = new ArrayList<>();
		File file = new File("/Users/alex/School/rsu/src/main/resources/weka/trainingModel.arff");
		lines.add("@relation intent");
		lines.add("\n");
		for (int i = 1; i <= 30; i++) {
			lines.add("@attribute speed" + i + " numeric");
		}
		lines.add("@attribute stops {yes, no}");
		lines.add("\n");
		lines.add("@data");
		for (List<VehicleData> vehicleDatas : vehicleModel) {
			XYSeries xySeries = chartCreator.createSpeedSeries(vehicleDatas);
			sb = new StringBuilder();
			for (int i = 20; i < 50; i++) {
				sb.append(String.format("%.2f", interpolate(xySeries, i)));
				sb.append(", ");
			}
			double intersectionSpeed = interpolate(xySeries, 110);
			if (intersectionSpeed > 8) {
				sb.append("no");
			}
			else {
				sb.append("yes");
			}
			lines.add(sb.toString());
		}
		Files.write(Paths.get(file.toURI()), lines, StandardCharsets.UTF_8);
	}

	public void createTestData(List<List<VehicleData>> vehicleModel) throws IOException {

		StringBuilder sb;
		List<String> lines = new ArrayList<>();
		File file = new File("/Users/alex/School/rsu/src/main/resources/weka/testModel.arff");
		lines.add("@relation intent");
		lines.add("\n");
		for (int i = 1; i <= 30; i++) {
			lines.add("@attribute speed" + i + " numeric");
		}
		lines.add("@attribute stops {yes, no}");
		lines.add("\n");
		lines.add("@data");
		for (List<VehicleData> vehicleDatas : vehicleModel) {
			XYSeries xySeries = chartCreator.createSpeedSeries(vehicleDatas);
			sb = new StringBuilder();
			for (int i = 20; i < 50; i++) {
				sb.append(String.format("%.2f", interpolate(xySeries, i)));
				sb.append(", ");
			}
			double intersectionSpeed = interpolate(xySeries, 110);
			if (intersectionSpeed > 8) {
				sb.append("no");
			}
			else {
				sb.append("yes");
			}
			lines.add(sb.toString());
		}
		Files.write(Paths.get(file.toURI()), lines, StandardCharsets.UTF_8);
	}

	public double interpolate(XYSeries s, double x) {
		if (x <= s.getMinX()) {
			return s.getY(0).doubleValue();
		}
		if (x >= s.getMaxX()) {
			return s.getY(s.getItemCount() - 1).doubleValue();
		}
		List<?> items = s.getItems();
		for (int i = 0; i < items.size() - 1; i++) {
			XYDataItem i0 = (XYDataItem) items.get(i);
			XYDataItem i1 = (XYDataItem) items.get(i + 1);
			double x0 = i0.getXValue();
			double y0 = i0.getYValue();
			double x1 = i1.getXValue();
			double y1 = i1.getYValue();

			if (x >= x0 && x <= x1) {
				double d = x - x0;
				double a = d / (x1 - x0);
				double y = y0 + a * (y1 - y0);
				return y;
			}
		}
		return 0;
	}

}
