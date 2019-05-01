#include <stdint.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <openssl/evp.h>
#include <wmmintrin.h>
 
#define SZ 5000015
 
void aesrand_setup(__m128i round_keys[11], unsigned char user_key[16]);
void aesrand(unsigned char buf[], int n, uint64_t iv, __m128i round_keys[11]);
 
int main() {
    EVP_CIPHER_CTX ctx;
    int outl;
    unsigned char ctr[16] = {1,0,};
    unsigned char key[16] = {0x01,0x12,0x23,0x34,0x45,0x56,0x67,0x78,
                             0x89,0x9A,0xAB,0xBC,0XCD,0xDE,0xEF,0xF0};
    unsigned char *buf = malloc(SZ);
    unsigned char *buf2 = malloc(SZ);
    memset(buf,0,SZ);
    memset(buf2,0,SZ);
     
    EVP_CIPHER_CTX_init(&ctx);
    EVP_CipherInit_ex(&ctx, EVP_aes_128_ctr(), NULL, key, ctr, 1);  // 0=dec
    EVP_CipherUpdate(&ctx, buf, &outl, buf, SZ);
    EVP_CipherFinal_ex(&ctx, buf+outl, &outl);
    EVP_CIPHER_CTX_cleanup(&ctx);
     
    __m128i round_keys[11];
    aesrand_setup(round_keys, key);
    aesrand(buf2, SZ, 0x0100000000000000ull, round_keys);
     
    if (memcmp(buf,buf2,SZ)==0)
        printf("pass\n");
    else
        printf("fail\n");
     
    return 0;
}