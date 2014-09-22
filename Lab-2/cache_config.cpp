#include <iostream>

using namespace std;

#define LRU 1;
#define LFU 2;
#define RR 3;

/** Defines the cache configuration class. This holds the attributes
 * of the cache which are known only at runtime from the input(in our case
 * the config file) **/
class CacheConfig {
  public:
	int size_;
	int associativity_;
	int block_size_;
	int hit_latency_;
	int replacement_policy_;
};
