#include <iostream>

#include "cache.h"

using namespace std;

/** Defines functions for cache level operations. The cache manager will
 * in turn use this functions on every cache in its set of caches **/
void Cache::initialize(CacheConfig *config) {
	config_ = config;
}

bool Cache::lookup(string address, int instr_num, bool set_dirty) {
	int cache_line_num = get_index(address, config_->block_size_,
			config_->associativity_, config_->size_) ;
	int tag = get_tag(address,config_->block_size_, config_->associativity_,
			config_->size_);

	vector<CacheBlock *> *cache_line = cache_lines_[cache_line_num];
	if (cache_line == NULL) {
		return false;
	}
	for (int i = 0; i < cache_line->size(); ++i) {
		if (!(cache_line->at(i)->empty_) && (cache_line->at(i)->tag_ == tag)) {
			cache_line->at(i)->last_used_ = instr_num;
			cache_line->at(i)->frequency_ += 1;
			if (set_dirty) {
				cache_line->at(i)->dirty_ = true;
			}
			return true;
		}
	}
	return false;
}

bool Cache::remove(string address)
{
	//The return type denotes whether the block is dirty or not, to the calling function.
	int cache_line_num = get_index(address, config_->block_size_,
			config_->associativity_, config_->size_) ;
	int tag = get_tag(address,config_->block_size_, config_->associativity_,
			config_->size_);

	vector<CacheBlock *> *cache_line = cache_lines_[cache_line_num];
	if (cache_line == NULL) {
		return false;
	}
	for (int i = 0; i < cache_line->size(); ++i) {
		if (!(cache_line->at(i)->empty_) && (cache_line->at(i)->tag_ == tag)) {
			//Invalidate the block, set it to empty
			cache_line->at(i)->empty_=true;
			if(cache_line->at(i)->dirty_)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	//If this happens, this means the assumptions made as to the presence of this address in the cache are violated
	//cout<<"Address Not Found In Cache"<<endl;
	return false;
}

string Cache::add(const string address, int instr_num,bool &was_replaced,string &with_address) 
{
	was_replaced=false;
	with_address="";
	//string original_address=address; //address to be copied for block
	int cache_line_num = get_index(address, config_->block_size_,
			config_->associativity_, config_->size_) ;
	int tag = get_tag(address, config_->block_size_, config_->associativity_,
			config_->size_);
	CacheBlock *cache_block;
	string replaced_address="";
	vector<CacheBlock *> *cache_line = cache_lines_[cache_line_num];
	if (cache_line == NULL) {
		cache_line = new vector<CacheBlock *>();
		cache_lines_[cache_line_num] = cache_line;
	}
	for (int i = 0; i < cache_line->size(); ++i) {
		if (cache_line->at(i)->empty_) {
			cache_block = cache_line->at(i);
			//Code added by Varun	
			cache_block->empty_ = false;
			cache_block->dirty_ = false;
			cache_block->tag_ = tag;
			cache_block->last_used_ = instr_num;
			cache_block->frequency_ = 1;
			cache_block->address_=address; //Initialize address to return later in replace
			return replaced_address;
			//Varun added code ends
			break;
		}
	}
	// No existing cache block is empty.
	
	// You can add a new cache block. 
	if (cache_line->size() < config_->associativity_) {
		CacheBlock *new_block = new CacheBlock();
		new_block->dirty_ = false;
		new_block->empty_ = true;
		cache_line->push_back(new_block);
		// cache_lines_[cache_line_num] = cache_line; // TODO: required??
		cache_block = new_block;
	} else {
		was_replaced=true;
		replaced_address = replace(cache_line, &cache_block,with_address);
	}
	if(cache_block==NULL)
	{
		//cout<<"NULL"<<endl;
	}
	//cache_block->address_=original_address;
	cache_block->empty_ = false;
	cache_block->dirty_ = false;
	cache_block->tag_ = tag;
	cache_block->last_used_ = instr_num;
	cache_block->frequency_ = 1;
	cache_block->address_=address; //Initialize address to return later in replace
	return replaced_address;
}

string Cache::replace(vector<CacheBlock *> *cache_line, CacheBlock **cache_block,string &with_address) {
	int lru, lfu;
	switch(config_->replacement_policy_) {
		case 1: // LRU
			//cout << "in lru " << endl;
			*cache_block = cache_line->at(0);
			lru = cache_line->at(0)->last_used_;
			for (int i = 1; i < cache_line->size(); ++i) {
				if (cache_line->at(i)->last_used_ < lru) {
					lru = cache_line->at(i)->last_used_;
					*cache_block = cache_line->at(i);
				}
			}
			break;

		case 2: // LFU
			*cache_block = cache_line->at(0);
			lfu = cache_line->at(0)->frequency_;
			for (int i = 0; i < cache_line->size(); ++i) {
				if (cache_line->at(i)->frequency_ < lfu) {
					lfu = cache_line->at(i)->frequency_;
					*cache_block = cache_line->at(i);
				}
			}
			break;
		
		case 3: // RR
			int index = rand() % (cache_line->size());
			*cache_block = cache_line->at(index);
	}
	with_address=(*cache_block)->address_;
	if ((*cache_block)->dirty_) {
		//cout<<"DirtyReplace"<<endl;
		return (*cache_block)->address_;
	}
	return "";
}
