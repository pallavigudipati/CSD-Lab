#include <iostream>
#include <vector>

using namespace std;

class Logger {
  public:
	vector<int> num_cache_hits_;
	vector<int> num_cache_misses_; // TODO: required?
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
		// TODO: print the required stuff.
		for (int i = 0; i < num_cache_hits_.size(); ++i) {
			cout << i << " " << num_cache_misses_[i] << " " 
				<< num_cache_hits_[i] << endl;
		}
	}
};
