// kernel_constants.h, 159

#ifndef _KERNEL_CONSTANTS_H_
#define _KERNEL_CONSTANTS_H_

#define BUFF_SIZE 101
#define DSP_READY IIR_TXRDY
#define KB_READY IIR_RXRDY
#define LOOP 166666//6
#define PAGE_BASE 0xe00000
#define PAGE_NUM 20
#define PAGE_SIZE 4096
#define PROC_NUM 20 
#define PROC_STACK_SIZE 4096 
#define Q_SIZE 20 
#define SIGCHILD 17
#define SIGINT 2
#define SIG_NUM 32
#define STDOUT 1
#define SYSCALL 128
#define SYS_EXEC 11
#define SYS_EXIT 1
#define SYS_FORK 2
#define SYS_GETPID 20
#define SYS_GETPPID 64
#define SYS_READ 3
#define SYS_SEMPOST 301
#define SYS_SEMWAIT 300
#define SYS_SIGNAL 48
#define SYS_SLEEP 162
#define SYS_WAITCHILD 7
#define SYS_WRITE 4
#define TERM1 35
#define TERM2 36
#define TIMER 32  
#define TIME_LIMIT 200 
#define VM_END 0x5fffffff
#define VM_START 0x20000000
#define VM_TF 0x5fffffc8

#endif
