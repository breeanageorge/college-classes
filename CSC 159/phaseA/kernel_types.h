// kernel_types.h, 159

#ifndef _KERNEL_TYPES_H_
#define _KERNEL_TYPES_H_

#include "kernel_constants.h"

typedef void (*func_p_t)(); 

typedef enum {AVAIL, READY, RUN, SLEEP, WAIT, WAITCHILD, ZOMBIE} state_t;

typedef struct {
   unsigned int edi;
   unsigned int esi;
   unsigned int ebp;
   unsigned int esp;
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
   int ppid;
   int page[5];
   int TT;
} pcb_t;                     

typedef struct { 
   int q[Q_SIZE]; 
   int size; 
} pid_q_t;

typedef struct { 
   int val; 
   pid_q_t wait_q;
} semaphore_t;

typedef struct {
   char dsp[BUFF_SIZE];
   pid_q_t dsp_wait_q;
   int port;
   char kb[BUFF_SIZE];
   pid_q_t kb_wait_q;
   int status;
} term_t;

#endif
