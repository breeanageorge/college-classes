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

