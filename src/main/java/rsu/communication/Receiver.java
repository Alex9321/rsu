package rsu.communication;

import rsu.dao.WayRepo;
import rsu.dto.Position;
import rsu.dto.VehiclePosition;
import rsu.utils.JsonUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class Receiver implements Runnable {

	private Socket socket;

	private WayRepo wayRepo;

	public Receiver(Socket socket, WayRepo wayRepo) {
		this.socket = socket;
		this.wayRepo = wayRepo;
	}

	@Override
	public void run() {
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (true) {
				String jsonMessage = inFromClient.readLine();
				if (jsonMessage == null) {
					socket.close();
					break;
				}
				VehiclePosition vehiclePositionFromJson = JsonUtils.getVehiclePositionFromJson(jsonMessage);
				System.out.println(vehiclePositionFromJson);
				Position position = vehiclePositionFromJson.getPosition();
				System.out.println(wayRepo.mapMatching(position.getLatitude(), position.getLongitude(), position.getLatitude(), position.getLongitude()));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}