package printPicture;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;



public class Main {
	public static final int DATA_ITEMS = 2810;
	private static final int DATA_ATTRIBUTES = 65;
	private static final int CATEGORIES = 10;
	
	public static int[][] dataValues = new int[DATA_ITEMS][DATA_ATTRIBUTES];
	public static int[][] testData = new int[DATA_ITEMS][DATA_ATTRIBUTES];
	public static Scanner sc = new Scanner(System.in);
	
	public static double[][] dataWeights = new double[CATEGORIES][2];
	
	public static void readFile() throws IOException{
		
		String pathToCsv = "../printPicture/src/printPicture/DataSet1.csv";
		String row = "";
		String[] line; 
		int linenum = 0;
		
		BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv));
		
		//copy excel to array -> Test Data Set
		while ((row = csvReader.readLine()) != null) {
		   
		   line = row.split(",");
		   
		   for(int col = 0; col < line.length; ++col) {
			   testData[linenum][col] = Integer.parseInt(line[col]);
			   
		   }
		   
		   linenum++;
		    
		}
		csvReader.close();
		
		pathToCsv = "../printPicture/src/printPicture/DataSet2.csv";
		row = "";
		
		linenum = 0;
		
		csvReader = new BufferedReader(new FileReader(pathToCsv));
		//copy excel to array -> Training Data Set
		while ((row = csvReader.readLine()) != null) {
		   
		   line = row.split(",");
		   
		   for(int col = 0; col < line.length; ++col) {
			   dataValues[linenum][col] = Integer.parseInt(line[col]);
		   }
		   
		   linenum++;
		    
		}
		csvReader.close();
	}
	
	public static void visualizeGridDigits() throws IOException {
		//used to visualize the numbers 
		for (int row = 0; row < dataValues.length; ++row) {
			int[] line = new int[64];
			for (int col = 0; col < dataValues[row].length - 1; ++col) {
				line[col] = dataValues[row][col];
				
				//System.out.print(dataValues[row][col] + " ");
			}
			
			// Creates a 8*8 grid replacing the digits with either a . or X depending on their value
			for(int col = 0; col < line.length; ++col) {
				if(line[col] <= 1) {
					System.out.print(".");
				}
				else {
					System.out.print("X");
				}
				
				if((col+1) % 8 == 0) {
					System.out.println(" ");
				}
			}
			
			System.out.println(" ");
		}
	}
	
	
	public static int EuclideanDistance(int[][] testArray, int[][] trainingArray) {
		int distance;
		int[] closestItem;
		int closestDistance;
		int total = 0;
			
		// Loop through training data
		for(int testArrays = 0; testArrays < DATA_ITEMS; testArrays++) {
			// Reset values
			closestItem = new int[0];
			closestDistance = 0;
			
			// Loop through test data
			for(int trainingArrays = 0; trainingArrays < DATA_ITEMS; trainingArrays++) {
				// Reset distance value
				distance = 0;
				// Loop through array elements
				for(int arrayElements = 0; arrayElements < DATA_ATTRIBUTES - 1; arrayElements++) {
					// Euclidean distance formula
					distance += Math.pow((trainingArray[trainingArrays][arrayElements] - testArray[testArrays][arrayElements]), 2);
				}
				// Find closest array
				if(closestDistance == 0 || distance < closestDistance) {
					closestDistance = distance;
					closestItem = trainingArray[trainingArrays];
				}
			}
			// Count total correct answers
			if(testArray[testArrays][64] == closestItem[64]) {
				total++;
			}
		}
		return total;
	}
	
	public static void main(String[] args) throws IOException {
		readFile();
		//visualizeGridDigits();
		
		//Euclidean Solution
		int correct = EuclideanDistance(testData, dataValues);
		double accuracy = ((double)correct/DATA_ITEMS);
		
		System.out.println("Euclidean Accuracy Percentage: " + accuracy * 100);
		
		
		
		//SOM Solution Test 100 Times (250 min's)
//		for(double learningRate = 0.01; learningRate <= 1.0; learningRate+=0.01) {
//			
//				double avgPer = 0;
//				for(int count = 0; count < 10; count++) {
//					SelfOrganizingMap som = new SelfOrganizingMap(DATA_ITEMS, 1, DATA_ATTRIBUTES, learningRate, 15000);
//					som.train(dataValues);
//					int correct = 0;
//					int total = 0;
//					for (int[] item : testData) {
//					   int calculatedClass = som.map(item);
//					   int actualClass = item[64];
//					   if (calculatedClass == actualClass) {
//					      correct++;
//					   }
//					   total++;
//					}
//
//					double accuracy = (double)correct / total;
//					avgPer += (accuracy * 100);
//				}
//				
//				System.out.println("LR: "+learningRate+", #Iter: "+ 15000 +" -> AVG Percentage: " + avgPer/10);
//			
//		}
		
		//SOM Singular Solution
		SelfOrganizingMap som = new SelfOrganizingMap(DATA_ITEMS, 1, DATA_ATTRIBUTES, 0.02, 15000);
		som.train(dataValues);
		correct = 0;
		int total = 0;
		for (int[] item : testData) {
		   int calculatedClass = som.map(item);
		   int actualClass = item[64];
		   if (calculatedClass == actualClass) {
		      correct++;
		   }
		   total++;
		}

		accuracy = (double)correct / total;
		
		System.out.println("Percentage: "+ accuracy * 100);
		
		// switch test and training data
//		som.train(testData);
//		correct = 0;
//		total = 0;
//		for (int[] item : dataValues) {
//		   int calculatedClass = som.map(item);
//		   int actualClass = item[64];
//		   if (calculatedClass == actualClass) {
//		      correct++;
//		   }
//		   total++;
//		}
//
//		accuracy = (double)correct / total;
//		
//		System.out.println("Percentage: "+ accuracy * 100);
		
	}
	
}
