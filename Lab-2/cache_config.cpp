#include <iostream>

using namespace std;

#define LRU 1;
#define LFU 2;
#define RR 3;

class CacheConfig {
  public:
	int size_;
	int associativity_;
	int block_size_;
	int hit_latency_;
	int replacement_policy_;
};
