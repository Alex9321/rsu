package rsu.communication;

import rsu.charts.ChartCreator;
import rsu.classification.Classifier;
import rsu.classification.DataSetCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import rsu.dao.WayDao;
import rsu.data.DataProcessor;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class MessagingInterface {

	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	private DataProcessor dataProcessor;

	@Autowired
	private WayDao wayDao;

	@Autowired
	private DataSetCreator dataSetCreator;

	@Autowired
	private Classifier classifier;

	@Autowired
	private SituationHandler situationAnalyser;

	@Autowired
	private ChartCreator chartCreator;

	@PostConstruct
	public void startListening() throws IOException {
		ServerSocket serverSocket = new ServerSocket(9999);
		while (true) {
			Socket socket = serverSocket.accept();
			taskExecutor.execute(new Receiver(socket, dataProcessor, wayDao, dataSetCreator, classifier, situationAnalyser, chartCreator));
		}
	}
}
