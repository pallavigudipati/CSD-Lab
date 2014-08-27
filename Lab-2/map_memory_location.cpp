#include <cstdlib>
#include <iostream>
#include <sstream>
#include <cmath>
#include <bitset>
using namespace std;
//Converts a hex string to a 32-bit binary string
string get_binary_string(string mem_location)
{
	stringstream ss;
	ss<<hex<<mem_location;
	unsigned n;
	ss>>n;
	bitset<32> b(n);
	return b.to_string();
}
//Gets the decimal value of a binary string. Convert mult to bit shift later, if there are problems.
int get_decimal_value(string bin_string)
{
	int base=1;
	int result=0;
	for(int j=bin_string.length()-1;j>=0;j--)
	{
		result+=base*(bin_string[j]-'0');
		base=base*2;
	}
	return result;	
}
//Compute block_offset. We assume it is present in the last block_offset bits of the string
int get_block_offset(string mem_location,int block_size)
{
	int block_offset_bits=ceil(log2(block_size));
	string bin_string=get_binary_string(mem_location);
	return get_decimal_value(bin_string.substr(32-block_offset_bits,block_offset_bits));
}
int get_index(string mem_location,int block_size,int associativity,int cache_size_KB)
{
	int cache_size=cache_size_KB*1024;
	string bin_string=get_binary_string(mem_location);
	int cache_lines=(cache_size)/(block_size*associativity);
	int index_bits=ceil(log2(cache_lines));
	int block_offset_bits=ceil(log2(block_size));
	return get_decimal_value(bin_string.substr(32-block_offset_bits-index_bits,index_bits));
}
int get_tag(string mem_location,int block_size,int associativity,int cache_size_KB)
{
	int cache_size=cache_size_KB*1024;
	string bin_string=get_binary_string(mem_location);
	int cache_lines=(cache_size)/(block_size*associativity);
	int index_bits=ceil(log2(cache_lines));
	int block_offset_bits=ceil(log2(block_size));
	return get_decimal_value(bin_string.substr(0,32-index_bits-block_offset_bits));
}
/*
int main()
{
	string mem_location="0xabcd19af";
	cout<<get_block_offset(mem_location,256)<<endl;
	cout<<get_index(mem_location,32,4,32)<<endl;
	cout<<get_tag(mem_location,32,4,32)<<endl;
}*/
