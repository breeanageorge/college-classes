// kernel.c, 159
// OS bootstrap and kernel code for OS phase 2
//
// Team Name: LlamaOS (Members: Breeana George, Nicholas Hedge, Jonathan Sou) 

#include "spede.h"         // given SPEDE stuff
#include "kernel_types.h"  // kernel data types
#include "entry.h"         // entries to kernel
#include "tools.h"         // small tool functions
#include "proc.h"          // process names such as IdleProc()
#include "services.h"      // service code

struct i386_gate *IDT_p;

// kernel data are all declared here:
int current_time;
int run_pid;                       // currently running PID; if -1, none selected
pid_q_t ready_pid_q, avail_pid_q;  // avail PID and those ready to run
pcb_t pcb[PROC_NUM];               // Process Control Blocks
char proc_stack[PROC_NUM][PROC_STACK_SIZE]; // process runtime stacks
semaphore_t video_sem;
term_t term[2];

void InitKernelData(void) {        // init kernel data
   int i;
   current_time = 0;
   run_pid = -1; //initialize run_pid (to negative 1)
   
   // initialize semaphore to 1 and wait_q size to 0
   video_sem.val = 1;
   video_sem.wait_q.size = 0;

   //clear two PID queues
   MyBzero((char *)&ready_pid_q, Q_SIZE);
   MyBzero((char *)&avail_pid_q, Q_SIZE);
   //enqueue all PID numbers into the available PID queue
   for(i = 0; i < Q_SIZE; i++){
      EnQ(i, &avail_pid_q);
   }

   MyBzero((char *)&term[0], sizeof(term_t));
   MyBzero((char *)&term[1], sizeof(term_t));
   term[0].port = 0x2f8;
   term[1].port = 0x3e8;
}

void InitKernelControl(void) {     // init kernel control
   IDT_p = get_idt_base(); //locate where IDT is
   cons_printf("IDT located at DRAM addr %x (%d).\n", IDT_p, IDT_p);//show its location on target PC
   
   //call fille_gate: fill out entry TIMER with TimerEntry
   fill_gate(&IDT_p[TIMER], (int)TimerEntry, get_cs(), ACC_INTR_GATE, 0);
   fill_gate(&IDT_p[SYSCALL], (int)SyscallEntry, get_cs(), ACC_INTR_GATE, 0);
   fill_gate(&IDT_p[TERM1], (int)Term1Entry , get_cs(), ACC_INTR_GATE, 0);
   fill_gate(&IDT_p[TERM2], (int)Term2Entry , get_cs(), ACC_INTR_GATE, 0);
   outportb(0x21, ~0x19); //send PIC a mask value
}

void ProcScheduler(void) {              // choose run_pid to load/run
   if (run_pid > 0){
      return; // no need if PID is a user proc
   }
   
   //if the ready_pid_q is empty: let run_pid be zero
   //else: get the 1st one in ready_pid_q to be run_pid
   if(ready_pid_q.size==0){
      run_pid = 0;
   }else{
      run_pid = DeQ(&ready_pid_q);
   }
   
   //accumulate its totaltime by adding its runtime
   pcb[run_pid].totaltime += pcb[run_pid].runtime;
   //and then reset its runtime to zero
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

int main(void) {  // OS bootstraps
   InitKernelData(); //initialize kernel data
   InitKernelControl(); //initialize kernel control
   InitTerm();

   NewProcService(IdleProc); //call NewProcService() with address of IdleProc to create it
   ProcScheduler(); //call ProcScheduler() to select a run_pid
   ProcLoader(pcb[run_pid].trapframe_p); //call ProcLoader() with address of the trapframe of the selected run_pid
   
   return 0; // compiler needs for syntax altho this statement is never exec
}

void Kernel(trapframe_t *trapframe_p) {
   char key;
   
   //save the trapframe_p to the PCB of run_pid
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
      key = cons_getchar(); //get the key
   
      if(key == 'n'){
         NewProcService(UserProc); //if it's 'n,' call NewProcService() to create a UserProc
      }
      if(key == 'b'){
         breakpoint(); //if it's 'b,' call breakpoint() to go to the GDB prompt
      }
   }
   ProcScheduler(); //call ProcScheduler() to select run_pid
   ProcLoader(pcb[run_pid].trapframe_p); //call ProcLoader() given the trapframe_p of the run_pid to load/run it
}
