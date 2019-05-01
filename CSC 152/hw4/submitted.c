/*
Jonathan Sou
2155
*/


#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include "chacha20_simple.h"
#include "chacha20_simple.c"

void rumba(unsigned char out[32], unsigned char in[], int n);
void commpression(unsigned char output[64], unsigned char input[192]);


void rumba(unsigned char out[32], unsigned char in[], int n){

	int chunkSz= (n/128)+1;
	int remainingSz = n%128;
	unsigned char chain[64];
	unsigned char buf[128];
	unsigned char compOut[64];
	unsigned char inCat[192];
	bool firstPad = true;

	//allocated chain with 64 zeros
	for (int k = 0; k < 64; k++){
			chain[k]=0;
	}
		
	//allocate buffer with in blocks of 128 for the loop	
	for(int i=0; i<chunkSz; i++){
		if (i<chunkSz-1){
			memcpy(buf,&in[i*128],128);
		}else{
			memcpy(buf,&in[(i*128)], remainingSz);
			if (remainingSz<128){
				//pad if necessary
				for(int l=0; l<(128-remainingSz); l++){
					if(firstPad==true){
						buf[remainingSz+l]=0x80;
						firstPad=false;
					}else{
						buf[remainingSz+l]=0x00;
					}	
				}
			}
		}

		//allocate a 192 array with a concat of chain and buf
		memcpy(&inCat[0],&chain,64);
		memcpy(&inCat[64],&buf, 128);
		//run through compression to get output
		commpression(compOut, inCat);
		memcpy(&chain[0], compOut, 64);
	}
		//store resulting values into out
		memcpy(out,chain,32);	
}




void commpression(unsigned char output[64], unsigned char input[192]){
	unsigned char m1[48];
	unsigned char m2[48];
	unsigned char m3[48];
	unsigned char m4[48];

	unsigned char concat1[64];
	unsigned char concat2[64];
	unsigned char concat3[64];
	unsigned char concat4[64];

	unsigned char chaOut1[64];
	unsigned char chaOut2[64];
	unsigned char chaOut3[64];
	unsigned char chaOut4[64];

	unsigned char xorCha[64];

	//split 192 byte input into 4 blocks each with 48 bytes
	memcpy(m1,&input[0],48);
	memcpy(m2,&input[48],48);
	memcpy(m3,&input[96],48);
	memcpy(m4,&input[144],48);

	//concatonate both the 16 and 48 bytes into another arr
	memcpy(&concat1[0],"firstRumba20bloc",16);
	memcpy(&concat2[0],"secondRumba20blo",16);
	memcpy(&concat3[0],"thirdRumba20bloc",16);
	memcpy(&concat4[0],"fourthRumba20blo",16);

	memcpy(&concat1[16],&m1,48);
	memcpy(&concat2[16],&m2,48);
	memcpy(&concat3[16],&m3,48);
	memcpy(&concat4[16],&m4,48);

	//Pass the 64 bytes into chacha cipher
	uint32_t tempOut[16];
	chacha20_ctx ctx;

	memcpy(ctx.schedule,concat1, 64);
	chacha20_block(&ctx,tempOut);
	memcpy(chaOut1,tempOut,64);

	memcpy(ctx.schedule,concat2, 64);
	chacha20_block(&ctx,tempOut);
	memcpy(chaOut2,tempOut,64);

	memcpy(ctx.schedule,concat3, 64);
	chacha20_block(&ctx,tempOut);
	memcpy(chaOut3,tempOut,64);

	memcpy(ctx.schedule,concat4, 64);
	chacha20_block(&ctx,tempOut);
	memcpy(chaOut4,tempOut,64);


	//XOR the chacha blocks
	for(int w = 0; w<4; w++){
		for (int p = 0; p < 64; p++){
			if(w==0){
				xorCha[p]=xorCha[p]^chaOut1[p];
			}else if(w==1){
				xorCha[p]=xorCha[p]^chaOut2[p];
			}else if(w==2){
				xorCha[p]=xorCha[p]^chaOut3[p];
			}else{
				xorCha[p]=xorCha[p]^chaOut4[p];	
			}
		}
	}

	//Copy the result to the output array
	memcpy(output,xorCha,64);

	//reset the XOR array by reallocating with zeroes
	for (int f = 0; f < 64; f++)
	{
		xorCha[f]=0x00;
	}
}