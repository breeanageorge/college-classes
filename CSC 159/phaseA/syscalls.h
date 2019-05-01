// syscalls.h, 159

#ifndef _SYSCALLS_H_
#define _SYSCALLS_H_

int sys_getpid(void);
void sys_write(int fileno, char *str, int len);
void sys_sleep(int centi_sec);
void sys_semwait(int sem_num);
void sys_sempost(int sem_num);
void sys_read(int fileno, char *str, int len);
int sys_fork(void);
void sys_signal(int, func_p_t);
int sys_getppid(void);
void sys_exit(int);
int sys_waitchild(int *);
void sys_exec(func_p_t, int);

#endif
