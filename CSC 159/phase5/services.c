// services.c, 159

#include "spede.h"
#include "kernel_types.h"
#include "kernel_data.h" 
#include "services.h"
#include "tools.h"       
#include "proc.h"

void NewProcService(func_p_t proc_p) {  
   int pid;

   if(avail_pid_q.size == 0){ 
      cons_printf("Kernel Panic: no more process!\n");
      return; 
   }

   pid = DeQ(&avail_pid_q);

   MyBzero((char *)&pcb[pid], sizeof(pcb_t));
   MyBzero((char *)&proc_stack[pid][0], PROC_STACK_SIZE); 

   pcb[pid].state = READY;

   if(pid != 0){
      EnQ(pid, &ready_pid_q);
   }

   pcb[pid].trapframe_p =
      (trapframe_t *)&proc_stack[pid][PROC_STACK_SIZE-sizeof(trapframe_t)];

   pcb[pid].trapframe_p->efl = EF_DEFAULT_VALUE | EF_INTR;
   pcb[pid].trapframe_p->eip = (int)proc_p;
   pcb[pid].trapframe_p->cs = get_cs();
}

void TimerService(void) {
   int i;
   
   current_time++;

   for(i = 0; i < PROC_NUM; i++){
      if(pcb[i].state == SLEEP && pcb[i].wake_time <= current_time){
         pcb[i].state = READY;
         EnQ(i, &ready_pid_q);
      }
   }   
   
   outportb(0x20, 0x60); 

   if(run_pid == 0){
      return;
   }

   pcb[run_pid].runtime += 1; 
   if(pcb[run_pid].runtime == TIME_LIMIT){
      pcb[run_pid].state = READY;
      EnQ(run_pid, &ready_pid_q);
      run_pid = -1;
   }
}

void SyscallService(trapframe_t *p) {
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
      case SYS_READ:
         ReadService((int)p->ebx, (char *)p->ecx, (int)p->edx);
         break;
   }
}

void GetpidService(int *p) {
   *p = run_pid;
}

void SleepService(int centi_sec) {
   pcb[run_pid].wake_time = current_time + centi_sec;
   pcb[run_pid].state = SLEEP;
   run_pid = -1;
}

void WriteService(int fileno, char *str, int len) {
   int i, which;
   static unsigned short *vga_p = (unsigned short *)0xb8000; 
   switch (fileno){
      case STDOUT: 
         for(i = 0; i<len; i++){
            *vga_p = *str + 0xf00;
            vga_p++; 
            if(vga_p >= (unsigned short *)0xb8000 + 25*80) { 
               for(vga_p = (unsigned short *)0xb8000; 
                   vga_p < (unsigned short *)0xb8000 + 25*80; vga_p++){
                  *vga_p = ' ';
               }
               vga_p = (unsigned short *)0xb8000;
            }
            str++;
         }
         break;
      case TERM1:
      case TERM2:
         which = (fileno % 2)? 0 : 1;
         MyStrcpy((char *)&term[which].dsp[0], str);
         pcb[run_pid].state = WAIT;
         EnQ(run_pid, &term[which].dsp_wait_q);
         run_pid = -1;
         DspService(which);
         break;
   }
}

void SemwaitService(int sem_num){
   if(sem_num == STDOUT){
      if(video_sem.val > 0){
         video_sem.val -= 1;
      }else{
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
         pid = DeQ(&video_sem.wait_q);
         pcb[pid].state = READY;
         EnQ(pid, &ready_pid_q);
      }
   }else{
      cons_printf("Kernel Panic: non-such semaphore number!\n");
   }
} 

void TermService(int which){
   int status = inportb(term[which].status);
    
   if(status == DSP_READY){
      DspService(which);
   }else{
      KbService(which);
   }
   
}

void DspService(int which){
   int pid;

   if(term[which].dsp[0] == (char)0){
      return;
   }
     
   outportb(term[which].port, term[which].dsp[0]);
   
   MyShiftBuffer(term[which].dsp, BUFF_SIZE);
  
   if(term[which].dsp[0] == (char)0 && term[which].dsp_wait_q.size > 0){
      pid = DeQ(&term[which].dsp_wait_q);
      pcb[pid].state = READY;
      EnQ(pid, &ready_pid_q);
   }
}

void KbService(int which){
   char ch, *dst;
   int pid;

   ch = inportb(term[which].port);
   
   outportb(term[which].port, ch);
    
   if(ch != '\r'){
      MyAppendToString(term[which].kb, ch);
      return;
   }

   outportb(term[which].port, '\n');

   if(term[which].kb_wait_q.size > 0){
      pid = DeQ(&term[which].kb_wait_q);
      pcb[pid].state = READY;
      EnQ(pid, &ready_pid_q);
      
      dst = (char *)pcb[pid].trapframe_p->ecx;
      MyStrcpy(dst, term[which].kb);  
   }

   term[which].kb[0] = (char) 0;
}

void ReadService(int termNum, char *str, int len){
   int which = (termNum % 2)? 0 : 1;

   pcb[run_pid].state = WAIT;
   EnQ(run_pid, &term[which].kb_wait_q);
   run_pid = -1;
}
