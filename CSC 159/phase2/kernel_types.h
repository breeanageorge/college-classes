// kernel_types.h, 159

#ifndef _KERNEL_TYPES_H_
#define _KERNEL_TYPES_H_

#include "kernel_constants.h"

typedef void (*func_p_t)(); // void-return function pointer type

typedef enum {AVAIL, READY, RUN, SLEEP} state_t;

typedef struct {
   //unsigned int regs[8];
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
   state_t state;            // state of process
   int runtime;              // runtime of this run
   int wake_time;
   int totaltime;            // total runtime
   trapframe_t *trapframe_p; // points to saved trapframe
} pcb_t;                     

typedef struct {             // generic queue type
   int q[Q_SIZE];            // integers are queued in q[] array
   int size;                 // size is also where the tail is for new data
} pid_q_t;

#endif // _KERNEL_TYPES_H_
