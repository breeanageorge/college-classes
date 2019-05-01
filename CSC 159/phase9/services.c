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
      case SYS_FORK:
         ForkService(&p->ebx);
         break;
      case SYS_SIGNAL:
         SignalService((int)p->ebx, (func_p_t)p->ecx);
         break;
      case SYS_GETPPID:
         GetppidService(&p->ebx);
         break;
      case SYS_EXIT:
         ExitService((int)p->ebx);
         break;
      case SYS_WAITCHILD:
         WaitchildService((int *)p->ebx, &p->ecx);
         break;
      case SYS_EXEC:
         ExecService((func_p_t)p->ebx, (int)p->ecx);
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
   if(*str == (char)0){
      return;
   }
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
   
   if(ch != (char)3){
      outportb(term[which].port, ch);
   }

   if(ch != '\r' && ch != (char)3){
      MyAppendToString(term[which].kb, ch);
      return;
   } 
   
   if(term[which].kb_wait_q.size > 0){
      pid = DeQ(&term[which].kb_wait_q);
      pcb[pid].state = READY;
      EnQ(pid, &ready_pid_q);
         
      dst = (char *)pcb[pid].trapframe_p->ecx;
      MyStrcpy(dst, term[which].kb);

      if(ch == (char)3 && signal_table[pid][SIGINT]){
         WrapperService(pid, signal_table[pid][SIGINT]);
      }else{
         ch = (ch == '\r') ? '\n' : '^';
         outportb(term[which].port, ch);
      }
   }
   
   term[which].kb[0] = (char)0;
}

void ReadService(int termNum, char *str, int len){
   int which = (termNum % 2)? 0 : 1;

   pcb[run_pid].state = WAIT;
   EnQ(run_pid, &term[which].kb_wait_q);
   run_pid = -1;
}

void ForkService(int *ebx_p){
   int pid, offset, *p;

   if(avail_pid_q.size == 0){
      *ebx_p = -1;
      cons_printf("Kernel Panic: no more process!\n");
      return;
   }

   pid = DeQ(&avail_pid_q);
   *ebx_p = pid;

   MyBzero((char *)&pcb[pid], sizeof(pcb_t));
   MyBzero((char *)&proc_stack[pid][0], PROC_STACK_SIZE);

   pcb[pid].state = READY;
   pcb[pid].ppid = run_pid;
   EnQ(pid, &ready_pid_q);

   MyMemcpy(&proc_stack[pid][0], &proc_stack[run_pid][0], PROC_STACK_SIZE);
   offset = &proc_stack[pid][0] - &proc_stack[run_pid][0];
   pcb[pid].trapframe_p = (trapframe_t *)((int)pcb[run_pid].trapframe_p + offset);

   pcb[pid].trapframe_p->ebx = 0;

   pcb[pid].trapframe_p->edi += offset;
   pcb[pid].trapframe_p->esi += offset;
   pcb[pid].trapframe_p->ebp += offset;
   pcb[pid].trapframe_p->esp += offset;
    
   p = (int *)pcb[pid].trapframe_p->ebp;

   while(*p){
      *p += offset;
      p = (int *)*p;
   }

   MyMemcpy((char *)&signal_table[pid][0], (char *)&signal_table[run_pid][0], SIG_NUM);
}

void SignalService(int signal, func_p_t handler){
   signal_table[run_pid][signal] = handler;  
}

void WrapperService(int pid, func_p_t p){
   trapframe_t temp_frame;
   MyMemcpy((char *)&temp_frame, (char *)pcb[pid].trapframe_p, sizeof(trapframe_t));

   (char *)pcb[pid].trapframe_p -= 8;  
   
   MyMemcpy((char *)pcb[pid].trapframe_p, (char *)&temp_frame, sizeof(trapframe_t));
   MyMemcpy((char *)pcb[pid].trapframe_p + sizeof(trapframe_t) + 4, (char *)&p, sizeof(func_p_t)); 
   MyMemcpy((char *)pcb[pid].trapframe_p + sizeof(trapframe_t), (char *)&pcb[pid].trapframe_p->eip, sizeof(unsigned int));

   pcb[pid].trapframe_p->eip = (unsigned int)Wrapper;
}

