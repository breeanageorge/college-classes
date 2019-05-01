// kernel_types.h, 159

#ifndef _KERNEL_TYPES_H_
#define _KERNEL_TYPES_H_

#include "kernel_constants.h"

typedef void (*func_p_t)();

typedef enum {AVAIL, READY, RUN, SLEEP, WAIT} state_t;

typedef struct {
   unsigned int regs[4];
   unsigned int ebx;
   unsigned int edx;
   unsigned int ecx;
   unsigned int eax;
   unsigned int intr_num;
   unsigned int eip;
   unsigned int cs;
   unsigned int efl;
} trapframe_t;

typedef struct {
   state_t state;            
   int runtime;          
   int wake_time;
   int totaltime;            
   trapframe_t *trapframe_p; 
} pcb_t;                     

typedef struct {         
   int q[Q_SIZE];
   int size;
} pid_q_t;

typedef struct{  
   int val;
   pid_q_t wait_q; 
} semaphore_t;

#endif
