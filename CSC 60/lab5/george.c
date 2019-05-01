#include <stdlib.h>
#include <stdio.h> 
#include <string.h> 
#include <stdbool.h> 
#include <sys/types.h> 
#include <sys/wait.h> 
#include <sys/stat.h> 
#include <unistd.h> 
#include <fcntl.h> 
#include <signal.h>
#include <bits/sigaction.h>
#define MAXLINE 80 
#define MAXARGS 20
#define MAXJOBS 20

struct job_array{
  int process_id;
  char command[80];
  int job_number;
};

struct job_array myJobs[MAXJOBS]; 
int proc_count = 0; 

void process_input(int argc, char **argv) {
  int count=0;
	int count2=0;
	bool Routput=false;
	bool Rinput=false;
	int output=0;
	int input=0;
	char* cmd[]={argv[0],0};
	char inputstring[2000];
	char *InString[2000];
	int z=0;
	int i; 
 
  bool inredir = false;
  bool outredir = false;
  
  char* cmd[]={argv[0],0};
  char inputstring[2000];
  char *InString[2000];
  
 for(i=0;i<argc;i++){
		 if(strcmp(argv[i],"<")==0){
                 count++;
       		 }
	}
	for(i=0;i<argc;i++){
                 if(strcmp(argv[i],">")==0){
                 count2++;
                 }
        }
	if(count2>1){
                printf("ERROR-Can't have two output redirects on one line\n");
                exit(0);
        }
	if(count>1){
        	printf("ERROR-Can't have two input redirects on one line\n");
        	exit(0);
  	}
  	if(strcmp(argv[argc-1],"<")==0 || strcmp(argv[argc-1],">")==0){
        	printf("ERROR-No redirection file specified\n");
        	exit(0);
	}
	if(strcmp(argv[0],"<")==0 || strcmp(argv[0],">")==0){
		printf("ERROR-No Command. Make sure file %s is not overwritten\n",argv[1]);

		exit(0);
	}
	for(i=0;i<argc;i++){
		if(strcmp(argv[i],">")==0){
		Routput=true;
		output=i;
		}	
	}
	for(i=0;i<argc;i++){
                if(strcmp(argv[i],"<")==0){
                Rinput=true;
                int input=i;
                }
        }
	if(Rinput && !Routput){
		if(argv[input + 1] == NULL) { printf("Usage: redir <filename.txt>\n");exit(0);}

		int fileId2 = open(argv[input+1], O_RDONLY);

		argv[input] = NULL;
		
		if(fileId2 < 0){printf("Error creating file \n"); exit(0);}

		dup2(fileId2,1);
		close(fileId2);
		execvp(argv[0],argv);
	}

	if(Routput&& !Rinput){
		if(argv[output + 1] == NULL) { printf("Usage: redir <filename.txt>\n");exit(0);}

		int fileId = open(argv[output+1],O_RDWR | O_CREAT | O_TRUNC, S_IRUSR | S_IWUSR);

		argv[output] = NULL;

		if(fileId < 0){printf("Error creating file \n"); exit(0);}

		dup2(fileId,1);
		execvp(cmd[0],argv);
		close(fileId);
	}

	if(Routput && Rinput){
		if(input > output){
			if(argv[input + 1] == NULL) { printf("Usage: redir <filename.txt>\n");exit(0);}

			int fileId2 = open(argv[input+1], O_RDONLY);
			argv[input] = NULL;
			
			if(fileId2 < 0){printf("Error creating file \n"); exit(0);}

			dup2(fileId2,1);
			close(fileId2);
			execvp(argv[input-1],argv);

			if(argv[output + 1] == NULL) { printf("Usage: redir <filename.txt>\n");exit(0);}
			int fileId = open(argv[output+1],O_RDWR | O_CREAT | O_TRUNC, S_IRUSR | S_IWUSR);
			argv[output] = NULL;
			if(fileId < 0){printf("Error creating file \n"); exit(0);}

			dup2(fileId,1);
			execvp(argv[output-1],argv);
			close(fileId);
		}
		if(output > input){
			 if(argv[output + 1] == NULL) { printf("Usage: redir <filename.txt>\n");exit(0);}
                        int fileId = open(argv[output+1],O_RDWR | O_CREAT | O_TRUNC, S_IRUSR | S_IWUSR);
                        argv[output] = NULL;
                        if(fileId < 0){printf("Error creating file \n"); exit(0);}

                        dup2(fileId,1);
                        execvp(argv[output-1],argv);
                        close(fileId);

			if(argv[input + 1] == NULL) { printf("Usage: redir <filename.txt>\n");exit(0);}

                        int fileId2 = open(argv[input+1], O_RDONLY);
                        argv[input] = NULL;

                        if(fileId2 < 0){printf("Error creating file \n"); exit(0);}

                        dup2(fileId2,1);
                        close(fileId2);
                        execvp(argv[input-1],argv);
		}
	}						
		
	if(!Routput && !Rinput){
		execvp(cmd[0],argv);
	}
 
	exit(0);
 
}
/* ----------------------------------------------------------------- */ 
/*                parse input line into argc/argv format             */ 
/* ----------------------------------------------------------------- */ 
int parseline(char *cmdline, char **argv) {
  int count = 0;
  char *separator = " \n\t";
  argv[count] = strtok(cmdline, separator);
  while ((argv[count] != NULL) && (count+1 < MAXARGS)) {
   argv[++count] = strtok((char *) 0, separator);
  }
  return count;
}
/* ----------------------------------------------------------------- */
// Is there is any job running ? 
/* ----------------------------------------------------------------- */
int jobrunning()
{
  int i, flag = 0;
  for (i = 0; i < proc_count; i++)
    if (myJobs[i].process_id != 0) {
       flag = 1;
       break;
    }
  return (flag);
}

