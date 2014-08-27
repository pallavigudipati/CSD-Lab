#include <iostream>
#include <vector>
#include <string>
#include <sstream>

using namespace std;

class Utils {
  public:
	static vector<string> split(string str, char delim) {
		// cout << str << endl;
		vector<string> elements;
		stringstream string_stream(str);
		string item;
		while (getline(string_stream, item, delim)) {
			elements.push_back(item);
		}
		return elements;
	}
};
