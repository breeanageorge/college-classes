// kernel_data.h, 159

#ifndef _KERNEL_DATA_H_ 
#define _KERNEL_DATA_H_  

#include "kernel_types.h" 

extern int run_pid; 
extern pid_q_t avail_pid_q, ready_pid_q; 
extern pcb_t pcb[PROC_NUM]; 
extern char proc_stack[PROC_NUM][PROC_STACK_SIZE]; 
extern int current_time;
extern semaphore_t video_sem;
extern term_t term[2];
extern func_p_t signal_table[PROC_NUM][SIG_NUM];

#endif  
