// proc.c, 159
// all processes are coded here
// processes do not use kernel data or code, must ask via service calls

#include "spede.h"       // cons_xxx below needs
#include "kernel_data.h" // run_pid needed below
#include "proc.h"        // prototypes of processes
#include "syscalls.h" 

void IdleProc(void) {
   int i;
   unsigned short *p = (unsigned short *) 0xb8000 + 79;
   while(1){
      *p = '0' + 0x0f00; //show on target PC: "0 " ...SystemProc has PID 0
      
      //repeat LOOP times: call asm("inb $0x80")  // to cause delay approx 1 second
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
   int my_pid, centi_sec, i;
   char str[] = "   ";
   
   my_pid = sys_getpid();
   centi_sec = 50 * my_pid;
   
   str[0] = '0' + my_pid/10;
   str[1] = '0' + my_pid%10; 
   while(1){
      sys_semwait(STDOUT);
      sys_write(STDOUT, str, 3); //show my PID
      for(i = 0; i < LOOP/2; i++) asm("inb $0x80"); //burn CPU time 
      sys_write(STDOUT, "using ", 6);
      for(i = 0; i < LOOP/2; i++) asm("inb $0x80");
      sys_write(STDOUT, "the ", 4);
      for(i = 0; i < LOOP/2; i++) asm("inb $0x80");
      sys_write(STDOUT, "video... ", 9);
      sys_sempost(STDOUT);
      sys_sleep(centi_sec); //sleep for .5 sec x PID
   }
}
