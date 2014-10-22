import java.util.ArrayList;
import java.util.List;


public class Bus {

    // List of all the processors listening.
    List<Processor> processors = new ArrayList<Processor>();

    // TODO: Do the part where it receives the request and check if we have a local copy etc.
    // Else fetch from main memory. Should give out COPY, FRESH or FAILED. In the case of failed 
    // think, processor should retry the command again

    public void notify(int blockNumber, int action) {
        for (Processor processor : processors) {
            processor.update(blockNumber, action);
        }
    }

    public void addListener(Processor processor) {
        processors.add(processor);
        processor.bus = this;
    }
}
