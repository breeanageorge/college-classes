/*
Breeana George
10/16/2017

This code runs AES with SSE2 instructions.
gcc -maes -W -std=c99 -lcrypto hw3_aesrand.c test.c
*/

#include <stdio.h>
#include "wmmintrin.h"
#include <string.h>
#include <stdint.h>

/*
EXPAND_ASSIST and AES_128_Expansion from Professor Krovetz 
at http://krovetz.net/x52/aesenc_openssl.html
*/

#define EXPAND_ASSIST(v1,v2,v3,v4,shuff_const,aes_const)                    \
    v2 = _mm_aeskeygenassist_si128(v4,aes_const);                           \
    v3 = _mm_castps_si128(_mm_shuffle_ps(_mm_castsi128_ps(v3),              \
                                         _mm_castsi128_ps(v1), 16));        \
    v1 = _mm_xor_si128(v1,v3);                                              \
    v3 = _mm_castps_si128(_mm_shuffle_ps(_mm_castsi128_ps(v3),              \
                                         _mm_castsi128_ps(v1), 140));       \
    v1 = _mm_xor_si128(v1,v3);                                              \
    v2 = _mm_shuffle_epi32(v2,shuff_const);                                 \
    v1 = _mm_xor_si128(v1,v2)

static void AES_128_Key_Expansion(const unsigned char userkey[16], __m128i key[11])
{
    __m128i x0,x1,x2;
    key[0] = x0 = _mm_loadu_si128((__m128i*)userkey);
    x2 = _mm_setzero_si128();
    EXPAND_ASSIST(x0,x1,x2,x0,255,1);   key[1]  = x0;
    EXPAND_ASSIST(x0,x1,x2,x0,255,2);   key[2]  = x0;
    EXPAND_ASSIST(x0,x1,x2,x0,255,4);   key[3]  = x0;
    EXPAND_ASSIST(x0,x1,x2,x0,255,8);   key[4]  = x0;
    EXPAND_ASSIST(x0,x1,x2,x0,255,16);  key[5]  = x0;
    EXPAND_ASSIST(x0,x1,x2,x0,255,32);  key[6]  = x0;
    EXPAND_ASSIST(x0,x1,x2,x0,255,64);  key[7]  = x0;
    EXPAND_ASSIST(x0,x1,x2,x0,255,128); key[8]  = x0;
    EXPAND_ASSIST(x0,x1,x2,x0,255,27);  key[9]  = x0;
    EXPAND_ASSIST(x0,x1,x2,x0,255,54);  key[10] = x0;
}

void aesrand_setup(__m128i round_keys[11], unsigned char user_key[16]){
	AES_128_Key_Expansion(user_key, round_keys);
}

void aesrand(unsigned char buf[], int n, uint64_t iv, __m128i round_keys[11]){
	
	uint64_t counter=0;
	int num = n/16+1;
	unsigned char out[16];
	__m128i block;
	
	/*
	Jonathan Sou helped me with this code, 
	I was previously trying to put the full blocks through 
	and it wasn't working properly because of little endian issues
	*/
	unsigned char arr[16]={(unsigned char)(iv>>56),(unsigned char)(iv>>48),(unsigned char)(iv>>40),(unsigned char)(iv>>32),(unsigned char)(iv>>24),(unsigned char)(iv>>16),(unsigned char)(iv>>8),(unsigned char)(iv)};
	
	for(int i=0; i <num; i++){
		
		//Jonathan also helped me with this section
		counter=i;
		//counter increments
		arr[8]=(unsigned char)(counter>>56);
		arr[9]=(unsigned char)(counter>>48);
		arr[10]=(unsigned char)(counter>>40);
		arr[11]=(unsigned char)(counter>>32);
		arr[12]=(unsigned char)(counter>>24);
		arr[13]=(unsigned char)(counter>>16);
		arr[14]=(unsigned char)(counter>>8);
		arr[15]=(unsigned char)(counter);


		//load newly incremented counter variable 
		block =_mm_loadu_si128((__m128i *)&arr);
		
		//XOR block and first round key
		block = _mm_xor_si128(block,round_keys[0]);
		
		//Run AES command nine times
		for(int j=1;j<10;j++){
			block = _mm_aesenc_si128(block, round_keys[j]);
		}	
		
		//Use AES ENC last for last run
		block = _mm_aesenclast_si128(block, round_keys[10]);
		
		//Store result into an array
		_mm_storeu_si128((__m128i *)&out, block);

		//Store result in to buffer
		for (int k = 0; k < 16; k++){
			buf[i*16+k] = out[k];
		}		
	}
}
