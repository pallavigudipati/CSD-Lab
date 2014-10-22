import java.util.ArrayList;
import java.util.List;


public class Bus {

    // List of all the processors listening.
    List<Processor> processors = new ArrayList<Processor>();

    public void notify(int blockNumber) {
        for (Processor processor : processors) {
            processor.update();
        }
    }
}
