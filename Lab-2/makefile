all: test
test: test.cpp cache_manager.o
	g++ test.cpp -g -o test
cache_manager.o: cache_manager.cpp cache.h configuration.o logger.o cache.o
	g++ -c cache_manager.cpp
logger.o: logger.cpp 
	g++ -c logger.cpp
cache.o: cache.cpp cache.h cache_config.o map_memory_location.o
	g++ -c cache.cpp
configuration.o: configuration.cpp cache_config.o utils.o
	g++ -c configuration.cpp
cache_config.o: cache_config.cpp
	g++ -c cache_config.cpp
utils.o: utils.cpp
	g++ -c utils.cpp
map_memory_location.o : map_memory_location.cpp
	g++ -c map_memory_location.cpp

