package rsu.dto;

import java.text.DecimalFormat;

public class Vehicle {

	private String vehicleId;

	private Position position;

	private float speed;

	private float acceleration;

	private float distance;

	public Vehicle() {

	}

	public Vehicle(String vehicleId, Position position, float speed, float acceleration, float distance) {
		this.vehicleId = vehicleId;
		this.position = position;
		this.speed = speed;
		this.acceleration = acceleration;
		this.distance = distance;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
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

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		DecimalFormat decimalFormat = new DecimalFormat("#000.00");
		return "Vehicle{" +
				"vehicleId='" + vehicleId + '\'' +
				", position=" + position +
				", speed=" + decimalFormat.format(speed) +
				", acceleration=" + acceleration +
				", distance=" + distance +
				"}\n";
	}
}
