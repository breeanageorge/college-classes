// kernel.c, 159
// OS bootstrap and kernel code for OS phase 5
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
term_t term[2];

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

   MyBzero((char *)&term[0], sizeof(term_t));
   MyBzero((char *)&term[1], sizeof(term_t));
   term[0].port = 0x2f8;
   term[1].port = 0x3e8;
   term[0].status = 0x2f8 + IIR;
   term[1].status = 0x3e8 + IIR;
   
}

void InitKernelControl(void) { 
   IDT_p = get_idt_base(); 
   cons_printf("IDT located at DRAM addr %x (%d).\n", IDT_p, IDT_p);
   
   fill_gate(&IDT_p[TIMER], (int)TimerEntry, get_cs(), ACC_INTR_GATE, 0);
   fill_gate(&IDT_p[SYSCALL], (int)SyscallEntry, get_cs(), ACC_INTR_GATE, 0);
   fill_gate(&IDT_p[TERM1], (int)Term1Entry , get_cs(), ACC_INTR_GATE, 0);
   fill_gate(&IDT_p[TERM2], (int)Term2Entry , get_cs(), ACC_INTR_GATE, 0);
   outportb(0x21, ~0x19); 
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

void InitTerm(void){
   int i, j;

   for(j = 0; j < 2; j++){
      outportb(term[j].port + CFCR, CFCR_DLAB);
      outportb(term[j].port + BAUDLO, LOBYTE(115200/9600));
      outportb(term[j].port + BAUDHI, HIBYTE(115200/9600));
      outportb(term[j].port + CFCR, CFCR_PEVEN | CFCR_PENAB | CFCR_7BITS);
      
      outportb(term[j].port + IER, 0);
      outportb(term[j].port + MCR, MCR_DTR | MCR_RTS | MCR_IENABLE);
      outportb(term[j].port + IER, IER_ERXRDY | IER_ETXRDY);

      for(i = 0; i < LOOP; i++) asm("inb $0x80");

      inportb(term[j].port);
   }

}

int main(void) { 
   InitKernelData(); 
   InitKernelControl(); 
   InitTerm();

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
      case TERM1:
         TermService(0);
         outportb(0x20, 0x63);
         break;
      case TERM2:
         TermService(1);
         outportb(0x20, 0x64);
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
