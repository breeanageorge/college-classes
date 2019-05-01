/*
Breeana George
10/29/2017

This code implements the Rumba cryptographic hash.
*/

#include <string.h>
#include "chacha20_simple.h"
#include "chacha20_simple.c"
#include <emmintrin.h>
#include <stdio.h>

void chacha20_block(chacha20_ctx *ctx, uint32_t output[16]);

void compression(unsigned char output[64], unsigned char input[192]){
	uint32_t out[16];
	chacha20_ctx ctx;
	unsigned char buf1[64];
	unsigned char buf2[64];
	unsigned char buf3[64];
	unsigned char buf4[64];
	unsigned char a[64];
	unsigned char b[64];
	unsigned char c[64];
	unsigned char d[64];
	
	//round 1
	unsigned char c1[16] = "firstRumba20bloc";
	memcpy(buf1,c1,16);
	memcpy(&buf1[16], input, 48);
	memcpy(ctx.schedule, buf1, 64);
	chacha20_block(&ctx, out);
	memcpy(a, out, 64);
	
	//round 2
	unsigned char c2[16] = "secondRumba20blo";
	memcpy(buf2,c2,16);
	memcpy(&buf2[16], &input[48], 48);
	memcpy(ctx.schedule, buf2, 64);
	chacha20_block(&ctx, out);
	memcpy(b, out, 64);
	
	//round 3
	unsigned char c3[16] = "thirdRumba20bloc";
	memcpy(buf3,c3,16);
	memcpy(&buf3[16], &input[96], 48);
	memcpy(ctx.schedule, buf3, 64);
	chacha20_block(&ctx, out);
	memcpy(c, out, 64);
	
	//round 4
	unsigned char c4[16] = "fourthRumba20blo";
	memcpy(buf4,c4,16);
	memcpy(&buf4[16], &input[144], 48);
	memcpy(ctx.schedule, buf4, 64);
	chacha20_block(&ctx, out);
	memcpy(d, out, 64);
	
	//XOR output of all chacha blocks
	for(int i=0; i<64; i++){	
		output[i] = a[i]^b[i]^c[i]^d[i];
		//printf("a:%02X b:%02X c:%02X d:%02X output:%02X \n", a[i], b[i], c[i], d[i], output[i]);
	}	
	
}

void rumba(unsigned char out[32], unsigned char in[], int n){
	int num = n/128+1;
	unsigned char buf[128];	
	unsigned char chain[64];
	unsigned char tempChain[192];
	
	for(int k=0; k<64; k++){
		chain[k] = 0;
	}
		
	for(int i=0; i<num; i++){
		//Places 128-byte chunks of in[] into a buffer, and adds padding
		if(i<num-1){
			memcpy(buf, &in[i*128], 128);
		}else{
			int x = n%128;
			memcpy(buf, &in[i*128], x);
			buf[x] = 128;
			for(int k=x+1;k<128;k++){
				buf[k] = 0;
			}	
		}
	
		//concat(chain, buf)
		memcpy(tempChain, &chain, 64);
		memcpy(&tempChain[64], &buf, 128);
		
		compression(chain, tempChain);
	}	
	
	memcpy(out, chain, 32);	
}
