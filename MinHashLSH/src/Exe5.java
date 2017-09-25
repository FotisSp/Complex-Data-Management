import java.io.IOException;
import java.util.List;

public class Exe5 {
	private final static String FOLDER = "Data5";
	private final static int NUMOFPERMUTATION = 200;
	private final static double EPS = 0.1;
	private final static int NUMOFBANDS = 1;
	private final static double SIMILARITYTHRESHOLD = 0.7;
	
	private static int count = 0;
	private static int comp = 0;
	private static long exactStartTime,approxStartTime,lshStartTime;
	private static long exactEndTime = 0, approxEndTime = 0, lshEndTime = 0;
	private static int FP;
	
	public static void main(String[] args) throws IOException {
		MinHash mh = new MinHash(FOLDER, NUMOFPERMUTATION);
		String[] files = mh.allDocs();
		int[][] minHashMat = mh.minHashMatrix();
		
		for (int k = 0; k < 5; k++) 
		{
			exactApprox(files,mh,minHashMat);
			lsh(files,mh,minHashMat);
		}
		
		getExecutionTime(exactEndTime,"Exact");
		getExecutionTime(approxEndTime, "Approximate");

		System.out.println("Total number of comparisons: " + comp);
		System.out.println("Number of times exact and approximate jaccard differ more than epsilon: " + count);
		
		getExecutionTime(lshEndTime, "LSH");
		System.out.println("Number of false positives: " + FP);
	}
	
	private static void exactApprox(String[] files, MinHash mh, int[][] minHashMat) throws IOException
	{
		count = 0;
		comp = 0;
		double exact;
		double approx;
		for (int i = 0; i < files.length; i++) {
//			System.out.println(files[i]);
			for (int j = i + 1; j < files.length; j++) {
				exactStartTime = System.currentTimeMillis();
				exact = mh.exactJaccard(files[i], files[j]);
				exactEndTime += System.currentTimeMillis() - exactStartTime;

				approxStartTime = System.currentTimeMillis();
				approx = mh.approximateJaccard(minHashMat[i], minHashMat[j]);
				approxEndTime += System.currentTimeMillis() - approxStartTime;

				if (approx < exact && approx + EPS < exact) {
					count++;
				} else if (approx > exact && approx - EPS > exact) {
					count++;
				}
				comp++;
			}
		}
	}
	
	private static void lsh(String[] docNames, MinHash mh, int[][] hashMtx) throws IOException
	{
		lshStartTime = System.currentTimeMillis();
		
		FP = 0;
		for (int i = 0; i < docNames.length; i++) {
			LSH lsh = new LSH(hashMtx, docNames, NUMOFBANDS);
			List<String> nearDuplicates = lsh.nearDuplicatesOf(docNames[i]);
			for (String s : nearDuplicates) {
				double sim = mh.exactJaccard(docNames[i], s);
				if (sim > SIMILARITYTHRESHOLD) {
					System.out.println(s);
				} else {
					FP++;
				}
			}
		}
		
		lshEndTime += System.currentTimeMillis() - lshStartTime;
	}

	private static void getExecutionTime(long endTime, String name) {
		long sec = endTime / 1000 % 60;
        long min = endTime / (60 * 1000) % 60;
        long hr = endTime / (60 * 60 * 1000) % 24;
        long ms = endTime % 1000;
    	System.out.println(name+" execution time is " + hr+" hrs "+min+" mins "+sec+" secs "+ ms+" milli secs");
	}
}
