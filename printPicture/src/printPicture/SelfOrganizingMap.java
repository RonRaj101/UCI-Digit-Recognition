package printPicture;
import java.util.ArrayList;
import java.util.Random;

public class SelfOrganizingMap {
   private double[][] weights; // weights of neurons in the map
   private int rows; // number of rows in the map
   private int cols; // number of columns in the map
   private int numAttributes; // number of attributes per item
   private double learningRate; // learning rate for adjusting weights
   private int maxIterations; // maximum number of training iterations
   
   //constructor function to initialize the variables
   public SelfOrganizingMap(int rows, int cols, int numAttributes, double learningRate, int maxIterations) {
      this.rows = rows;
      this.cols = cols;
      this.numAttributes = numAttributes;
      this.learningRate = learningRate;
      this.maxIterations = maxIterations;
      this.weights = new double[rows][numAttributes];
   }
   
   public double euclideanDistance(int row1, int col1, int row2, int col2) {
	      double distance = 0;
	      for (int dimension = 0; dimension < numAttributes; dimension++) {
	          int index1 = col1 * numAttributes + dimension;
	          int index2 = col2 * numAttributes + dimension;
	          distance += Math.pow(weights[row1][index1] - weights[row2][index2], 2);
	       }
	       //System.out.println(Math.sqrt(distance));
	       return Math.sqrt(distance);
	   }
   
   public void train(int[][] data) {
	    
      Random random = new Random();
      
      for (int iteration = 0; iteration < maxIterations; iteration++) {
    	 
    	 //use a random data item  
         int randomIndex = random.nextInt(data.length);
         
         //assign data and its attributes to 'item'
         int[] item = data[randomIndex];
         
         //get the winning neuron (i.e the best matching/closest neuron)
         int[] winnerCoords = getWinningNeuron(item);
         int winnerRow = winnerCoords[0];
         int winnerCol = winnerCoords[1];
         
         // iterate over all of the data items
         for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
               double distance = euclideanDistance(winnerRow, winnerCol, row, col);
               double neighborhoodRadius = getNeighborhoodRadius(iteration);
               double influence = getInfluence(distance, neighborhoodRadius);
               //for each attribute, assign a suitable weight to it 
               for (int attrCount = 0; attrCount < numAttributes; attrCount++) {
                  int index = col * numAttributes + attrCount;
                  //weight average of the new weight by the influence
                  weights[row][index] += influence * (item[attrCount] - weights[row][index]);
               }
               
            }
            
         }
         
      }
      
   }
   
   //used to determine the winning neuron in the SOM for a particular data item
   public int[] getWinningNeuron(int[] item) {
	  //2D Coordinates for each Neuron 
      int[] winnerCoords = new int[2];
      //Get the shortest distance
      double minDistance = Double.MAX_VALUE;
      
      for (int row = 0; row < rows; row++) {
         for (int col = 0; col < cols; col++) {
            double distance = 0;
            //calculate euclidean distance b/w all nodes and choose the shortest
            for (int numAttr = 0; numAttr < numAttributes; numAttr++) {
               int index = col * numAttributes + numAttr;
               distance += Math.pow(weights[row][index] - item[numAttr], 2);
            }
            distance = Math.sqrt(distance);
            if (distance < minDistance) {
               //update when shorter distance found
               winnerCoords[0] = row;
               winnerCoords[1] = col;
               minDistance = distance;
            }
         }
      }
      return winnerCoords;
   }
   
   
   //calculates the distance between an item and a neuron
   private double calculateDistance(double[] a, int[] b) {
	   double sum = 0.0;
	   for (int element = 0; element < a.length - 1; element++) {
	      double diff = a[element] - b[element];
	      //done to work in multiple dimensions
	      sum += diff * diff;
	   }
	   return Math.sqrt(sum);
	}
   
   //maps a data item to its closest neuron
   public int map(int[] item) {
	   int winningNeuron = -1;
	   double minDistance = Double.MAX_VALUE;
	   for (int weightCount = 0; weightCount < weights.length; weightCount++) {
	      double distance = calculateDistance(weights[weightCount], item);
	      if (distance < minDistance) {
	    	 minDistance = distance;
	         winningNeuron = (int) weights[weightCount][64];
	      }
	   }
	   return winningNeuron;   }
    //control the extent to which the weights of the winning neuron and its neighboring neurons are updated
    public double getNeighborhoodRadius(int iteration) {
       double timeConstant = maxIterations / Math.log(rows / 2);
       return rows / 2 * Math.exp(-iteration / timeConstant);
    }
    
    //determines the extent to which the weights of the winning neuron and its neighboring neurons should be updated
    public double getInfluence(double distance, double radius) {
    	//gaussian function
       return Math.exp(-distance * distance / (2 * radius * radius));
    }
   
 }

        
