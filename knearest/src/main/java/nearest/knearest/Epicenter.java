package nearest.knearest;

public class Epicenter {
	
	private double latitude;
	private double longitude;
	private double distance;
	
	public Epicenter(double longitude, double latitude)
	{
		this.latitude = latitude;
		this.longitude = longitude;
		distance = 0;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "latitude=" + latitude + ", longitude=" + longitude+" distance="+distance;
	}

}
