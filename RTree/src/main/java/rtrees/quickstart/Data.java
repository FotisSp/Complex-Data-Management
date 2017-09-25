package rtrees.quickstart;

public class Data {
	
	private int day;
	private String month;
	private int year;
	private String hour;
	private double depth;
	private double magnitude;

	public Data(int day, String month, int year, String hour, double depth, double magnitude)
	{
		this.day = day;
		this.month = month;
		this.year = year;
		this.hour = hour;
		this.depth = depth;
		this.magnitude = magnitude;
	}

	public int getDay() {
		return day;
	}

	public String getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}

	public String getHour() {
		return hour;
	}

	public double getDepth() {
		return depth;
	}

	public double getMagnitude() {
		return magnitude;
	}

	@Override
	public String toString() {
		return "Data [day=" + day + ", month=" + month + ", year=" + year + ", hour=" + hour + ", depth=" + depth
				+ ", magnitude=" + magnitude + "]";
	}

}
