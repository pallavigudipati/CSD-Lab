import java.io.*;
import java.util.*;

public class MemoryInterface 
{
	HashMap<Integer,Integer> MemoryStore=new HashMap<Integer,Integer>();
	public int readLocation(int location)
	{
		if(!MemoryStore.containsKey(location))
		{
			return Global.MEM_DEFAULT_VALUE;
		}
		else
		{
			return MemoryStore.get(location);
		}		
	}
	public void writeLocation(int value,int location)
	{
		MemoryStore.put(location, value);
	}

}
