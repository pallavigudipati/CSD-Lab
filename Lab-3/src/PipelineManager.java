import java.util.Queue;

/*
 * Main class that manages the pipeline.
 */
public class PipelineManager {
    public ARF arf;
    public ReservationStation reservationStation;
    public ReOrderBuffer reOrderBuffer;
    public ALU[] aluUnits;
    public Parameters parameters;

    public PipelineManager() {
        parameters = Utils.parseParameters("parameters.txt");
        arf = new ARF(new RRF());
        reservationStation = new ReservationStation(arf, parameters.sizeOfRS);
        MemoryInterface memoryInterface = new MemoryInterface();
        LoadStoreUnit loadStoreUnit = new LoadStoreUnit(arf,memoryInterface,parameters.sizeOfSB,parameters.sizeOfLB);
        reOrderBuffer = new ReOrderBuffer(arf, parameters.sizeOfROB,loadStoreUnit);
        aluUnits = new ALU[Global.NUM_ALU];
        for (int i = 0; i < aluUnits.length; ++i) {
            aluUnits[i] = new ALU(parameters.latency, arf);
        }
    }

    public void runPipeline(Queue<Instruction> instructions) {
        int currentCycle = 0;
        // TODO: check termination condition
        while (!(reOrderBuffer.buffer.isEmpty() && reservationStation.buffer.isEmpty()
                && instructions.isEmpty())) {
            // Complete any pending tasks in Re-order buffer.
            reOrderBuffer.completePending();

            // Check if ALUs are completed with their current work.
            for (int i = 0; i < Global.NUM_ALU; ++i) {
                if (aluUnits[i].isReady(currentCycle)) {
                    // ALU has completed the work. Push the result to its RRF register.
                    int[] rrfTagResult = aluUnits[i].pushResult();
                    // Forward the value to the reservationStation
                    // TODO: should we forward to RRF later?
                    reservationStation.forward(rrfTagResult[0], rrfTagResult[1]);
                }
            }

            //If there are load store instructions at the head of RS, send to ROB
            if(!reOrderBuffer.isFull())
            {
            	
            }
            // If an ALU is free, fetch and put from Reservation Station.
            for (int i = 0; i < Global.NUM_ALU; ++i) {
                if (!aluUnits[i].busy) {
                    // ALU is free.
                    ReservationStation.Entry entry = reservationStation.getFirstReadyEntry(); 
                    if (entry != null) {
                        // There is a ready entry in the reservation station.
                        aluUnits[i].startExecution(entry.instruction,
                                currentCycle, entry.operandA.tagOrValue,
                                entry.operandB.tagOrValue,
                                entry.destination.tagOrValue);
                    }
                }
            }

            // If either the Station or the ROB is full, the instruction is not dispatched.
            // Does not dispatch if no RRF register is empty for Destination.
            if (!reservationStation.isFull() && !reOrderBuffer.isFull() 
                    && !instructions.isEmpty()) {
                Instruction instruction = instructions.peek();
                int rrfTag = -1;
                try {
                    rrfTag = reservationStation.fillEntry(instruction);
                } catch (RRFFullException e) {
                    currentCycle += 1;
                    continue;
                }
                reOrderBuffer.fillEntry(instruction, rrfTag);
                instructions.poll();
            }
            currentCycle += 1;
        }
        System.out.println(currentCycle);
    }

    public static void main(String[] args) {
        PipelineManager pipelineManager = new PipelineManager();
        Queue<Instruction> instructions = Utils.parseInstructions("instructions.txt");
        pipelineManager.runPipeline(instructions);
    }
}
