// services.h, 159

#ifndef _SERVICES_H_
#define _SERVICES_H_

#include "kernel_types.h"

void NewProcService(func_p_t);
void TimerService(void);
void SyscallService(trapframe_t *p);
void GetpidService(int *p);
void SleepService(int centi_sec);
void WriteService(int fileno, char *str, int len);
void SemwaitService(int sem_num);
void SempostService(int sem_num);

#endif
