package path.ShortestPath;

import java.util.LinkedList;

public class Vertex implements Comparable<Vertex> {
	private int id;
	private Vertex prev;
	private boolean known;
	private double minDistance = 0;
	private LinkedList<Edge> edge = new LinkedList<Edge>();

	public Vertex(int id) {
		this.id = id;
		prev = null;
	}

	public void addEdge(Edge edge) {
		this.edge.add(edge);
	}

	public void setMinDistance(double dist) {
		this.minDistance = dist;
	}

	public void setKnown(boolean state) {
		this.known = state;
	}

	public void setPrev(Vertex prev) {
		this.prev = prev;
	}

	public boolean getKnown() {
		return known;
	}

	public double getMinDistance() {
		return minDistance;
	}

	public int getId() {
		return id;
	}

	public LinkedList<Edge> getList() {
		return edge;
	}

	public Vertex getPrev() {
		return prev;
	}

	@Override
	public String toString() {
		return id + "";
	}

	public int compareTo(Vertex t) {
		if (minDistance < t.getMinDistance())
			return -1;
		else if (minDistance > t.getMinDistance())
			return 1;
		return 0;
	}
}
