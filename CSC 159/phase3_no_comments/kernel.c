// kernel.c, 159
// OS bootstrap and kernel code for OS phase 3
//
// Team Name: LlamaOS (Members: Breeana George, Nicholas Hedge, Jonathan Sou) 

#include "spede.h"         
#include "kernel_types.h"  
#include "entry.h"        
#include "tools.h"         
#include "proc.h"          
#include "services.h"     

struct i386_gate *IDT_p;

int current_time;
int run_pid;                       
pid_q_t ready_pid_q, avail_pid_q;  
pcb_t pcb[PROC_NUM];              
char proc_stack[PROC_NUM][PROC_STACK_SIZE];
semaphore_t video_sem;

void InitKernelData(void) {        
   int i;
   current_time = 0;
   run_pid = -1; 
   
   video_sem.val = 1;
   video_sem.wait_q.size = 0;

   MyBzero((char *)&ready_pid_q, Q_SIZE);
   MyBzero((char *)&avail_pid_q, Q_SIZE);

   for(i = 0; i < Q_SIZE; i++){
      EnQ(i, &avail_pid_q);
   }
}

void InitKernelControl(void) {    
   IDT_p = get_idt_base();
   cons_printf("IDT located at DRAM addr %x (%d).\n", IDT_p, IDT_p);

   fill_gate(&IDT_p[TIMER], (int)TimerEntry, get_cs(), ACC_INTR_GATE, 0);
   fill_gate(&IDT_p[SYSCALL], (int)SyscallEntry, get_cs(), ACC_INTR_GATE, 0);
   outportb(0x21, ~1); 
}

void ProcScheduler(void) {         
   if (run_pid > 0){
      return;
   }

   if(ready_pid_q.size==0){
      run_pid = 0;
   }else{
      run_pid = DeQ(&ready_pid_q);
   }

   pcb[run_pid].totaltime += pcb[run_pid].runtime;
   pcb[run_pid].runtime = 0;
}

int main(void) {  
   InitKernelData(); 
   InitKernelControl();

   NewProcService(IdleProc); 
   ProcScheduler(); 
   ProcLoader(pcb[run_pid].trapframe_p); 
   return 0;
}

void Kernel(trapframe_t *trapframe_p) {
   char key;
   
   pcb[run_pid].trapframe_p = trapframe_p;
   
   switch(trapframe_p->intr_num){
      case TIMER:
         TimerService();
         break;
      case SYSCALL:
         SyscallService(trapframe_p);
         break;
   }
   
   if(cons_kbhit()){
      key = cons_getchar();
   
      if(key == 'n'){
         NewProcService(UserProc); 
      }
      if(key == 'b'){
         breakpoint(); 
      }
   }
   ProcScheduler(); 
   ProcLoader(pcb[run_pid].trapframe_p); 
}
