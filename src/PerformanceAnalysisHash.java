import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

public class PerformanceAnalysisHash implements PerformanceAnalysis {

    // The input data from each file is stored in this/ per file
    private ArrayList<String> inputData;
    private String dataFile;
    private ArrayList<String> fileNames;
    private ArrayList<String> testData;
    private HashTable<Object,Object> hashTable;
    private TreeMap<Object,Object> treeMap;
    private String currentFileName;

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
    
    @Override
    public void compareDataStructures() {
        try {
            for (String s : fileNames) {
                System.out.println("Trying " + s);
                currentFileName = s;
                hashTable = new HashTable<Object,Object>(100000000,0.75);
                treeMap = new TreeMap<Object,Object>();
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
            }  
        } catch (IOException e) {
            
        }
    }

    @Override
    public void printReport() {
        System.out.println("The report name : Performance Analysis Report");
        System.out.println("------------------------------------------------------------------------------------------------ ");
        System.out.println("|            FileName|      Operation| Data Structure|   Time Taken (micro sec)|     Bytes Used|");
        System.out.println("------------------------------------------------------------------------------------------------ ");
        for (String s : testData) {
            String[] data = s.split(",");
            String fileName = data[0];
            String operation = data[1];
            String dataStruct = data[2];
            String time = data[3];
            String bytes = data[4];
            System.out.printf("|%20s|%15s|%15s|%25s|%15s\n", fileName,operation,dataStruct,time,bytes);
        }
        System.out.println("------------------------------------------------------------------------------------------------- ");
    }
// |            FileName|      Operation| Data Structure|   Time Taken (micro sec)|     Bytes Used|
    @Override
    public void compareInsertion() {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMemory = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();
        
        for (String e : inputData) {
            hashTable.put(e, null);
        }
        
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

    @Override
    public void compareDeletion() {
        //TODO: Complete this method
    }

    @Override
    public void compareSearch() {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMemory = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();
        
        for (String e : inputData) {
            hashTable.get(e);
        }
        
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

    /*
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
