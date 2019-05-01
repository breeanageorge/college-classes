#include <stdint.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>


void rumba(unsigned char out[32], unsigned char in[], int n);

/* 
int main() {
    
    int num = 500;
    unsigned char output[32];
    unsigned char input[num];
	for(int i=0;i<num;i++){
		input[i] = i%256;
	}
     
    rumba(output, input, num);
    
	for(int j=0;j<32;j++){
		printf("%02X  ", output[j]);
	}
	
    return 0;
}*/

int main() {
    unsigned char in[4096];
    unsigned char check[32];
    unsigned char result1[32];
    unsigned char result2[32];
    int score = 0;
    
    for (int i=0; i<4096; i++)
        in[i] = (unsigned char)(i*i+i+13);

    rumba_me(check, in, 4096);
    
    rumba_me(result1, in, 0);
    rumba(result2, in, 0);
    if (memcmp(result1,result2,32)==0) score += 25;

    rumba_me(result1, in, 1);
    rumba(result2, in, 1);
    if (memcmp(result1,result2,32)==0) score += 25;

    rumba_me(result1, in, 256);
    rumba(result2, in, 256);
    if (memcmp(result1,result2,32)==0) score += 25;

    rumba_me(result1, in, 4000);
    rumba(result2, in, 4000);
    if (memcmp(result1,result2,32)==0) score += 25;

    rumba_me(result1, in, 4096);
    if (memcmp(result1,check,32)!=0) printf("Data altered!\n");
    
    printf("%d.%d\n",score/10,score%10);
}