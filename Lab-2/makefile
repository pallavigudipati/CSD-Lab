all: test
test: test.cpp cache_manager.o
	g++ test.cpp -o test
cache_manager.o: cache_manager.cpp cache.h configuration.o logger.o
	g++ -c cache_manager.cpp
logger.o: logger.cpp 
	g++ -c logger.cpp
cache.o: cache.cpp cache.h cache_config.o
	g++ -c cache.cpp
configuration.o: configuration.cpp cache_config.o utils.o
	g++ -c configuration.cpp
cache_config.o: cache_config.cpp
	g++ -c cache_config.cpp
utils.o: utils.cpp
	g++ -c utils.cpp

