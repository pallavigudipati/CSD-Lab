
public class CacheCohenrenceSimulator {

    public static void main(String[] args) {
        Logger logger = new Logger();
        Bus bus = new Bus(logger, Globals.MOESI);
        Processor processor0 = new Processor(logger, 0);
        Processor processor1 = new Processor(logger, 1);

        bus.addListener(processor0.cache);
        bus.addListener(processor1.cache);

        for (int i = 0; i < Globals.numIterations; ++i) {
            int blockNumber0 = Utils.randomBlock();
            int blockNumber1 = Utils.randomBlock();
            for (int j = 0; j < Globals.blockSize / Globals.wordSize; ++j) {
                processor0.read(blockNumber0);
                processor1.read(blockNumber1);
            }
            processor0.write(blockNumber0);
            processor1.write(blockNumber1);
        }

        logger.printStatistics();
    }
}
