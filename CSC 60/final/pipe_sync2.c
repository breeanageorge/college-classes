#include "curr_time.h"
#include "tlpi_hdr.h"
#include <sys/types.h>
#include <sys/sem.h>
#include <sys/stat.h>
#include "semun.h"

int main(int argc, char *argv[])
{

	int j, dummy, semid;
	union semun arg;
	if(argc < 2 || strcmp(argv[1], "--help") == 0) {
		usageErr("%s No input\n", argv[0]);	
	}
	setbuf(stdout, NULL);
	key_t semkey;
	if((semkey = ftok(".",'a')) == (key_t)-1){
		perror("IPC error:ftok");
		exit(1);
	}
	semid = semget(semkey,1,IPC_CREAT | S_IRUSR | S_IWUSR);
	printf("Semaphoe ID = %d\n", semid);
	arg.val = argc - 1;
	if(semctl(semid, 0, SETVAL, arg) == -1){errExit("semct1"); }
	struct sembuf sop;
	sop.sem_num = 0;
	sop.sem_flg = 0;
	for(j = 1; j < argc; j++) {
		switch(fork()) {
		case -1:
			errExit("Fork");
		case 0: //child process
			sleep(getInt(argv[j], GN_NONNEG, "sleep-time"));
			printf("%s Child %d unlocking (PID=%ld)  \n", currTime("%T"), j, (long) getpid());
			sop.sem_op = -1;
			if(semop(semid, &sop, 1)) { errExit("NOPE");}
			exit(EXIT_SUCCESS);	
		}
	}
	sop.sem_op = 0;
	semop(semid,&sop,1);
	if(semctl(semid, 0, IPC_RMID, 0) == -1) { errExit("semct1"); }
	printf("%s all obstacles removed, parent proceeds \n", currTime("%T"));
	printf("sem id (%d) successfully removed \n", semid);
	exit(EXIT_SUCCESS);
}
