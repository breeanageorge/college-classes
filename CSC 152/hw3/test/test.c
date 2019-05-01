#include <stdio.h>  
#include <string.h> 
#include <stdint.h> 

void main() {
    int x = 0b01100111;
    unsigned char a = (unsigned char)(x>>2);
    printf("%c", a);
    
}
