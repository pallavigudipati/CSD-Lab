#include <iostream>

#include "cache.h"

using namespace std;

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

string Cache::add(const string address, int instr_num) 
{
	if(address.compare("0xb5faa014")==0)
	{
		cout<<"Blah"<<endl;
	}
	string original_address=address; //address to be copied for block
	cout<<"Address at read entry"<<original_address<<endl;
	int cache_line_num = get_index(address, config_->block_size_,
			config_->associativity_, config_->size_) ;
	int tag = get_tag(address, config_->block_size_, config_->associativity_,
			config_->size_);
	cout<<tag<<endl;
	string replaced_address ="";
	cout<<"Address after index and tag call"<<original_address<<endl;
	CacheBlock *cache_block;
	vector<CacheBlock *> *cache_line = cache_lines_[cache_line_num];
	if (cache_line == NULL) {
		cache_line = new vector<CacheBlock *>();
		cache_lines_[cache_line_num] = cache_line;
	}
	cout<<"After if"<<original_address<<endl;
	for (int i = 0; i < cache_line->size(); ++i) {
		if (cache_line->at(i)->empty_) {
			cache_block = cache_line->at(i);
			//Code added by Varun	
			cache_block->empty_ = false;
			cache_block->dirty_ = false;
			cache_block->tag_ = tag;
			cache_block->last_used_ = instr_num;
			cache_block->frequency_ = 1;
			cout<<"AssignedAddress:"<<original_address<<endl;
			cout<<"ReplacedAddress:"<<replaced_address<<endl;
			cache_block->address_=original_address; //Initialize address to return later in replace
			return replaced_address;
			//Varun added code ends
			break;
		}
	}
	cout<<"After loop"<<original_address<<endl;
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
		replaced_address = replace(cache_line, cache_block);
	}
	if(cache_block==NULL)
	{
		cout<<"NULL"<<endl;
	}
	//cache_block->address_=original_address;
	cout<<"After final if else"<<original_address<<endl;
	cache_block->empty_ = false;
	cout<<"After empty assignment"<<original_address<<endl;
	cache_block->dirty_ = false;
	cout<<"After empty assignment"<<original_address<<endl;
	cache_block->tag_ = tag;
	cout<<"After empty assignment"<<original_address<<endl;
	cache_block->last_used_ = instr_num;
	cout<<"After empty assignment"<<original_address<<endl;
	cache_block->frequency_ = 1;
	cout<<"AssignedAddress:"<<original_address<<endl;
	cout<<"ReplacedAddress:"<<replaced_address<<endl;
	cache_block->address_=original_address; //Initialize address to return later in replace
	return replaced_address;
}

string Cache::replace(vector<CacheBlock *> *cache_line, CacheBlock *cache_block) {
	int lru, lfu;
	switch(config_->replacement_policy_) {
		case 1: // LRU
			cout << "in lru " << endl;
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
	if (cache_block->dirty_) {
		return cache_block->address_;
	}
	return "";
}
