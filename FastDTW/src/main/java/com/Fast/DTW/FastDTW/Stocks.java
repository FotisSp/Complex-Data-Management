package com.Fast.DTW.FastDTW;

import java.io.FileReader;
import java.io.IOException;

import com.opencsv.CSVReader;

import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.fastdtw.dtw.FastDTW;
import net.sf.javaml.distance.fastdtw.timeseries.TimeSeries;

public class Stocks 
{
    public static void main( String[] args )
    {
		CSVReader goldmanStockReader = null;
		CSVReader jPMorganStockReader = null;
		double[] goldmanClose = new double[2000]; 
		double[] jpMorganClose = new double[2000]; 

		try {
			goldmanStockReader = new CSVReader(new FileReader("Goldman Sachs Group.csv"),',', '\'',9);
			jPMorganStockReader = new CSVReader(new FileReader("JPMorgan Chase.csv"),',', '\'',9);
			
			String[] nextRecord;
			int i=0;
			while ((nextRecord = goldmanStockReader.readNext()) != null) {
				goldmanClose[i] = Double.parseDouble(nextRecord[4]);
				i++;
			}
			
			i=0;
			while ((nextRecord = jPMorganStockReader.readNext()) != null) {
				jpMorganClose[i] = Double.parseDouble(nextRecord[4]);
				i++;
				
			}
		} catch (IOException e) {
			System.out.println("Cannot find specified file : " + e.getMessage());
			System.exit(1);
		}
		
		Instance goldmanInstance = new DenseInstance(goldmanClose);
		Instance jpMorganInstance = new DenseInstance(jpMorganClose);

        long startTime = System.currentTimeMillis();
        double result = 0;
		
		for (int i = 0; i < 100; i++) 
		{
			result = FastDTW.getWarpDistBetween(new TimeSeries(goldmanInstance),
					new TimeSeries(jpMorganInstance), 1);
		}
		
		long endTime = System.currentTimeMillis() - startTime;
		long mili = endTime % 1000;
        long sec = endTime / 1000 % 60;
        long min = endTime / (60 * 1000) % 60;
        long hr = endTime / (60 * 60 * 1000) % 24;
        System.out.println("Distance is "+result);
    	System.out.println("Execution time is " + hr+" hrs "+min+" mins "+sec+" secs "+ mili+" milli secs");
    }
}
