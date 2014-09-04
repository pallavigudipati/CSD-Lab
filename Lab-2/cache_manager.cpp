#include <iostream>
#include "cache.cpp"
using namespace std;

void CacheManager::initialize(Configuration *config)
{
    config_=config;
	Logger* logger=new Logger();
	logger->initialize(config_->levels_);
	set_logger(logger);
}

void CacheManager::initialize_cache_list()
{
    for(int i=0;i<config_->levels_;i++)
    {
        Cache* cache_obj=new Cache();
        cache_obj->initialize((config_->cache_configs_)[i]);
        cache_list.push_back(cache_obj);
    }
    //cout<<cache_list.size()<<endl;
}

void CacheManager::print_statistics() {
	logger_->print_log();
}

void CacheManager::read(string address, int instr_num)
{
    int foundAt=config_->levels_;
    for(int i=0;i<config_->levels_;i++)
    {
        if(cache_list[i]->lookup(address,instr_num,false))
        {
            foundAt=i;
			logger_->num_cache_hits_[i]+=1;
			logger_->num_accesses_[i]+=1;
            break;
        }
		logger_->num_cache_misses_[i]+=1;
		logger_->num_accesses_[i]+=1;
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
		/*
		for(int i=0;i<foundAt;i++)
		{
			string replaced_address;
			replaced_address=cache_list[i]->add(address,instr_num);
			if(replaced_address.compare("")==0)
			{
				//Nothing was replaced, so no action to be done
				continue;
			}
			else if(i+1>config_->levels_-1)
			{
				//The previous level is memory, you need not do anything
				continue;		

			}	
			else
			{
				//Since it is inclusive, it is guaranteed to be in the level (i+1)
				bool looked_up=cache_list[i+1]->lookup(replaced_address,instr_num,true);
				if(!looked_up)
				{
					cout<<"Constraint violated read"<<endl;
				}
			}
		
		}
		*/
		for(int i=0;i<foundAt;i++)
		{
			string dirty_address;
			bool was_replaced=false;
			string replaced_address=""; 
			dirty_address=cache_list[i]->add(address,instr_num,was_replaced,replaced_address);
			logger_->num_accesses_[i]+=1;
			if(replaced_address.compare("")==0)
			{
				//Nothing was replaced, so no action to be done
				continue;
			}
			else if(i+1>config_->levels_-1)
			{
				remove_inclusive(replaced_address,instr_num,i-1);
				//The previous level is memory, you need not write back
				continue;		

			}
			else if(dirty_address.compare("")==0)
			{
				//This means that the replaced block was not dirty
				bool was_dirty=remove_inclusive(replaced_address,instr_num,i-1);
				if(was_dirty)
				{
					bool looked_up=cache_list[i+1]->lookup(replaced_address,instr_num,true);
					logger_->num_accesses_[i+1]+=1;
					if(!looked_up)
					{
						//cout<<"Constraint violated"<<endl;
					}
				}				
			}
			else
			{
				remove_inclusive(replaced_address,instr_num,i-1);
				//Since it is inclusive, it is guaranteed to be in the level (i+1)
				bool looked_up=cache_list[i+1]->lookup(replaced_address,instr_num,true);
				logger_->num_cache_misses_[i]+=1;
				logger_->num_accesses_[i]+=1;
				if(!looked_up)
				{
					//cout<<"Constraint violated write"<<endl;
				}
			}
		
		}



    }
}
void CacheManager::write_back(string address, int instr_num)
{
	//Write to the closest(index 0) cache
	//If you remove any block, write back to the next level
	//This will cascade
	int foundAt = config_->levels_;
	for(int i=0;i<config_->levels_;i++)
	{
		if(cache_list[i]->lookup(address,instr_num,true))
		{
			foundAt=i;
			logger_->num_cache_hits_[i]+=1;
			logger_->num_accesses_[i]+=1;
			break;
		}
		logger_->num_cache_misses_[i]+=1;
		logger_->num_accesses_[i]+=1;
	}
	//add the address to every closer level of cache
	for(int i=0;i<foundAt;i++)
	{
		string dirty_address;
		bool was_replaced=false;
		string replaced_address=""; 
		dirty_address=cache_list[i]->add(address,instr_num,was_replaced,replaced_address);
		logger_->num_accesses_[i]+=1;
		if(replaced_address.compare("")==0)
		{
			//Nothing was replaced, so no action to be done
			//cout<<"Case 1"<<endl;
			continue;
		}
		else if(i+1>config_->levels_-1)
		{
			//cout<<"Case 2"<<endl;
			remove_inclusive(replaced_address,instr_num,i-1);
			//The previous level is memory, you need not write back
			continue;		

		}
		else if(dirty_address.compare("")==0)
		{
			//This means that the replaced block was not dirty
			//cout<<"Case 3"<<endl;
			bool was_dirty=remove_inclusive(replaced_address,instr_num,i-1);
			if(was_dirty)
			{
				bool looked_up=cache_list[i+1]->lookup(replaced_address,instr_num,true);
				logger_->num_accesses_[i+1]+=1;
				if(!looked_up)
				{
					//cout<<"Constraint violated"<<endl;
				}
			}				
		}
		else
		{
			//cout<<"Case 4"<<endl;
			remove_inclusive(replaced_address,instr_num,i-1);
			//Since it is inclusive, it is guaranteed to be in the level (i+1)
			bool looked_up=cache_list[i+1]->lookup(replaced_address,instr_num,true);
			logger_->num_accesses_[i+1]+=1;
			logger_->num_cache_misses_[i]+=1;
			logger_->num_accesses_[i+1]+=1;
			if(!looked_up)
			{
				//cout<<"Constraint violated write"<<endl;
			}
		}
		
	}

						
}

bool CacheManager::remove_inclusive(string address,int instr_num,int k)
{
	//Remove the address from all levels where it exists from k downwards, returns true if it was dirty anywhere
	bool was_dirty=false;
	if(k<0)
	{
		return false;
	}
	for(int i=k;i>=0;i--)
	{
		//check if the address exists at this level, if not there's no need to go further down.
		if(!cache_list[i]->lookup(address,instr_num,false))
		{
			logger_->num_accesses_[i]+=1;
			break;
		}
		bool i_dirty = cache_list[i]->remove(address);
		if(i_dirty)
		{
			was_dirty=true;
		}
	}
	return was_dirty;
}	
void CacheManager::set_logger(Logger *logger)
{
	logger_ = logger;
}
