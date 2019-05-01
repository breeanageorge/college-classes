// entry.h of entry.S
// prototypes those coded in entry.S

#ifndef _ENTRY_H_
#define _ENTRY_H_

#ifndef ASSEMBLER 
                 
__BEGIN_DECLS

#include "kernel_types.h" 

void SyscallEntry(void);
void EnterKernel(void);
void TimerEntry(void); 
void ProcLoader(trapframe_t *); 
void Term1Entry(void);
void Term2Entry(void);

__END_DECLS

#endif

#endif