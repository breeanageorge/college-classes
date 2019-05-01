/*
Breeana George
11/14/2017

This code implements the exponentiation algorithm pow that was learned in class.
*/

#include <stdio.h>
#include <stdint.h>

uint64_t pow(uint64_t base, uint64_t exponent, uint64_t modulus){
	uint64_t result = 1;
	int x = 0;
	for(int i=63; i>=0; i--){
		//from HW 5 hints
		x = ((exponent>>i)&1);
		//squares the result
		result = (result*result);
		//mod to prevent overflow
		result = result%modulus;
		if(x != 0) {
			//multiply result by base
			result = (result*base);
			//mod to prevent overflow
			result = result%modulus;
		}
		result = result%modulus;
	}
	printf("Result = %i\n", result);
	return result;
}


int main(int argc, char *argv[]){
	//Should be 1023
	uint64_t base = 4294967295;
	uint64_t exponent = 4294967295;
	uint64_t modulus = 1024;
	pow(base, exponent, modulus);
	
	//Should be 4
	base = 10;
	exponent = 70;
	modulus = 12;
	pow(base, exponent, modulus);
	
	//Should be 5953
	base = 56184;
	exponent = 1144564;
	modulus = 15643;
	pow(base, exponent, modulus);
	
}