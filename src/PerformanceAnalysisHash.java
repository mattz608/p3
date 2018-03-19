import java.io.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;
///////////////////////////////////////////////////////////////////////////////
//Title:            P3
//Files:            Hash Table.java, PerformanceAnalysisTest.java, AnalysisTest.java, 
//HashTableADT.java, PerformanceAnalysis.java
//Semester:         CS 400 Spring 2018
//
//Authors:          Matt Zimmers, Tarun Mandalapu
//Email:            tmandalapu@wisc.edu, mzimmers@wisc.edu
//Lecturer's Name:  Debra Deppeler
//Source Credits:   
//Known Bugs:       bytes used differs greatly from sample file
///////////////////////////////////////////////////////////////////////////////

public class PerformanceAnalysisHash implements PerformanceAnalysis {
    
    /**
     * Line by line data from the current data file being worked in
     */
    private ArrayList<String> inputData;
    
    /**
     * This is actually the path to the data files
     */
    private String dataFile;
    
    /**
     * All file names that will be compared
     */
    private ArrayList<String> fileNames;
    
    /**
     * Feedback from every test done to every file
     */
    private ArrayList<String> testData;
    
    /**
     * The hash table we made for testing. Re-referenced for each file tested
     */
    private HashTable<Object,Object> hashTable;
    
    /**
     * The hash table being compared to that's built in. Also re-referenced
     * for each file tested
     */
    private TreeMap<Object,Object> treeMap;
    
    /**
     * The current data file being read from so that it can be recorded in testData
     */
    private String currentFileName;

    /**
     * Initializes some fields and processes the details file given. It uses
     * this path to read a file containing the file names to be tested.
     * @param details_filename the path to the relevant files for testing
     */
    public PerformanceAnalysisHash(String details_filename) {
        try {
            // A couple initiations
            inputData = new ArrayList<String>();
            testData = new ArrayList<String>();
            
            // Find details file and add lines to an ArrayList
            File details = new File(details_filename);
            Scanner reader = new Scanner(details);
            ArrayList<String> lines = new ArrayList<String>();
            while (reader.hasNextLine()) {
                lines.add(reader.nextLine());
            }
            
            // Find and format the data file path
            dataFile = lines.get(0).split(",")[1].trim();
            dataFile = dataFile.replace("\\",File.separator).replace("/",File.separator).trim(); 
            
            // Get the rest of the file names
            fileNames = new ArrayList<String>();
            for (int i = 1; i < lines.size(); i++) {
                fileNames.add(lines.get(i).split(",")[0].trim());
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + details_filename + " was not found");
        }
        
    }
    
    /**
     * Goes through every file and runs the get, search and remove methods on them.
     */
    @Override
    public void compareDataStructures() {
        try {
            for (String s : fileNames) {
                // Reset local references
                System.out.println("Trying " + s);
                currentFileName = s;
                hashTable = new HashTable<Object,Object>(100000000,0.75);
                treeMap = new TreeMap<Object,Object>();
                // new loadData method call will populate appropriate inputData
                inputData.clear();
                
                System.out.println("Loading Data...");
                loadData(dataFile + File.separator + s);
                
                System.out.println("Comparing Insertion...");
                compareInsertion();
                System.out.println("Comparing Search...");
                compareSearch();
                System.out.println("Comparing Deletion...");
                compareDeletion();
                System.out.println();
                hashTable.clear();
                treeMap.clear();
            }  
        } catch (IOException e) {
            
        }
    }

    /**
     * Gives a detailed report for all the different files tested for each operation.
     */
    @Override
    public void printReport() {
        System.out.println("The report name : Performance Analysis Report");
        System.out.println("------------------------------------------------------------------------------------------------ ");
        System.out.println("|            FileName|      Operation| Data Structure|   Time Taken (micro sec)|     Bytes Used|");
        System.out.println("------------------------------------------------------------------------------------------------ ");
        PrintWriter pw;
        try {
            pw = new PrintWriter(new FileWriter(new File ("results.txt")), true);     
            pw.println("The report name : Performance Analysis Report");
            pw.println("------------------------------------------------------------------------------------------------ ");
            pw.println("|            FileName|      Operation| Data Structure|   Time Taken (micro sec)|     Bytes Used|");
            pw.println("------------------------------------------------------------------------------------------------ ");
            for (String s : testData) { // Every entry is a detailed report
                String[] data = s.split(",");
                String fileName = data[0];
                String operation = data[1];
                String dataStruct = data[2];
                String time = data[3];
                String bytes = data[4];
                System.out.printf("|%20s|%15s|%15s|%25s|%15s|\n", fileName,operation,dataStruct,time,bytes);
                pw.printf("|%20s|%15s|%15s|%25s|%15s|\n", fileName,operation,dataStruct,time,bytes);
            }
            System.out.println("------------------------------------------------------------------------------------------------- ");
            pw.println("------------------------------------------------------------------------------------------------- ");
            pw.close();
            
        } catch (Exception e) {
            
        }
    }
    
