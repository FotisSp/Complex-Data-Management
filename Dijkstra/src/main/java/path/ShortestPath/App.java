package path.ShortestPath;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

import com.opencsv.CSVReader;

public class App {
	private final static int SOURCE = 117; // Magic Numbers :)
	private final static int TARGET = 1235;
	private static HashMap<Integer, Vertex> adjList = new HashMap<Integer, Vertex>();
	private static int count = 0;

	public static void main(String[] args) throws NumberFormatException, IOException {
		CSVReader edgeReader = null;
		try {
			edgeReader = new CSVReader(new FileReader("edges.csv"));
		} catch (IOException e) {
			System.out.println("Cannot find specified file : " + e.getMessage());
			System.exit(1);
		}

		String[] nextRecord;
		while ((nextRecord = edgeReader.readNext()) != null) {
			addEdge(Integer.parseInt(nextRecord[1]), Integer.parseInt(nextRecord[2]),
					Double.parseDouble(nextRecord[3]));
			addEdge(Integer.parseInt(nextRecord[2]), Integer.parseInt(nextRecord[1]),
					Double.parseDouble(nextRecord[3]));
		}

		Vertex source = adjList.get(SOURCE);
		Vertex target = adjList.get(TARGET);

		dijkstra(source);

		printPath(source, target);

		edgeReader.close();
	}

	private static void printPath(Vertex source, Vertex target) {
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		for (Vertex vertex = target; vertex != null; vertex = vertex.getPrev()) {
			path.add(vertex);
		}
		Collections.reverse(path);
		if (path.size() != 1) {
			System.out.println("Path from " + source.getId() + " to " + target.getId() + " is : " + path);
			System.out.println("Min Distanse to target is : " + target.getMinDistance());
			System.out.println("Total node visits : " + count);
		} else {
			System.out.println("No path found from " + source.getId() + " to " + target.getId());
		}
	}

	private static void addEdge(int node1, int node2, Double distance) {
		if (!adjList.containsKey(node1)) {
			adjList.put(node1, new Vertex(node1));
		}
		if (!adjList.containsKey(node2)) {
			adjList.put(node2, new Vertex(node2));
		}

		Vertex sourceVertex = adjList.get(node1);
		Vertex targetVertex = adjList.get(node2);
		Edge newEdge = new Edge(distance, targetVertex);
		sourceVertex.addEdge(newEdge);
	}

	private static void dijkstra(Vertex source) {
		PriorityQueue<Vertex> pq = new PriorityQueue<Vertex>();

		source.setMinDistance(0);

		for (Vertex v : adjList.values()) {
			if (v.getId() != source.getId())
				v.setMinDistance(Double.MAX_VALUE);
			v.setKnown(false);
			v.setPrev(null);
			pq.add(v);
		}

		while (!pq.isEmpty()) {
			Vertex v = pq.poll();
			v.setKnown(true);

			for (Edge e : v.getList()) {
				Vertex w = e.getTarget();
				if (!w.getKnown()) {
					double d = e.getDistance();
					if (v.getMinDistance() + d < w.getMinDistance()) {
						w.setMinDistance(v.getMinDistance() + d);
						w.setPrev(v);
						pq.add(w);
						count++;
					}
				}
			}
		}
	}
}
