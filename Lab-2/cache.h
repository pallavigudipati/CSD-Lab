#include <iostream>
#include <map>
#include <vector>
#include "configuration.cpp"
#include "logger.cpp"
#include "map_memory_location.cpp"
using namespace std;

class CacheBlock {
  public:
  	bool empty_;
	bool dirty_;
	int tag_;
	string address_;
	int last_used_; // For LRU policy.
	int frequency_; // For LFU policy.	
};

class Cache {
  public:
	CacheConfig *config_;
	map<int, vector<CacheBlock *> *> cache_lines_;
	void initialize(CacheConfig *config);
	bool lookup(string address,int instr_num, bool set_dirty);
	string add(string address,int instr_num);
	string replace(vector<CacheBlock *> *cache_line, CacheBlock **cache_block);
};

class CacheManager {
    public:
        Configuration *config_;
        vector<Cache*> cache_list;
		Logger *logger_;		
        void initialize(Configuration *config);
        void initialize_cache_list();
		void set_logger(Logger *logger);
        void read(string address, int instr_num);
		void write_back(string address,int instr_num);
		void write_through();
};
