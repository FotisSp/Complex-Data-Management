package nearest.knearest;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import org.apache.commons.math3.random.MersenneTwister;

import com.github.davidmoten.grumpy.core.Position;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;
import com.opencsv.CSVReader;


public class App 
{
	private static final int RANDOMCENTERS = 5000;
	private static final int DISTANCE = 10;
	private static PrintWriter treeWriter;
	private static PrintWriter bruteWriter;
	
	public static void main( String[] args ) throws IOException
    {
        CSVReader reader = new CSVReader(new FileReader("eqData2.csv"));
        HashMap<Integer,Epicenter> allEpicenters = new HashMap<Integer,Epicenter>();
        String [] nextRecord;
        int count=0;
        long treeST,treeET = 0, bruteForceST, bruteForceET = 0;

        RTree<Epicenter, Point> tree = RTree.create();

        while ((nextRecord = reader.readNext()) != null) 
        {        	
        	double latitude = Double.parseDouble(nextRecord[4]);
        	double longitude = Double.parseDouble(nextRecord[5]);
        	Epicenter center = new Epicenter(latitude,longitude);
        	allEpicenters.put(count, center);
        	tree = tree.add(center, Geometries.point(longitude,latitude));
        	count++;
        }
        reader.close();
        
        MersenneTwister mt = new MersenneTwister(System.currentTimeMillis());
        HashMap<Integer,Epicenter> dRandom = new HashMap<Integer,Epicenter>();
        try{
        	treeWriter = new PrintWriter("tree.txt","UTF-8");
        	bruteWriter = new PrintWriter("brute.txt","UTF-8");
        } catch (Exception e) {
        	System.out.println("File exception "+e);
        }
        
        for(int i=0; i<RANDOMCENTERS; i++)
        {
        	int randomInt = mt.nextInt(71693);

        	if(i % 1000 == 0)
        		System.out.println("i am at stage : "+i);
        	
        	dRandom.put(i, allEpicenters.get(randomInt));
        	
	        bruteForceST = System.currentTimeMillis();
	        findCenters(allEpicenters, Position.create(dRandom.get(i).getLatitude(), dRandom.get(i).getLongitude()));
	        bruteForceET += System.currentTimeMillis() - bruteForceST;
        }
        for(int i=0; i<RANDOMCENTERS; i++)
        {
			Point findPoint = Geometries.point(dRandom.get(i).getLatitude(),dRandom.get(i).getLongitude());
	        treeST = System.currentTimeMillis();
	        List<Entry<Epicenter,Point>> list = tree.nearest(findPoint, DISTANCE, 3).toList().toBlocking().single();
	        treeET += System.currentTimeMillis() - treeST;
	        
	        printTreeResults(list);
        }
        
        treeWriter.close();
        bruteWriter.close();
        
        

        long treeSec = treeET / 1000 % 60;
        long treeMin = treeET / (60 * 1000) % 60;
        long treeHr = treeET / (60 * 60 * 1000) % 24;
        long treeMs = treeET % 1000;
    	System.out.println("Tree execution time is " + treeHr+" hrs "+treeMin+" mins "+treeSec+" secs "+ treeMs+" milli secs");
    	long bruteSec = bruteForceET / 1000 % 60;
        long bruteMin = bruteForceET / (60 * 1000) % 60;
        long bruteHr = bruteForceET / (60 * 60 * 1000) % 24;
        long bruteMs = bruteForceET % 1000;
    	System.out.println("Brute Force execution time is " + bruteHr+" hrs "+bruteMin+" mins "+bruteSec+" secs "+ bruteMs+" milli secs");
    	
    }
	
	private static void printTreeResults(List<Entry<Epicenter, Point>> list) {
		for(int i=0; i<list.size(); i++)
        {
			if(list.get(i).value().getDistance() !=0)
				treeWriter.println("latitude="+list.get(i).geometry().x()+", longitude="+list.get(i).geometry().y()+" distance="+list.get(i).value().getDistance());
        }
	}
	
	private static void printBruteResults(PriorityQueue<Epicenter> pq) {
		Iterator<Epicenter> itr=pq.iterator();  
		while(itr.hasNext()){  
			Epicenter ep = itr.next();
			if(ep.getDistance() != 0.0)
				bruteWriter.println(ep);  
		}
	}
	
	private static void findCenters(HashMap<Integer,Epicenter> allEpicenters, Position randPos)
	{
		PriorityQueue<Epicenter> queue = new PriorityQueue<Epicenter>(3, new Comparator<Epicenter>() {
			public int compare(Epicenter o1, Epicenter o2) {
				if(o1.getDistance() < o2.getDistance()) {
					return 1;
				} else if(o1.getDistance() > o2.getDistance()) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		
		for(Epicenter c: allEpicenters.values())
		{
			Position pos = Position.create(c.getLatitude(),c.getLongitude());
			if(randPos.getDistanceToKm(pos) < DISTANCE)
			{
				c.setDistance(randPos.getDistanceToKm(pos));
				if (queue.size() >= 3) {
					if(c.getDistance() < queue.peek().getDistance())
					{
		                queue.poll();
		                queue.add(c);
					}
		        }
				else
				{
					queue.add(c);
				}
			}
		}
        printBruteResults(queue);
	}
	
}
