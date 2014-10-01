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
	public void writeLocation(int location,int value)
	{
		MemoryStore.put(location, value);
	}
	public void printState()
	{
		System.out.println("-----Memory State Begin------");
		for(Integer key:MemoryStore.keySet())
		{
			System.out.println("Location:"+key+"Value:"+MemoryStore.get(key));
		}
		System.out.println("-----Memory State End------");		
	}
}
