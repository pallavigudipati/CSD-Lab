
public class CacheCohenrenceSimulator {

    public static void main(String[] args) {
        Logger logger = new Logger();
        Bus bus = new Bus(logger, Globals.MOESI);
        Processor processor0 = new Processor(logger, 0);
        Processor processor1 = new Processor(logger, 1);

        bus.addListener(processor0.getCache());
        bus.addListener(processor1.getCache());

        for (int i = 0; i < Globals.numIterations; ++i) {
            int blockNumber0 = Utils.randomBlock();
            int blockNumber1 = Utils.randomBlock();
            for (int j = 0; j < Globals.blockSize / Globals.wordSize; ++j) {
                if (Utils.tossCoin()) {
                    // System.out.println("0: block: " + blockNumber0 + " Read");
                    processor0.read(blockNumber0);
                    // System.out.println("1: block: " + blockNumber1 + " Read");
                    processor1.read(blockNumber1);

                } else {
                    // System.out.println("1: block: " + blockNumber1 + " Read");
                    processor1.read(blockNumber1);
                    // System.out.println("0: block: " + blockNumber0 + " Read");
                    processor0.read(blockNumber0);
                }
            }
            if (Utils.tossCoin()) {
                // System.out.println("0: block: " + blockNumber0 + " Write");
                processor0.write(blockNumber0);
                // System.out.println("1: block: " + blockNumber1 + " Write");
                processor1.write(blockNumber1);
            } else {
                // System.out.println("1: block: " + blockNumber1 + " Write");
                processor1.write(blockNumber1);
                // System.out.println("0: block: " + blockNumber0 + " Write");
                processor0.write(blockNumber0);
            }
        }

        logger.printStatistics();
    }
}
