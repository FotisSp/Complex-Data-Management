package rtrees.quickstart;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import com.github.davidmoten.grumpy.core.Position;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.opencsv.CSVReader;

import rx.Observable;
import rx.functions.Func1;

public class App 
{
	private static final Point IOANNINA = Geometries.point(20.853746600000022,39.66502880000001);
	
    public static void main( String[] args ) throws IOException
    {
        CSVReader reader = new CSVReader(new FileReader("eqData.csv"));
        String [] nextRecord;
//        RTree<Data, Point> tree = RTree.create();
        RTree<Data, Point> tree = RTree.maxChildren(128).create();

        while ((nextRecord = reader.readNext()) != null) {
        	int day = Integer.parseInt(nextRecord[0]);
        	int year = Integer.parseInt(nextRecord[2]);
        	double depth = Double.parseDouble(nextRecord[6]);
        	double magnitude = Double.parseDouble(nextRecord[7]);
        	
        	Data record = new Data(day,nextRecord[1],year,nextRecord[3],depth,magnitude);
        	
        	double latitude = Double.parseDouble(nextRecord[4]);
        	double longitude = Double.parseDouble(nextRecord[5]);
        	tree = tree.add(record, Geometries.point(longitude,latitude));
        }
        reader.close();
        
        final double distanceKm = 5;
        
        long startTime = System.currentTimeMillis();

        List<Entry<Data, Point>> list = search(tree, IOANNINA, distanceKm)
                // get the result
                .toList().toBlocking().single();
        
        long endTime = System.currentTimeMillis() - startTime;
        long sec = endTime / 1000 % 60;
        long min = endTime / (60 * 1000) % 60;
        long hr = endTime / (60 * 60 * 1000) % 24;
    	System.out.println("Execution time is " + hr+" hrs "+min+" mins "+sec+" secs "+ endTime+" milli secs");
    	System.out.println("List Size : "+list.size());
    	
        printResults(list,distanceKm);
//    	tree.visualize(600, 600).save("map.png");
    }

	private static void printResults(List<Entry<Data, Point>> list, double dist) {
		System.out.println("Earthquackes in Ioannina, Greece in a radius of :"+dist+" km \n");
		for(int i=0; i<list.size(); i++)
        {
            System.out.println(list.get(i).value());
        }
	}
    
    public static <T> Observable<Entry<Data, Point>> search(RTree<Data, Point> tree, Point lonLat,
            final double distanceKm) {
        // First we need to calculate an enclosing lat long rectangle for this
        // distance then we refine on the exact distance
        final Position from = Position.create(lonLat.y(), lonLat.x());
        Rectangle bounds = createBounds(from, distanceKm);

        return tree
                // do the first search using the bounds
                .search(bounds)
                // refine using the exact distance
                .filter(new Func1<Entry<Data, Point>, Boolean>() {
                    public Boolean call(Entry<Data, Point> entry) {
                        Point p = entry.geometry();
                        Position position = Position.create(p.y(), p.x());
                        System.out.println("y : "+p.y()+" x : "+p.x()+" from : "+from.toString());
                        return from.getDistanceToKm(position) < distanceKm;
                    }
                });
    }
    
    private static Rectangle createBounds(final Position from, final double distanceKm) {
        // this calculates a pretty accurate bounding box. Depending on the
        // performance you require you wouldn't have to be this accurate because
        // accuracy is enforced later
        Position north = from.predict(distanceKm, 0);
        Position south = from.predict(distanceKm, 180);
        Position east = from.predict(distanceKm, 90);
        Position west = from.predict(distanceKm, 270);

        return Geometries.rectangle(west.getLon(), south.getLat(), east.getLon(), north.getLat());
    }
}
