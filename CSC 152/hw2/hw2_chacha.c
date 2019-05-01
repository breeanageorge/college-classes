/*
Breeana George
10/3/2017

This code manipulates that chacha20 framework to run the quarterrounds in parallel
instead of in sequence, using Intel SSE2.
*/

#include <string.h>
#include "chacha20_simple.h"
#include <emmintrin.h>
#include <stdio.h>

void chacha20_block_sse(chacha20_ctx *ctx, uint32_t output[16])
{
  uint32_t *const nonce = ctx->schedule+12; //12 is where the 128 bit counter is
  int i = 10;

  memcpy(output, ctx->schedule, sizeof(ctx->schedule));

  while (i--) 
  {
	//load 
	__m128i a = _mm_loadu_si128((__m128i *)&output[0]);
	__m128i b = _mm_loadu_si128((__m128i *)&output[4]);
	__m128i c = _mm_loadu_si128((__m128i *)&output[8]);
	__m128i d = _mm_loadu_si128((__m128i *)&output[12]);
		
	//QUARTERROUND(output, 0, 4, 8, 12)	
	a = _mm_add_epi32(a,b);
	d = _mm_xor_si128(d,a);
	d = _mm_xor_si128(_mm_slli_epi32 (d, 16) , _mm_srli_epi32 (d, 16));
	
	//QUARTERROUND(output, 1, 5, 9, 13)
	c = _mm_add_epi32(c,d);
	b = _mm_xor_si128(b,c);
	b = _mm_xor_si128(_mm_slli_epi32 (b, 12) , _mm_srli_epi32 (b, 20));
	
	//QUARTERROUND(output, 2, 6, 10, 14)
	a = _mm_add_epi32(a,b);
	d = _mm_xor_si128(d,a);
	d = _mm_xor_si128(_mm_slli_epi32 (d, 8) , _mm_srli_epi32 (d, 24));
	
	//QUARTERROUND(output, 3, 7, 11, 15)
	c = _mm_add_epi32(c,d);
	b = _mm_xor_si128(b,c);
	b = _mm_xor_si128(_mm_slli_epi32 (b, 7) , _mm_srli_epi32 (b, 25));
	
	//Shuffle
	b = _mm_shuffle_epi32(b, 0b00111001);
	c = _mm_shuffle_epi32(c, 0b01001110);
	d = _mm_shuffle_epi32(d, 0b10010011);
	
	//QUARTERROUND(output, 0, 5, 10, 15)
	a = _mm_add_epi32(a,b);
	d = _mm_xor_si128(d,a);
	d = _mm_xor_si128(_mm_slli_epi32 (d, 16) , _mm_srli_epi32 (d, 16));
	
	//QUARTERROUND(output, 1, 6, 11, 12)
	c = _mm_add_epi32(c,d);
	b = _mm_xor_si128(b,c);
	b = _mm_xor_si128(_mm_slli_epi32 (b, 12) , _mm_srli_epi32 (b, 20));
	
	//QUARTERROUND(output, 2, 7, 8, 13)
	a = _mm_add_epi32(a,b);
	d = _mm_xor_si128(d,a);
	d = _mm_xor_si128(_mm_slli_epi32 (d, 8) , _mm_srli_epi32 (d, 24));
		
	//QUARTERROUND(output, 3, 4, 9, 14)	
	c = _mm_add_epi32(c,d);
	b = _mm_xor_si128(b,c);
	b = _mm_xor_si128(_mm_slli_epi32 (b, 7) , _mm_srli_epi32 (b, 25));
	
	//Inverse Shuffle
	b = _mm_shuffle_epi32(b, 0b10010011);
	c = _mm_shuffle_epi32(c, 0b01001110);
	d = _mm_shuffle_epi32(d, 0b00111001);
	
	//Store
	_mm_storeu_si128((__m128i *)&output[0], a);
	_mm_storeu_si128((__m128i *)&output[4], b);
	_mm_storeu_si128((__m128i *)&output[8], c);
	_mm_storeu_si128((__m128i *)&output[12], d);
	
  }
  for (i = 0; i < 16; ++i)
  {
    uint32_t result = output[i] + ctx->schedule[i];
    FROMLE((uint8_t *)(output+i), result);
  }

  /*
  Official specs calls for performing a 64 bit increment here, and limit usage to 2^64 blocks.
  However, recommendations for CTR mode in various papers recommend including the nonce component for a 128 bit increment.
  This implementation will remain compatible with the official up to 2^64 blocks, and past that point, the official is not intended to be used.
  This implementation with this change also allows this algorithm to become compatible for a Fortuna-like construct.
  */
  if (!++nonce[0] && !++nonce[1] && !++nonce[2]) { ++nonce[3]; }
}

