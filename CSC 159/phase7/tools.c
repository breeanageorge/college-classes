// tools.c, 159

#include "spede.h"
#include "kernel_types.h"
#include "kernel_data.h"

void MyBzero(char *p, int size) {
   int i;
   for(i = 0; i < size; i++){   
      *p = (char)0;
      p++;
   }
}

int DeQ(pid_q_t *p) {
   int i,  element = -1;

   if(p->size == 0){
      return -1;
   }

   element = p->q[0];
   p->size -= 1;
   for(i = 0; i < p->size; i++){
      p->q[i] = p->q[i + 1];
   }

   return element;
}

void EnQ(int element, pid_q_t *p) {
   if(p->size == Q_SIZE){  
      cons_printf("Kernel Panic: queue is full, cannot EnQ!\n");
      return; 
   }

   p->q[p->size] = element;
   p->size += 1;
}

void MyStrcpy(char *dst, char *src){
   while(*src){
      *dst++ = *src++;
   }
   *dst = (char)0;
}

void MyShiftBuffer(char *buff, int size){
   int i;

   for(i = 0; i < size-1  && buff[i] != (char)0; i++){
      buff[i] = buff[i+1];
   }
}

void MyAppendToString(char *str, char c){ 
   int i;

   for(i = 0; i < BUFF_SIZE -1 ; i++){
      if(str[i] == (char)0){
         break;
      }
   }

   str[i] = c;
   str[i + 1] = (char)0;
}

int MyStrcmp(char *s1, char *s2){
   int i;

   for(i = 0; s1[i] == s2[i] ; i++){
      if(s1[i] == (char)0 || s2[i] == (char)0){
         return 1;
      }
   }
   
   return 0; 
}

void MyMemcpy(char *dst, char *src, int bytes){
   int i;

   for(i = 0; i < bytes; i++){
      dst[i] = src[i];
   }
}
