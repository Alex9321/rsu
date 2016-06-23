package rsu.dto;

public class VehicleData {

	private String vehicleId;

	private Position position;

	private float speed;

	private float acceleration;

	private float distanceFromA;

	private float distanceFromB;

	public VehicleData() {

	}

	public VehicleData(String vehicleId, Position position, float speed, float acceleration, float distanceFromA, float distanceFromB) {
		this.vehicleId = vehicleId;
		this.position = position;
		this.speed = speed;
		this.acceleration = acceleration;
		this.distanceFromA = distanceFromA;
		this.distanceFromB = distanceFromB;
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

	public float getDistanceFromA() {
		return distanceFromA;
	}

	public void setDistanceFromA(float distanceFromA) {
		this.distanceFromA = distanceFromA;
	}

	public float getDistanceFromB() {
		return distanceFromB;
	}

	public void setDistanceFromB(float distanceFromB) {
		this.distanceFromB = distanceFromB;
	}

}
