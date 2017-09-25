# Complex Data Management

### **Assignment1**

The goal of this assignment is to find earthquakes that happened near Ioannina,Greece.  To achieve this goal we use R-trees [(this specific library)](https://github.com/davidmoten/rtree) and [grumpy](https://github.com/davidmoten/grumpy) library in order to use coordinates.

The [given dataset](http://geophysics.geo.auth.gr/ss/CATALOGS/preliminary/finalcat.cat) includes earthquakes that occurred in Greece from 1995 to 2016 in the format : dd,mon,y,hh:mm:ss.ss,lat,lon,Depth,mag(ML)

To better understand R-Trees we had to experiment with different values for the parameters of the R-Tree. We came to the conclusion that using value 128 for the parameter **max children** decreases the search time as well as the overlap, so that during search, fewer subtrees need to be processed.

###  **Assignment2**

In this assignment we implement the nearest neighbor search using two different methods: exhaustive search and Rtree. We use again the same Rtree library and dataset. In addition we use the [Mersenne Twister random number generator](https://mvnrepository.com/artifact/org.apache.commons/commons-math3/3.6.1) to get a sample of 5000 random earthquake data.

We concluded, as expected, that using an Rtree makes searching the data significantly faster.

### **Assignment 3**

Shortest path computation using Dijkstra’s algorithm

### **Assignment 4**

Dynamic Time Wrapping algorithm on stock data. We calculate distance between time-series using [FastDTW](http://java-ml.sourceforge.net/api/0.1.7/net/sf/javaml/distance/fastdtw/dtw/FastDTW.html)

### **Assignment 5**

Text similarity using Locality Sensitive Hashing and Minhashing using the following [github repository](https://github.com/ALShum/MinHashLSH/).

