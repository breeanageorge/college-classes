// services.c, 159

#include "spede.h"
#include "kernel_types.h"
#include "kernel_data.h" 
#include "services.h"
#include "tools.h"       
#include "proc.h"

//int tick_count = 0;

// to create process, alloc PID, PCB, and process stack
// build trapframe, initialize PCB, record PID to ready_pid_q (unless 0)
void NewProcService(func_p_t proc_p) {  // arg: where process code starts
   int pid;

   if(avail_pid_q.size == 0){ //if the size of avail_pid_q is 0 (may occur as too many proc got created)
      cons_printf("Kernel Panic: no more process!\n");
      return;                   // alternative: breakpoint()
   }

   //get a 'pid' from avail_pid_q
   pid = DeQ(&avail_pid_q);

   //use tool MyBzero() to clear PCB and proc stack
   MyBzero((char *)&pcb[pid], sizeof(pcb_t));
   MyBzero((char *)&proc_stack[pid][0], PROC_STACK_SIZE); // char proc_stack[PROC_NUM][...]

   //set its process state to READY
   pcb[pid].state = READY;

   //queue pid to be ready_pid_q unless it's 0 (IdleProc)
   if(pid != 0){
      EnQ(pid, &ready_pid_q);
   }

   pcb[pid].trapframe_p =
      (trapframe_t *)&proc_stack[pid][PROC_STACK_SIZE-sizeof(trapframe_t)];

   pcb[pid].trapframe_p->efl = EF_DEFAULT_VALUE | EF_INTR;
   pcb[pid].trapframe_p->eip = (int)proc_p;
   pcb[pid].trapframe_p->cs = get_cs();
}

// count runtime of process and preempt it when reaching time limit
void TimerService(void) {
   int i;
   
   current_time++;
   //tick_count++;
   
   //loop thru PCB array
   //for each SLEEP state processes at or past wake time:
   //enqueue PID to Ready PID queue and update state
   for(i = 0; i < PROC_NUM; i++){
      if(pcb[i].state == SLEEP && pcb[i].wake_time <= current_time){
         pcb[i].state = READY;
         EnQ(i, &ready_pid_q);
      }
   }   
   
   outportb(0x20, 0x60); // PIC control, dismiss code

   //(every .75 second display a dot symbol '.')
   //just in case: asm("inb $0x80");
   //if(tick_count == 75){
      //tick_count = 0;
      //cons_printf(". ");
   //}

   //if the run_pid is 0, simply return (IdleProc is exempted)
   if(run_pid == 0){
      return;
   }

   //upcount the runtime of the running process
   pcb[run_pid].runtime += 1; 
   //if it reaches the time limit
   if(pcb[run_pid].runtime == TIME_LIMIT){
      //change its state to READY
      pcb[run_pid].state = READY;
      //queue it to ready_pid_q
      EnQ(run_pid, &ready_pid_q);
      //reset run_pid (to -1)
      run_pid = -1;
   }
}

void SyscallService(trapframe_t *p) {
   //switch on p->eax to call one the 3 services below
   switch (p->eax){
      case SYS_WRITE:
         WriteService((int)p->ebx, (char *)p->ecx, (int)p->edx);
         break;
      case SYS_GETPID:
         GetpidService(&p->ebx);
         break;
      case SYS_SLEEP:
         SleepService((int)p->ebx);
         break;
      case SYS_SEMWAIT:
         SemwaitService((int)p->ebx);
         break;
      case SYS_SEMPOST:
         SempostService((int)p->ebx);
         break;
   }
}

void GetpidService(int *p) {
   //fill out what p point to with the currently-running PID
   *p = run_pid;
}

void SleepService(int centi_sec) {
   //set wake time of running process by current OS time + centi_sec
   pcb[run_pid].wake_time = current_time + centi_sec;
   //printf("PID: %d Evanesence: %d\n",run_pid,pcb[run_pid].wake_time);
   //alter process state
   pcb[run_pid].state = SLEEP;
   
   //reset the running PID
   run_pid = -1;
}

void WriteService(int fileno, char *str, int len) {
   int i;
   static unsigned short *vga_p = (unsigned short *)0xb8000; // top-left
   if(fileno == STDOUT) {
      //while(*str != (char)0){
      for(i = 0; i<len; i++){
         *vga_p = *str + 0xf00;
         vga_p++; //increment vga_p
         if(vga_p >= (unsigned short *)0xb8000 + 25*80) { // bottom-right
            //erase the whole screen
            for(vga_p = (unsigned short *)0xb8000 + 25*80; vga_p >= (unsigned short *)0xb8000; vga_p--){
               *vga_p = ' ';
            }
         }
         str++;
      }
   }
}

void SemwaitService(int sem_num){
   if(sem_num == STDOUT){
      if(video_sem.val > 0){
         video_sem.val -= 1;
      }else{
         //block running process
         pcb[run_pid].state = WAIT;
         EnQ(run_pid, &video_sem.wait_q);
         run_pid = -1;
      }
   }else{
      cons_printf("Kernel Panic: non-such semaphore number!\n");
   }
}

void SempostService(int sem_num){
   int pid;
   
   if(sem_num == STDOUT){
      if(video_sem.wait_q.size == 0){
         video_sem.val += 1;
      }else{
         //liberate a waiting process
         pid = DeQ(&video_sem.wait_q);
         pcb[pid].state = READY;
         EnQ(pid, &ready_pid_q);
      }
   }else{
      cons_printf("Kernel Panic: non-such semaphore number!\n");
   }
}
