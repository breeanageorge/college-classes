// kernel_data.h, 159
// kernel data are all declared in kernel.c during bootstrap
// kernel .c code reference them as 'extern'

#ifndef _KERNEL_DATA_H_             
#define _KERNEL_DATA_H_            

#include "kernel_types.h"          

extern int run_pid;                 
extern pid_q_t avail_pid_q, ready_pid_q;  
extern pcb_t pcb[PROC_NUM];        
extern char proc_stack[PROC_NUM][PROC_STACK_SIZE]; 
extern int current_time;
extern semaphore_t video_sem;

#endif                            
