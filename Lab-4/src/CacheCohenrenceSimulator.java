
public class CacheCohenrenceSimulator {

    public static void main(String[] args) {
        Bus bus = new Bus();
        Processor processor0 = new Processor(0);
        Processor processor1 = new Processor(1);

        bus.addListener(processor0);
        bus.addListener(processor1);

        for (int i = 0; i < Globals.numIterations; ++i) {
            Processor processor = Utils.tossCoin() ? processor0 : processor1;
            int blockNumber = Utils.randomBlock();
            processor.read(blockNumber);
            processor.write(blockNumber);
        }
    }
}
