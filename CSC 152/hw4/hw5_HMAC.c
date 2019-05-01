#include <stdint.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>


void rumba(unsigned char out[32], unsigned char in[], int n);
 
int main() {
    unsigned char message[11]= "hello world";
	unsigned char o_key_pad[64];
	unsigned char i_key_pad[64];
    int num = 16;
    unsigned char output[32];
    unsigned char key[num];
	for(int i=0;i<num;i++){
		key[i] = i%256;
	}
     
	memcpy(o_key_pad, 0x5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c5c, 64);
	 
    rumba(output, key, num);
    
	for(int j=0;j<32;j++){
		printf("%02X  ", output[j]);
	}
	
    return 0;
}

