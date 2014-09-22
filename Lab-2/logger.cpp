#include <iostream>
#include <vector>

using namespace std;
/** A logger object logs various types of memory accesses.
 *  Each cache has its own logger which records the statistics for that
 *  cache */
class Logger {
  public:
	vector<int> num_cache_hits_;
	vector<int> num_cache_misses_;
	vector<int> num_accesses_;

	void initialize(int num_caches) {
		num_cache_hits_.resize(num_caches);
		num_cache_misses_.resize(num_caches);
		num_accesses_.resize(num_caches);
	}

	void log_hit(int cache_level) {
		num_cache_hits_[cache_level] += 1;
	}

	void log_miss(int cache_level) {
		num_cache_misses_[cache_level] += 1;
	}

	void log_access(int cache_level) {
		num_accesses_[cache_level] += 1;
	}

	void print_log() {
		for (int i = 0; i < num_cache_hits_.size(); ++i) {
			cout << "Cache L" << i << " " << num_cache_misses_[i] << " "
				<< num_cache_hits_[i] << " " << num_accesses_[i] << endl;
		}
	}
};
