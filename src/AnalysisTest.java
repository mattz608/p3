public class AnalysisTest {
    public static void main(String[] args)  {

                // TODO Add code for checking command line arguments
                args = new String[1];
                args[0] = "./data/data_details.txt";
                PerformanceAnalysisHash ana = new PerformanceAnalysisHash(args[0]);
                ana.compareDataStructures();
                ana.printReport();
            }
}
