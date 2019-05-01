// proc.c, 159

#include "spede.h" 
#include "kernel_data.h" 
#include "proc.h" 
#include "syscalls.h" 

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
   int my_pid, centi_sec, which;
   char str[] = "   ";
   char cmd[BUFF_SIZE];
   
   my_pid = sys_getpid();
   centi_sec = 50 * my_pid;
   
   str[0] = '0' + my_pid/10;
   str[1] = '0' + my_pid%10; 

   which = (my_pid % 2)? TERM1 : TERM2;

   while(1){
      sys_write(which, "\n\r", 2);
      sys_write(which, str, 3); 
      sys_write(which, "enter ", 6);
      sys_write(which, "shell ", 6);
      sys_write(which, "command: ", 9);
      sys_read(which, cmd, BUFF_SIZE);
      sys_write(which, "You've entered: ", 16);
      sys_write(which, cmd, BUFF_SIZE);
      sys_sleep(centi_sec); 
   }
}
