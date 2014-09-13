#include <iostream>
#include <stdlib.h>
#include <vector>

#define UPPER_LIMIT 10

/** Multiplies two randomly generated n*n matrices, and stores the product
 *  Takes size as input from stdin **/
using namespace std;

int main() {
	// int sizes[] = {8,16,32,64,128,256,512,1024};
	int n;
	cin >> n;
	//   for (int i = 0; i < 8; ++i) {
	//	int n = sizes[i];
		cout << "Size of the matrix " << n << endl;

		int **A = (int **) malloc(n * sizeof(int *));
		int **B = (int **) malloc(n * sizeof(int *));
		int **C = (int **) malloc(n * sizeof(int *));
		for (int i = 0; i < n; ++i) {
			A[i] = (int *) malloc(n * sizeof(int));
			B[i] = (int *) malloc(n * sizeof(int));
			C[i] = (int *) malloc(n * sizeof(int));
			for (int j = 0; j < n; ++j) {
				A[i][j] = rand() % UPPER_LIMIT;
				B[i][j] = rand() % UPPER_LIMIT;
				C[i][j] = 0;
			}
		}
		
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < n; ++j) {
				int sum = 0;
				for (int k = 0; k < n; ++k) {
					sum += A[i][k] * B[k][j];
				}
				C[i][j] = sum;
			}
		}
//	}	
}
