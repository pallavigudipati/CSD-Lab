#include <iostream>

#include "cache.cpp"
using namespace std;

void CacheManager::initialize(Configuration *config)
{
    config_=config;
}

void CacheManager::initialize_cache_list()
{
    for(int i=0;i<config_->levels_;i++)
    {
        Cache* cache_obj=new Cache();
        cache_obj->initialize((config_->cache_configs_)[i]);
        cache_list.push_back(cache_obj);
    }
    cout<<cache_list.size()<<endl;
}
void CacheManager::read(string address, int instr_num)
{
    int foundAt=config_->levels_;
    for(int i=0;i<config_->levels_;i++)
    {
        if(cache_list[i]->lookup(address,instr_num,false))
        {
            foundAt=i;
            break;
        }
    }
    if(foundAt==0)
    {
        //Nothing more to do, return
        return;
    }
    else
    {
        //foundAt less than i(levels_ means value only present in Main Memory
        //Code needs to be changed to handle write_back
        for(int i=0;i<foundAt;i++)
        {
            cache_list[i]->add(address,instr_num);
        }
    }
}
void CacheManager::write_back(string address, int instr_num)
{
	//Write to the closest(index 0) cache
	//If you remove any block, write back to the next level
	//This will cascade
	cache_list[0]->add(address,instr_num);	
}

void CacheManager::set_logger(Logger *logger)
{
	logger_ = logger;
}
