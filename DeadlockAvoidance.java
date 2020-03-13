package deadlockAvoidance;

/*
 * File: DeadlockAvoidance.java
 * Author: Gabriel Gallegos
 * Date: Nov 15, 2019
 * Purpose: The following program implements the Banker's algorithm for deadlock avoidance.
 * User must first input a correct filename, from which the program will read all the data
 * and populate matrices. The output will be the data from the file and
 * the final result, which is the safe sequence.
 * 
 */


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class DeadlockAvoidance {
	
	public static int numOfProcesses, numOfResources;
	public static int need[][];
	public static int max[][];
	public static int allocation[][];
	public static int available[];
	public static int initialAvailable[];
	public static int safeSequence[];
	public static boolean visited[];
	public static String file;
	
	/*
	 * Once user enters a valid filename, the data read from the file is read
	 * and matrices are created from that same data. 
	 */
	public static void populateMatrices() throws IOException {
		
		System.out.println("Enter filename:");
		Scanner scanner = new Scanner(System.in);
		file = scanner.next();
		String line;
		// Read file
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			
			System.out.print("Filename not valid\nProgram ended");
			System.exit(0);
		}	
		// Read first line
		line=br.readLine();
		numOfProcesses = Integer.parseInt(line);
		// Read second line
		line=br.readLine();
		numOfResources = Integer.parseInt(line);
		
		// Initialize matrices with data read from first two lines
		need= new int[numOfProcesses][numOfResources];
		max = new int[numOfProcesses][numOfResources];
		allocation = new int[numOfProcesses][numOfResources];
		available = new int[numOfResources];
		initialAvailable = new int[numOfResources];
		visited = new boolean[numOfProcesses];
		safeSequence = new int[numOfProcesses];
		// Populate allocation matrix
		for (int i=0; i<numOfProcesses; i++) {
			line=br.readLine();
			StringTokenizer token = new StringTokenizer(line);
			for (int j=0; j<numOfResources; j++) {
				allocation[i][j] = Integer.parseInt(token.nextToken());
			}
		}
		
		// Populate max claim matrix
		for (int i=0; i<numOfProcesses; i++) {
			line=br.readLine();
			StringTokenizer token = new StringTokenizer(line);
			for (int j=0; j<numOfResources; j++) {
				max[i][j] = Integer.parseInt(token.nextToken());
			}
		}
		
		// Populate initial available resources array
		for (int i=0; i<1; i++) {
			line=br.readLine();
			StringTokenizer token = new StringTokenizer(line);
			for (int j=0; j<numOfResources; j++) {
				available[j] = Integer.parseInt(token.nextToken());
			}
		}
		
		// Copy from available array, in order to then display
		// the initial available resources array. 
		for (int i=0; i<1; i++) {
			for (int j=0; j<numOfResources; j++) {
				initialAvailable[j] = available[j];
			}
		}
		
		// Calculate the need matrix from subtracting allocation from max matrix
		for (int i=0; i<numOfProcesses; i++) {
			for (int j=0; j<numOfResources; j++) {
				need[i][j] = max[i][j]-allocation[i][j];
			}
		}
		scanner.close();
		br.close();		
	}
	
	// Check if available resources satisfies the needs. 
	static boolean checkIfAvailable(int i) {
		for (int j=0; j<numOfResources; j++) {
			if (available[j] < need[i][j]) {
				return false;
			}
		}
		return true;
	}
	
	
	/*
	 * Begins visiting the processes whose needs can be satisfied from the 
	 * current available resources, by calling the checkAvailable() for
	 * each process. If available resources are too little, the while loop 
	 * moves on to the next process, and so on, until all processes are visited and
	 * added to the safeSequence array.
	 */
	static void isSafe() {
		int processFinished=0;
		while(processFinished<numOfProcesses) {
			for (int i=0; i<numOfProcesses; i++) {
				if (visited[i] == false && checkIfAvailable(i) == true) {
					for (int j=0; j<numOfResources; j++) {
						available[j] += allocation[i][j];
					}
					visited[i] = true;
					safeSequence[processFinished] = i+1;
					processFinished++;
				}
			}
		}
		if (processFinished == numOfProcesses) {
			System.out.println("The system is in a safe state.");
		} else if (processFinished<numOfProcesses) {
			System.out.println("Error. Not all processes were executed.");
		}
	}
	
	// Display all data from the file read, and the final result
	static void displayInputData() {
		System.out.println("\n******** INPUT DATA FROM FILE *********");
		System.out.println("\nFILE INPUT:\n" + file);
		System.out.println("\nPROCESSES:\n"+ numOfProcesses);
		System.out.println("\nRESOURCES:\n" + numOfResources);
		
		System.out.println("\nALLOCATION:");
		for (int i=0; i<numOfProcesses; i++) {
			if (i>0) {
				System.out.println("");
			}
			for (int j=0; j<numOfResources; j++) {
				System.out.print(allocation[i][j]+ " ");
			}
		}
		
		System.out.println("\n\nMAX CLAIM:");
		for (int i=0; i<numOfProcesses; i++) {
			if (i>0) {
				System.out.println("");
			}
			for (int j=0; j<numOfResources; j++) {
				System.out.print(max[i][j]+ " ");
			}
		}
		
		System.out.println("\n\nAVAILABLE:");
		for (int i=0; i<1; i++) {
			for (int j=0; j<numOfResources; j++) {
				System.out.print(initialAvailable[j]+" ");
			}
		}
		System.out.println("\n\n******** RESULT *********");
		System.out.println("\nSAFE SEQUENCE: ");
		System.out.print("<");
		for (int i=0; i<numOfProcesses; i++) {
			if (i<numOfProcesses-1) {
				System.out.print("P"+safeSequence[i]+", ");
			} else {
				System.out.print("P"+safeSequence[i]+"");
			}
		}
		System.out.print(">");
	}
	
	
	public static void main(String[] args) throws IOException {
		
		populateMatrices();
		isSafe();
		displayInputData();
	}
}
