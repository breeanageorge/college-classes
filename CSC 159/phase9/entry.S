// entry.S, entries to kernel services, build process trapframe

#include "kernel_constants.h"
#include <spede/machine/asmacros.h> // do not use/include "spede.h"

.comm kernel_stack, 8192, 1
.text

ENTRY(ProcLoader)
   movl 4(%esp), %ebx 
   movl %ebx, %esp 
   popa 
   addl $4, %esp 
   iret 

ENTRY(EnterKernel) 
   pusha 
   movl %esp, %ecx 
   cld 
   movw $0x10, %ax
   mov %ax, %ds
   mov %ax, %es 
   leal kernel_stack + 8192, %esp
   push %ecx 
   cld 
   call CNAME(Kernel)

ENTRY(TimerEntry)
   pushl $TIMER
   jmp EnterKernel

ENTRY(SyscallEntry)
   pushl $SYSCALL
   jmp EnterKernel
   
ENTRY(Term1Entry)
   pushl $TERM1
   jmp EnterKernel

ENTRY(Term2Entry)
   pushl $TERM2
   jmp EnterKernel
