/*
Jonathan Sou
CSC152
Code:2155
UPDATED FILE
Update: Changed the way the counter increments so that it will account for more than 256 bits.
*/

#include <stdio.h>
#include "wmmintrin.h"
#include <string.h>
#include <stdint.h>
#include <stdbool.h>
//----------------------------------given code -------------------
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


//-----------------------------------My code----------------------

//this calls given key schedule to generate round key from the user key
void aesrand_setup(__m128i round_keys[11], unsigned char user_key[16]){
	AES_128_Key_Expansion(user_key,round_keys);
}

//creates a stream cipher 
void aesrand(unsigned char buf[], int n, uint64_t iv, __m128i round_keys[11]){

	uint64_t ctr=0; // counter for the bottom half of the block register
	int currentRnd=0; // a counter to keep track of cycles
	unsigned char output[16]; //used to transfer values of each cycle to buffer

	unsigned char testArr[16]={(unsigned char)(iv>>56),(unsigned char)(iv>>48),(unsigned char)(iv>>40),(unsigned char)(iv>>32),
        (unsigned char)(iv>>24),(unsigned char)(iv>>16),(unsigned char)(iv>>8),(unsigned char)(iv),
		(unsigned char)(ctr>>56),(unsigned char)(ctr>>48),(unsigned char)(ctr>>40),(unsigned char)(ctr>>32),
        (unsigned char)(ctr>>24),(unsigned char)(ctr>>16),(unsigned char)(ctr>>8),(unsigned char)(ctr)};


	__m128i block;
	__m128i incr;

	for (int i = 0; i < n/16+1; i++){

		if(currentRnd>0){
			ctr=currentRnd;
			//This is where the counter incrementation happens.
        testArr[8]=(unsigned char)(ctr>>56);
        testArr[9]=(unsigned char)(ctr>>48);
        testArr[10]=(unsigned char)(ctr>>40);
        testArr[11]=(unsigned char)(ctr>>32);
        testArr[12]=(unsigned char)(ctr>>24);
        testArr[13]=(unsigned char)(ctr>>16);
        testArr[14]=(unsigned char)(ctr>>8);
        testArr[15]=(unsigned char)(ctr);
		}

		//load newly incremented counter variable 
		//incr=_mm_loadu_si128((__m128i *)&ctrAdd[0]);
		block =_mm_loadu_si128((__m128i *)&testArr[0]);

		//start AES 
		block = _mm_xor_si128(block,round_keys[0]);// XOR the block with the roundkey once
		block = _mm_aesenc_si128(block, round_keys[1]);// run AES 9 times
		block = _mm_aesenc_si128(block, round_keys[2]);
		block = _mm_aesenc_si128(block, round_keys[3]);
		block = _mm_aesenc_si128(block, round_keys[4]);
		block = _mm_aesenc_si128(block, round_keys[5]);
		block = _mm_aesenc_si128(block, round_keys[6]);
		block = _mm_aesenc_si128(block, round_keys[7]);
		block = _mm_aesenc_si128(block, round_keys[8]);
		block = _mm_aesenc_si128(block, round_keys[9]);
		block = _mm_aesenclast_si128(block, round_keys[10]);// for the last one run AESlast once
		_mm_storeu_si128((__m128i *)&output, block);

		//This for loop was used to assign all the streamcipher result of size n into my buffer
		for (int j = 0; j < 16; j++)
		{
			//store values into our final keystream array
            if((i*16+j)<n){
			     buf[i*16 +j] = output[j];
            }
		}
		//increment the round which will help to increment the counter
		currentRnd+=1;
	}
}
