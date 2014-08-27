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
	int tag_;
	int last_used_; // For LRU policy.
	int frequency_; // For LFU policy.	
};

class Cache {
  public:
	CacheConfig *config_;
	map<int, vector<CacheBlock *> *> cache_lines_;
	void initialize(CacheConfig *config);
	bool lookup(string address,int instr_num);
	void add(string address,int instr_num);
	CacheBlock* replace(vector<CacheBlock *> *cache_line);
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
