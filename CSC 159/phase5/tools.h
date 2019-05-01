// tools.h, 159

#ifndef _TOOLS_H_
#define _TOOLS_H_

#include "kernel_types.h" 

void EnQ(int, pid_q_t *);
int DeQ(pid_q_t *);
void MyBzero(char *, int);
void MyStrcpy(char *, char *);
void MyShiftBuffer(char *, int);
void MyAppendToString(char *, char);

#endif

