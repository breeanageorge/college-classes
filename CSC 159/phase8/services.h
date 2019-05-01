// services.h, 159

#ifndef _SERVICES_H_
#define _SERVICES_H_

#include "kernel_types.h"

void NewProcService(func_p_t);
void TimerService(void);
void SyscallService(trapframe_t *);
void GetpidService(int *);
void SleepService(int);
void WriteService(int, char *, int);
void SemwaitService(int);
void SempostService(int);
void TermService(int);
void ReadService(int, char *, int);
void DspService(int);
void KbService(int);
void ForkService(int *);
void SignalService(int, func_p_t);
void WrapperService(int, func_p_t);
void GetppidService(int *);
void ExitService(int);
void WaitchildService(int *, int *);

#endif
