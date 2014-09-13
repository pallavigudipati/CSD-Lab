#include <iostream>
#include <vector>
#include <string>
#include <stdlib.h>

#include "utils.cpp"
#include "cache_config.cpp" 

using namespace std;

/** Defines a configuration object which parses the config file
 *  and initializes the config object **/
class Configuration {
  public:
	vector<CacheConfig *> cache_configs_;
	int main_memory_latency_;
	int levels_;

	void ParseString(string config_file) {
		vector<string> lines = Utils::split(config_file, '\n');
		vector<string> levels = Utils::split(lines[0], ' ');
		this->levels_ = atoi(levels[2].c_str());

		int line_no = 2;
		for (int i = 0; i < levels_; ++i) {
			CacheConfig *cache = new CacheConfig();
			vector<string> size = Utils::split(lines[line_no + 1], ' ');
			size[2].erase(size[2].end() - 2, size[2].end());
			cache->size_ = atoi(size[2].c_str());
			vector<string> assoc = Utils::split(lines[line_no + 2], ' ');
			cache->associativity_ = atoi(assoc[2].c_str());
			vector<string> block = Utils::split(lines[line_no + 3], ' ');
			cache->block_size_ = atoi(block[2].c_str());
			vector<string> latency = Utils::split(lines[line_no + 4], ' ');
			cache->hit_latency_ = atoi(latency[2].c_str());
			vector<string> policy = Utils::split(lines[line_no + 5], ' ');
			if (policy[2].compare("LRU") == 0) {
				cache->replacement_policy_ = LRU;
			} else if (policy[2].compare("LFU") == 0) {
				cache->replacement_policy_ = LFU;
			} else if (policy[2].compare("RR") == 0) {
				cache->replacement_policy_ = RR;
			}

			// TODO: Read memory latency too.
			this->cache_configs_.push_back(cache);
			line_no += 7;
		}
	}
};