    /**
     * Records data for the way that our HashTable and Java's TreeMap handles inserting data
     * from a certain file.
     */
    @Override
    public void compareInsertion() {
        // Set up start memory and time references
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMemory = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();
        
        for (String e : inputData) {
            hashTable.put(e, null);
        }
        
        // Compare memory and time difference
        long memoryUsed = (runtime.totalMemory() - runtime.freeMemory()) - startMemory;
        long timeElapsed = (System.nanoTime() - startTime)*1000;
        testData.add(currentFileName + ", " + "PUT, HASHTABLE, " + timeElapsed + ", " + memoryUsed);
        
        // Repeat for TreeMap
        
        runtime.gc();
        startMemory = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.nanoTime();
        
        for (String e : inputData) {
            treeMap.put(e, null);
        }
        
        memoryUsed = (runtime.totalMemory() - runtime.freeMemory()) - startMemory;
        timeElapsed = (System.nanoTime() - startTime)*1000;
        testData.add(currentFileName + ", " + "PUT, TREEMAP, " + timeElapsed + ", " + memoryUsed);
        
    }

    /**
     * Records data for the way that our HashTable and Java's TreeMap handles deleting data
     * from a certain file.
     */
    @Override
    public void compareDeletion() {
     // Set up start memory and time references
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMemory = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();
        
        for (String e : inputData) {
            hashTable.remove(e);
        }
        
        // Compare memory and time difference
        long memoryUsed = (runtime.totalMemory() - runtime.freeMemory()) - startMemory;
        long timeElapsed = (System.nanoTime() - startTime)*1000;
        testData.add(currentFileName + ", " + "REMOVE, HASHTABLE, " + timeElapsed + ", " + memoryUsed);
        
         // Repeat for TreeMap
        
        runtime.gc();
        startMemory = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.nanoTime();
        
        for (String e : inputData) {
            treeMap.remove(e);
        }
        
        memoryUsed = (runtime.totalMemory() - runtime.freeMemory()) - startMemory;
        timeElapsed = (System.nanoTime() - startTime)*1000;
        testData.add(currentFileName + ", " + "REMOVE, TREEMAP, " + timeElapsed + ", " + memoryUsed);
    }

    /**
     * Records data for the way that our HashTable and Java's TreeMap handles searches data
     * from a certain file.
     */
    @Override
    public void compareSearch() {
        // Set up start memory and time references
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMemory = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();
        
        for (String e : inputData) {
            hashTable.get(e);
        }
        
        // Compare memory and time difference
        long memoryUsed = (runtime.totalMemory() - runtime.freeMemory()) - startMemory;
        long timeElapsed = (System.nanoTime() - startTime)*1000;
        testData.add(currentFileName + ", " + "GET, HASHTABLE, " + timeElapsed + ", " + memoryUsed);
        
         // Repeat for TreeMap
        
        runtime.gc();
        startMemory = runtime.totalMemory() - runtime.freeMemory();
        startTime = System.nanoTime();
        
        for (String e : inputData) {
            treeMap.get(e);
        }
        
        memoryUsed = (runtime.totalMemory() - runtime.freeMemory()) - startMemory;
        timeElapsed = (System.nanoTime() - startTime)*1000;
        testData.add(currentFileName + ", " + "GET, TREEMAP, " + timeElapsed + ", " + memoryUsed);
    }

    /**
    An implementation of loading files into local data structure is provided to you
    Please feel free to make any changes if required as per your implementation.
    However, this function can be used as is.
     */
    @Override
    public void loadData(String filename) throws IOException {

        // Opens the given test file and stores the objects each line as a string
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));
        inputData = new ArrayList<>();
        String line = br.readLine();
        while (line != null) {
            inputData.add(line);
            line = br.readLine();
        }
        br.close();
    }
}
