package rsu.dto;

public class Position {

	private float latitude;

	private float longitude;

	public Position() {

	}

	public Position(long latitude, long longitude) {

		this.latitude = latitude;
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "Position{" +
				"latitude=" + latitude +
				", longitude=" + longitude +
				'}';
	}
}