void GetppidService(int *p) {
   *p = pcb[run_pid].ppid;
}

void ExitService(int exit_code){
   int ppid, *p;
   char *page_p;

   ppid = pcb[run_pid].ppid;

   if(pcb[ppid].state != WAITCHILD){
      pcb[run_pid].state = ZOMBIE;
      run_pid = -1;
      if(signal_table[run_pid][SIGCHILD]){
         WrapperService(ppid, signal_table[run_pid][SIGCHILD]);
      }
      return;
   }

   pcb[ppid].state = READY;
   EnQ(ppid, &ready_pid_q);
  
   p = (int *)pcb[ppid].trapframe_p->ebx;
   
   *p = exit_code;
   pcb[ppid].trapframe_p->ecx = run_pid;
   page_p = (char *)(PAGE_BASE + (pcb[run_pid].page * PAGE_SIZE));

   EnQ(run_pid, &avail_pid_q);
   EnQ(pcb[run_pid].page, &page_q);
   MyBzero((char *)&pcb[run_pid], sizeof(pcb_t));
   MyBzero((char *)&proc_stack[run_pid][0], PROC_STACK_SIZE);
   MyBzero((char *)&signal_table[run_pid][0], SIG_NUM);
   MyBzero(page_p, PAGE_SIZE);

   run_pid = -1;
}

void WaitchildService(int *exit_code_p, int *child_pid_p){
   int child_pid;
   char *page_p;
   
   for(child_pid = 0; child_pid < PROC_NUM; child_pid++){
      if(pcb[child_pid].state == ZOMBIE && pcb[child_pid].ppid == run_pid){
         break;
      }
   }
   
   if(child_pid == PROC_NUM){
      pcb[run_pid].state = WAITCHILD;
      run_pid = -1;
      return;
   }

   *child_pid_p = child_pid;
   *exit_code_p = 100 - child_pid;
   page_p = (char *)(PAGE_BASE + (pcb[child_pid].page * PAGE_SIZE));

   EnQ(child_pid, &avail_pid_q);
   EnQ(pcb[child_pid].page, &page_q);
   MyBzero((char *)&pcb[child_pid], sizeof(pcb_t));
   MyBzero((char *)&proc_stack[child_pid][0], PROC_STACK_SIZE);
   MyBzero((char *)&signal_table[child_pid][0], SIG_NUM);
   MyBzero(page_p, PAGE_SIZE);
}

void ExecService(func_p_t p, int arg){
   int page, trapframe_location;
   char *page_p;
   int zero = 0;

   page = DeQ(&page_q);
   if(page == -1){
      cons_printf("Kernel Panic: No more pages. \n");
      return;
   }

   pcb[run_pid].page = page;
   page_p = (char *)(PAGE_BASE + (page * PAGE_SIZE));

   MyMemcpy(page_p, (char *)p, PAGE_SIZE);

   MyMemcpy(page_p + PAGE_SIZE - sizeof(int), (char *)&arg, sizeof(int));
   MyMemcpy(page_p + PAGE_SIZE - sizeof(int) - sizeof(int), (char *)&zero, sizeof(int));
   
   pcb[run_pid].trapframe_p->eip = (unsigned int)page_p;
   trapframe_location = (int)page_p + PAGE_SIZE - sizeof(int) - sizeof(int) - sizeof(trapframe_t);

   MyMemcpy((char *)trapframe_location, (char *)pcb[run_pid].trapframe_p, sizeof(trapframe_t));
   pcb[run_pid].trapframe_p = (trapframe_t *)trapframe_location;
}


