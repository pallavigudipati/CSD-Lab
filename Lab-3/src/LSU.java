import java.util.*;

import ReOrderBuffer.Entry;
public class LSU 
{
	public class LoadEntry
	{
		//What will a LoadEntry need
		public Instruction instruction; //The Load Instruction
		//TODO : Since these come from ROB, you may have actual values for the source
		public int rrfTagDestination=-1; //The RRF Tag, for the destination(Register)
		public int rrfTagSource=-1;     //The RRF Tag for the source(Memory Location)
		public LoadEntry(Instruction instruction,int rrfTagDestination,int rrfTagSource)
		{
			this.instruction=instruction;
			this.rrfTagDestination=rrfTagDestination;
			this.rrfTagSource=rrfTagSource;
		}
	}
	
	public class StoreEntry
	{
		//What will a LoadEntry need
		public Instruction instruction; //The Load Instruction
		//TODO : Since these come from ROB, you may have actual values for the source
		public int rrfTagDestination=-1; //The RRF Tag, for the destination(Register)
		public int rrfTagSource=-1;     //The RRF Tag for the source(Memory Location)
		public boolean loadBypassed=false;  //If it has been load bypassed
		public StoreEntry(Instruction instruction,int rrfTagDestination,int rrfTagSource)
		{
			this.instruction=instruction;
			this.rrfTagDestination=rrfTagDestination;
			this.rrfTagSource=rrfTagSource;
		}			
	}
	
    public Queue<LoadEntry> loadQueue = new LinkedList<LoadEntry>();
    public Queue<StoreEntry> storeQueue=new LinkedList<StoreEntry>();
    public ARF arf;
    public MemoryInterface memoryInterface;
    
    public LSU(ARF arf,MemoryInterface memoryInterface)
    {
    	this.arf=arf;
    	this.memoryInterface=memoryInterface;
    }
    public void StoreToMem()
    {
    	//This function assumes the StoreQueue is non-empty
    	StoreEntry storeEntry=storeQueue.remove();
    	//TODO Get the value and location to be written
    	Object[] valueResult=arf.readRegister(storeEntry.rrfTagSource);
    	int value=(Integer)(valueResult[1]);
    	Object[] locationResult=arf.readRegister(storeEntry.rrfTagDestination);    	
    	int location=(Integer)(locationResult[1]);
    	//Write to the memory
    	memoryInterface.writeLocation(value, location);
    }
    public void LoadFromMem()
    {
    	LoadEntry loadEntry=loadQueue.remove();
    	//TODO Get the memorySource
    	Object[] memorySourceResult=arf.readRegister(loadEntry.rrfTagSource);
    	int memorySource=(Integer)(memorySourceResult[1]);
    	
    	int value=memoryInterface.readLocation(memorySource);
    	//TODO Write the value to ARF
    }
    
    public void executeNext()
    {
    	//Inspect both load and store queues, and then arrive at a decision
    	if(storeQueue.size()==0)
    	{
    		if(loadQueue.size()==0)
    		{
    			//Do nothing
    		}
    		else
    		{
    			LoadFromMem();
    		}    			
    	}
    	else if(loadQueue.size()==0)
    	{
    		StoreToMem();
    	}
    	else
    	{
    		//Both the loadQueue and the storeQueue have non-zero size
    		StoreEntry topStoreElement= storeQueue.peek();
    		LoadEntry topLoadElement = loadQueue.peek();
    		if(topStoreElement.loadBypassed)
    		{
    			//The current store head was already bypassed once, hence prefer it
    			StoreToMem();
    		}
    		else
    		{
    			LoadFromMem();
    			topStoreElement.loadBypassed=true;
    		}
    	}
    	
    }
    
    public void addStoreEntry(int sizeOfSB,Instruction instruction,int rrfTagDestination,int rrfTagSource)
    {
    	if(storeQueue.size()==sizeOfSB)
    	{
    		return;
    	}
    	else
    	{
    		StoreEntry newStoreEntry=new StoreEntry(instruction,rrfTagDestination,rrfTagSource);
    		storeQueue.add(newStoreEntry);
    	}    	
    }

    public void addLoadEntry(int sizeOfLB,Instruction instruction,int rrfTagDestination,int rrfTagSource)
    {
    	if(loadQueue.size()==sizeOfLB)
    	{
    		return;
    	}
    	else
    	{
    		LoadEntry newLoadEntry=new LoadEntry(instruction,rrfTagDestination,rrfTagSource);
    		loadQueue.add(newLoadEntry);
    	}    	
    }
    
    public boolean IsMatching(int location)
    {
    	//Checks if there is an entry in storeQueue storing to the same location
    	for(StoreEntry storeEntry:storeQueue)
    	{
    		//TODO Get the value of the location from this entry
    		int storeDestination=0;
    		if(storeDestination==location)
    		{
    			return true;
    		}
    	}
    }
	
}
