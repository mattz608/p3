import java.io.*;
import java.util.ArrayList;

public class PerformanceAnalysisHash implements PerformanceAnalysis {

    // The input data from each file is stored in this/ per file
    private ArrayList<String> inputData;
    private String fileName;
    private ArrayList<String> testData;

    public PerformanceAnalysisHash(String details_filename) {
        fileName = details_filename;
        try {
            loadData(details_filename);
        } catch (IOException e) {
            
        }
    }
    @Override
    public void compareDataStructures() {
        compareInsertion();
        compareSearch();
        compareDeletion();
    }

    @Override
    public void printReport() {
        //TODO: Complete this method
    }
// |            FileName|      Operation| Data Structure|   Time Taken (micro sec)|     Bytes Used|
    @Override
    public void compareInsertion() {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long memory = runtime.totalMemory() - runtime.freeMemory();
        long start = System.nanoTime();
        
        HashTable<String,String> ht = new HashTable<String,String>(100,0.75);
        for (String e : inputData) {
            ht.put(e, null);
        }
        long memoryUsed = memory - (runtime.totalMemory() - runtime.freeMemory());
        long timeElapsed = start - System.nanoTime();
        testData.add(fileName + ", " + "PUT, HASHTABLE, " + timeElapsed + ", " + memoryUsed);
        
    }

    @Override
    public void compareDeletion() {
        //TODO: Complete this method
    }

    @Override
    public void compareSearch() {
        //TODO: Complete this method
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
