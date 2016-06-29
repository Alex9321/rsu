package rsu.charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;
import org.springframework.stereotype.Component;
import rsu.dto.VehicleData;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.util.List;

@Component
public class ChartCreator {

	public ChartCreator() {

	}

	public void showChart(List<List<VehicleData>> vehicleLists) {
		JFrame frame = new JFrame("Driver intent");
		JFreeChart speedDistanceChart = ChartFactory
				.createXYLineChart("Driver template", "Distance", "Speed", createDataSet(vehicleLists), PlotOrientation.VERTICAL, false, false, false);

		ChartPanel chartPanel = new ChartPanel(speedDistanceChart);
		chartPanel.setPreferredSize(new Dimension(560, 367));
		frame.setContentPane(chartPanel);
		frame.pack();
		RefineryUtilities.centerFrameOnScreen(frame);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	public XYSeries createSpeedSeries(List<VehicleData> vehicleDataList) {
		XYSeries vehicleSeries = new XYSeries(vehicleDataList.get(0).getVehicleId());
		for (VehicleData vehicleData : vehicleDataList) {
			vehicleSeries.add(vehicleData.getDistanceFromA(), vehicleData.getSpeed());
		}
		return vehicleSeries;
	}

	private XYSeriesCollection createDataSet(List<List<VehicleData>> vehicleLists) {
		XYSeriesCollection dataSet = new XYSeriesCollection();
		for (List<VehicleData> vehicleDatas : vehicleLists) {
			dataSet.addSeries(createSpeedSeries(vehicleDatas));
		}
		return dataSet;
	}

}
