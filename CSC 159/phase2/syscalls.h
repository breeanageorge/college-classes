// syscalls.h, 159

#ifndef _SYSCALLS_H_
#define _SYSCALLS_H_

int sys_getpid(void);
void sys_write(int fileno, char *str, int len);
void sys_sleep(int centi_sec);

#endif
