package rsu.communication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import rsu.charts.ChartCreator;
import rsu.data.DataProcessor;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;

@Component
public class MessagingInterface {

	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	private DataProcessor dataProcessor;

	@Autowired
	private TrafficAnalyser trafficAnalyser;

	@Autowired
	private ChartCreator chartCreator;

	@PostConstruct
	public void startListening() throws IOException {
		ServerSocket serverSocket = new ServerSocket(9999);
		while (true) {
			Socket socket = serverSocket.accept();
			int dayPeriod = 1;
			LocalTime seven = LocalTime.of(7, 0);
			LocalTime ten = LocalTime.of(10, 0);
			LocalTime fifteen = LocalTime.of(15, 0);
			LocalTime twenty = LocalTime.of(20, 0);
			LocalTime now = LocalTime.now();
			if ((now.isAfter(seven) && now.isBefore(ten)) || (now.isAfter(fifteen) && now.isBefore(twenty))) {
				dayPeriod = 1;
			}
			if (now.isAfter(ten) && now.isBefore(fifteen)) {
				dayPeriod = 2;
			}
			if (now.isBefore(seven) || now.isAfter(twenty)) {
				dayPeriod = 2;
			}
			taskExecutor.execute(new Receiver(dayPeriod, socket, dataProcessor, trafficAnalyser, chartCreator));
		}
	}
}
