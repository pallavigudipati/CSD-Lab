#include <iostream>
#include <fstream>
//#include "configuration.cpp"
//#include "map_memory_location.cpp"
//#include "cache.h"
#include "cache_manager.cpp"
using namespace std;

int main() {
	ifstream istream("config.txt", ifstream::in);
	stringstream buffer;
	buffer << istream.rdbuf();
	string file = buffer.str();

	Configuration config;
	config.ParseString(file);
	//cout << "Num levels " <<  config.levels_ << endl;
	for (int i = 0; i < config.cache_configs_.size(); ++i) {
		//cout << "Level " << i << endl;
		CacheConfig *cache = config.cache_configs_[i];
		//cout << "Size " << cache->size_ << endl;
		//cout << "Assoc " << cache->associativity_ << endl;
		//cout << "Block Size " << cache->block_size_ << endl;
		//cout << "Latency " << cache->hit_latency_ << endl;
		//cout << "Policy " << cache->replacement_policy_ << endl;
	}
    //Create CacheManager object
    CacheManager* cache_manager=new CacheManager();
    cache_manager->initialize(&config);
	cache_manager->initialize_cache_list();
	//Code to illustrate usage of the three extraction functions - tag, block offset and index
    /*
    string mem_location="0xabcd19af";
	cout<<get_block_offset(mem_location,cache->block_size_)<<endl;
	cout<<get_index(mem_location,cache->block_size_,cache->associativity_,cache->size_)<<endl;
	cout<<get_tag(mem_location,cache->block_size_,cache->associativity_,cache->size_)<<endl;
    */
	ifstream infile("pindump.out");
	string a,b,c;
    int instr_num=0;
	while(infile >> a >> b >> c)
	{
		//cout<<a<<endl;
		//cout<<b<<endl;
		//cout<<c<<endl;
		if(b.compare("R")==0)
		{
			//cout<<"READ"<<endl;
			//cout<<c<<endl;
			//cout<<"ENTER READ"<<endl;
			cache_manager->read(c,instr_num);
			//cout<<"READ OVER"<<endl;
		}
		else
		{
			//cout<<"WRITE"<<endl;
			//cout<<c<<endl;
			cache_manager->write_back(c,instr_num);
			//cout<<"WRITE OVER"<<endl;
		}
		//Code for testing individual cache
		/*
        cache_manager->cache_list[0]->add(a,instr_num);
        cout<<cache_manager->cache_list[0]->lookup(a,instr_num,false)<<endl;*/ //Whatever has been added is looked up
		instr_num+=1;
	}
	cache_manager->print_statistics();
}