/* ----------------------------------------------------------------- */
// New SIGCHLD handler. Loop through entries and mark off finished jobs 
// with 0 
/* ----------------------------------------------------------------- */
void childHandler(int sig){
  int i,status;
  pid_t pid;
  while ((pid = waitpid(-1, &status, WNOHANG | WUNTRACED)) > 0) {
    for(i = 0; i < proc_count; i++){
       if(myJobs[i].process_id == pid){
            printf("[%i]Job %d removed. ", myJobs[i].job_number, pid); 
            myJobs[i].process_id = 0; // mark as deletion
       }
    }
    if (WIFEXITED(status)) 
      printf("Child %d terminated normally. \n", pid);
    else if (WIFSIGNALED(status)) 
      printf("Child %d terminated by signal: %d\n", pid, WTERMSIG(status));
    else if (WIFSTOPPED(status)) 
      printf("Child %d stopped by signal: %d\n", pid, WSTOPSIG(status));
  }
}
/* ----------------------------------------------------------------- */ 
/*                The main program starts here                       */ 
/* ----------------------------------------------------------------- */ 
int main(void) {
 char cmdline[MAXLINE];
 char *argv[MAXARGS];
 int argc;
 int status;
 pid_t pid;
 int process_id;
 char cmdnoamp[80];
 int background =0;

// Declare & register signal handler for the parent to ignore SIGINT (Control-C)
 struct sigaction handler;
 handler.sa_handler = SIG_IGN; // SIG_IGN
 sigemptyset(&handler.sa_mask);
 handler.sa_flags = 0;
 sigaction(SIGINT, &handler, NULL);
 
 
 /* Loop 10 times */
 int count;
 for (count=0;count<10;count++) {
 printf("csc60mshell> ");
  while (fgets(cmdline, MAXLINE, stdin) == NULL )
     continue; 
  ///////////////////////////////////////////////////////////////////////////
  // save a copy of the command text to log later for background job
  char tcmdline[80];
  strcpy(tcmdline,cmdline);

  argc = parseline(cmdline, argv);
  if(argc==0)
    continue;
  if(argc > 1 && strcmp(argv[argc-1],"&")==0){
    if (proc_count == MAXJOBS) { // Max jobs reached
      printf("Max jobs number reached\n"); 
      printf("Run job in front ground\n");
      background = 0;
    }
    else 
      background = 1; // run job in background
    argv[argc-1] = NULL;
    argc--;	
  } else
    background = 0;
  if(strcmp(argv[0],"exit")==0) {
        if (jobrunning())
          printf("Please terminate the running jobs first\n"); 
        else
          exit(0);
  }
  /////////////////////////////////////////////////////
  // New jobs command: display only entries which valid
  else if(strcmp(argv[0],"jobs")==0){
        if (jobrunning()) {
	  printf("Your Current Jobs:\n");
          int i;
	  for(i = 0; i < MAXJOBS; i++){
            if (myJobs[i].process_id != 0)
              printf("[%d] Running \t %s \n", myJobs[i].job_number, myJobs[i].command);  
          }
        }
  }
  /////////////////////////////////////////////////////
  // Non build-in command starts here 
  else {
    pid = fork();
    if (pid == -1) 
      perror("Shell Program fork error");
    if (pid == 0) { // I am a child process
      ///////////////////////////////////////////////////////////
      // new code to create action handler for a child process 
      // to use default setting of SIGINT. That is termination.
      struct sigaction siginthandler;
      siginthandler.sa_handler = SIG_DFL;
      sigemptyset(&siginthandler.sa_mask);
      siginthandler.sa_flags = 0;
      sigaction(SIGINT, &siginthandler, NULL);
      /* I am child process. I will execute the command, call: execvp */ 
      if(background) // if user specifies a background command
        // puts the child in a new process group so user can not kill background job
        setpgid(0, 0);
      process_input(argc, argv);
    } else { // I am a parent
       if(background) { // I am a parent process
        /////////////////////////////////////////////////////////////////
        // new code to log jobs info to job array. Remove & while logging
        tcmdline[strlen(tcmdline)-2] = '\0';
	strcpy(myJobs[proc_count].command,tcmdline);	
	myJobs[proc_count].job_number = proc_count;
	myJobs[proc_count].process_id = pid;
	proc_count++;	
       } 
       else 
           if (wait(&status) == -1 )
             perror("Shell Program error");
           else
             printf("Child returned status: %d\n",status);
    }
  }
 }
}
