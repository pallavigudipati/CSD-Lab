#include <iostream>

#include "cache.h"

using namespace std;

void Cache::initialize(CacheConfig *config) {
	config_ = config;
}

bool Cache::lookup(string address, int instr_num) {
	int cache_line_num = get_index(address,config_->block_size_,config_->associativity_,config_->size_) ;
	int tag = get_tag(address,config_->block_size_,config_->associativity_,config_->size_);

	vector<CacheBlock *> *cache_line = cache_lines_[cache_line_num];
	if (cache_line == NULL) {
		return false;
	}
	for (int i = 0; i < cache_line->size(); ++i) {
		if (!(cache_line->at(i)->empty_) && (cache_line->at(i)->tag_ == tag)) {
			cache_line->at(i)->last_used_ = instr_num;
			cache_line->at(i)->frequency_ += 1;
			return true;
		}
	}
	return false;
}

void Cache::add(string address, int instr_num) {
	int cache_line_num; // TODO: varun's code
	int tag; // TODO: varun's code

	CacheBlock *cache_block;
	vector<CacheBlock *> *cache_line = cache_lines_[cache_line_num];
	if (cache_line == NULL) {
		cache_line = new vector<CacheBlock *>();
		cache_lines_[cache_line_num] = cache_line;
	}
	for (int i = 0; i < cache_line->size(); ++i) {
		if (cache_line->at(i)->empty_) {
			cache_block = cache_line->at(i);	
			break;
		}
	}
	// No existing cache block is empty.
	
	// You can add a new cache block. 
	if (cache_line->size() < config_->associativity_) {
		CacheBlock *new_block = new CacheBlock();
		cache_line->push_back(new_block);
		// cache_lines_[cache_line_num] = cache_line; // TODO: required??
		cache_block = new_block;
	} else {
		cache_block = replace(cache_line);
	}

	cache_block->empty_ = false;
	cache_block->tag_ = tag;
	cache_block->last_used_ = instr_num;
	cache_block->frequency_ = 1;
}

CacheBlock* Cache::replace(vector<CacheBlock *> *cache_line) {
	CacheBlock *cache_block;
	int lru, lfu;
	switch(config_->replacement_policy_) {
		case 1: // LRU
			cache_block = cache_line->at(0);
			lru = cache_line->at(0)->last_used_;
			for (int i = 1; i < cache_line->size(); ++i) {
				if (cache_line->at(i)->last_used_ < lru) {
					lru = cache_line->at(i)->last_used_;
					cache_block = cache_line->at(i);
				}
			}
			break;

		case 2: // LFU
			cache_block = cache_line->at(0);
			lfu = cache_line->at(0)->frequency_;
			for (int i = 0; i < cache_line->size(); ++i) {
				if (cache_line->at(i)->frequency_ < lfu) {
					lfu = cache_line->at(i)->frequency_;
					cache_block = cache_line->at(i);
				}
			}
			break;
		
		case 3: // RR
			int index = rand() % (cache_line->size());
			cache_block = cache_line->at(index);
	}
}
