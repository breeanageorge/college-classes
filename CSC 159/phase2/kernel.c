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

void InitKernelData(void) {        // init kernel data
   int i;
   current_time = 0;
   run_pid = -1; //initialize run_pid (to negative 1)
   //clear two PID queues
   MyBzero((char *)&ready_pid_q, Q_SIZE);
   MyBzero((char *)&avail_pid_q, Q_SIZE);
   //enqueue all PID numbers into the available PID queue
   for(i = 0; i < Q_SIZE; i++){
      EnQ(i, &avail_pid_q);
   }
}

void InitKernelControl(void) {     // init kernel control
   IDT_p = get_idt_base(); //locate where IDT is
   cons_printf("IDT located at DRAM addr %x (%d).\n", IDT_p, IDT_p);//show its location on target PC
   
   //call fille_gate: fill out entry TIMER with TimerEntry
   fill_gate(&IDT_p[TIMER], (int)TimerEntry, get_cs(), ACC_INTR_GATE, 0);
   fill_gate(&IDT_p[SYSCALL], (int)SyscallEntry, get_cs(), ACC_INTR_GATE, 0);
   outportb(0x21, ~1); //send PIC a mask value
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

int main(void) {  // OS bootstraps
   InitKernelData(); //initialize kernel data
   InitKernelControl(); //initialize kernel control

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
