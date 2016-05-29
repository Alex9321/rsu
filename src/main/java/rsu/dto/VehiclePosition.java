package rsu.dto;

public class VehiclePosition {

	private String vehicleId;

	private Position position;

	public VehiclePosition() {

	}

	public VehiclePosition(String vehicleId, Position position) {

		this.vehicleId = vehicleId;
		this.position = position;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "VehiclePosition{" +
				"vehicleId='" + vehicleId + '\'' +
				", position=" + position +
				'}';
	}
}
