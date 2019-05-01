// proc.h, 159

#ifndef _PROC_H_
#define _PROC_H_

void IdleProc(void);  
void UserProc(void);
void ChildStuff(int);
void Ouch(void);
void Wrapper(func_p_t);
void ChildHandler(void);

#endif
