package path.ShortestPath;

public class Edge {
	private final Vertex target;
	private double distance;

	public Edge(double argDistance, Vertex argSource) {
		distance = argDistance;
		target = argSource;
	}

	public double getDistance() {
		return distance;
	}

	public Vertex getTarget() {
		return target;
	}
}