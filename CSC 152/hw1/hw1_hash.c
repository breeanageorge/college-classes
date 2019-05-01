#include <stdio.h>

unsigned char xor_buf(unsigned char buf[], int nbytes) {
    unsigned char acc = 0;
    int i;
    for (i=0; i<nbytes; i++){
        acc ^= buf[i];
    }
    return acc;
}

int main(int argc, char *argv[]) {
    unsigned char buf[128];
    unsigned char acc = 0;
    int bytes_read  = fread(buf,1,sizeof(buf),stdin);
    while (bytes_read > 0) {
        acc ^= xor_buf(buf,bytes_read);
        bytes_read  = fread(buf,1,sizeof(buf),stdin);
    }
    printf("%X\n",acc);
}
