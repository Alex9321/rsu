package rsu.communication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import rsu.dao.WayRepo;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class MessagingInterface {

	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	private WayRepo wayRepo;

	@PostConstruct
	public void startListening() throws IOException {
		ServerSocket serverSocket = new ServerSocket(9999);
		while (true) {
			Socket socket = serverSocket.accept();
			taskExecutor.execute(new Receiver(socket, wayRepo));
		}
	}
}
