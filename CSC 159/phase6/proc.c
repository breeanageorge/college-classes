// proc.c, 159

#include "spede.h" 
#include "kernel_data.h" 
#include "proc.h" 
#include "syscalls.h" 
#include "tools.h"

void IdleProc(void) {
   int i;
   unsigned short *p = (unsigned short *) 0xb8000 + 79;
   while(1){
      *p = '0' + 0x0f00;

      for(i=0;i<(LOOP/2);i++){
         asm("inb $0x80");
      }
      
      *p = ' ' + 0x0f00;
      
      for(i=0;i<(LOOP/2);i++){
         asm("inb $0x80");
      }
   }
}

void UserProc(void) {
   int my_pid, centi_sec, which, cpid;
   char str[] = "   ";
   char cmd[BUFF_SIZE];
   char child_str[] = "   ";   

   my_pid = sys_getpid();
   centi_sec = 50 * my_pid;
   
   str[0] = '0' + my_pid/10;
   str[1] = '0' + my_pid%10; 

   which = (my_pid % 2)? TERM1 : TERM2;

   while(1){
      sys_write(which, "\n\r", 2);
      sys_write(which, str, 3); 
      sys_write(which, "enter shell command: ", 21);
      sys_read(which, cmd, BUFF_SIZE);
      sys_write(which, "You've entered: ", 16);
      sys_write(which, cmd, BUFF_SIZE);
      if(MyStrcmp(cmd, "fork")){
         cpid = sys_fork();
         if(cpid == -1){
            cons_printf("Kernel Panic: OS failed to fork\n");
            sys_write(which, "\n\rUserProc: cannot fork!", 24);
         }else if(cpid == 0){
            ChildStuff(which);
         }else if(cpid > 0){
            child_str[0] = '0' + cpid/10;
            child_str[1] = '0' + cpid%10;
            sys_write(which, "\n\rUserProc: forked a child, PID ", 32);
            sys_write(which, child_str, 3);
         }
      }
      sys_sleep(centi_sec); 
   }
}

void ChildStuff(int which){
   int cpid, centi_sec;
   char child_str[] = "   ";
   
   cpid = sys_getpid();
   centi_sec = 50 * cpid;

   child_str[0] = '0' + cpid/10;
   child_str[1] = '0' + cpid%10;

   while(1){
      sys_write(which, "\n\rI'm the child, PID ", 21);
      sys_write(which, child_str, 3);
      sys_sleep(centi_sec);
   }

}
