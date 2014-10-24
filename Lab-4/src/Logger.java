import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;


public class Logger {

    private List<HashMap<String, Integer>> statistics = new ArrayList<HashMap<String, Integer>>();
    private List<HashMap<Integer, Integer>> coherenceRequests =
            new ArrayList<HashMap<Integer, Integer>>();

    public Logger() {
        for (int i = 0; i < Globals.numProcessors; ++i) {
            statistics.add(new HashMap<String, Integer>());
            coherenceRequests.add(new HashMap<Integer, Integer>());
        }
    }

    public void logStateChange(int oldState, int newState, int processorId) {
        HashMap<String, Integer> processorStatistics = statistics.get(processorId);
        String key = Utils.statesToString(oldState, newState);
        // System.out.println(processorId + ": " + key);
        int oldCount = processorStatistics.containsKey(key) ? processorStatistics.get(key) : 0;
        processorStatistics.put(key, oldCount + 1);
    }

    public void logCoherenceRequest(int requestType, int processorId) {
        HashMap<Integer, Integer> processorRequests = coherenceRequests.get(processorId);
        int oldCount = processorRequests.containsKey(requestType) ? processorRequests
                .get(requestType) : 0;
        processorRequests.put(requestType, oldCount + 1);
    }

    public void printStatistics() {
        int processorId = 0;
        System.out.println("State Changes\n");
        for (HashMap<String, Integer> processorStatistics : statistics) {
            System.out.println("Processor " + ++processorId);
            System.out.println("------------------------------------------------------------");
            for (Entry<String, Integer> statistic : processorStatistics.entrySet()) {
                System.out.println(statistic.getKey() + ": " + statistic.getValue());
            }
            System.out.println();
        }
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        processorId = 0;
        System.out.println("Coherence Requests\n");
        for (HashMap<Integer, Integer> processorRequests : coherenceRequests) {
            System.out.println("Processor " + ++processorId);
            System.out.println("------------------------------------------------------------");
            for (Entry<Integer, Integer> request : processorRequests.entrySet()) {
                System.out.println(Utils.requestTypeToString(request.getKey())
                        + ": " + request.getValue());
            }
            System.out.println();
        }
    }
}
