// syscalls.c

#include "kernel_constants.h" 

int sys_getpid(void) {
   int pid;

   asm("movl %1, %%eax;
        int $128;  
        movl %%ebx, %0" 
       : "=g" (pid)  
       : "g" (SYS_GETPID) 
       : "eax", "ebx" 
    );

   return pid;
}

void sys_write(int fileno, char *str, int len) {
   if(str == (char)0 || len<1){
      return;
   }

   asm("movl %0, %%eax; 
        movl %1, %%ebx; 
        movl %2, %%ecx; 
        movl %3, %%edx; 
        int $128" 
       :  
       : "g" (SYS_WRITE), "g" (fileno), "g" ((int)str), "g" (len) 
       : "eax", "ebx", "ecx", "edx"
       );
}

void sys_sleep(int centi_sec) { 
   asm("movl %0, %%eax;
        movl %1, %%ebx; 
        int $128"
       :
       : "g" (SYS_SLEEP), "g" (centi_sec)
       : "eax", "ebx"
       );
}

void sys_semwait(int sem_num){
   asm("movl %0, %%eax;
        movl %1, %%ebx;
        int $128"
       :
       : "g" (SYS_SEMWAIT), "g" (sem_num)
       : "eax", "ebx"
       );
}

void sys_sempost(int sem_num){
   asm("movl %0, %%eax;
        movl %1, %%ebx;
        int $128"
       :
       : "g" (SYS_SEMPOST), "g" (sem_num)
       : "eax", "ebx"
       );
}


void sys_read(int fileno, char *str, int len) {
   asm("movl %0, %%eax;
        movl %1, %%ebx;
        movl %2, %%ecx;
        movl %3, %%edx;
        int $128"
       :
       : "g" (SYS_READ), "g" (fileno), "g" ((int)str), "g" (len)
       : "eax", "ebx", "ecx", "edx"
       );
}

