#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/stat.h>
#include <unistd.h>
#include <fcntl.h>

#define MAXLINE 80
#define MAXARGS 20

void process_input(int argc, char **argv) {
  /* Problem 1: perform system call execvp to execute command     */ 
  /*            No special operator(s) detected                   */
  /* Hint: Please be sure to review execvp.c sample program       */
  /* if (........ == -1) {                                        */  
  /*  perror("Shell Program");                                    */
  /*  _exit(-1);                                                  */
  /* }d                                                            */
  /* Problem 2: Handle redirection operators: < , or  >, or both  */ 

  int count;
  bool outredir;
  outredir = false;
  bool inredir;
  inredir = false;
  int outind;
  int inind;
  for(count = 0; count < argc; count++) {
    if(strcmp(argv[count],">") == 0)  {
      outredir = true;
      outind = count;
    }
    if(strcmp(argv[count],"<") == 0){
      inredir = true;
      inind = count;
    }
 }
 if(outredir){
   if(argv[outind + 1] == NULL) { 
      printf("Usage: redir <filename.txt>\n"); 
      return; 
   }
   int fileId = open(argv[outind + 1],O_RDWR | O_CREAT | O_TRUNC, S_IRUSR | S_IWUSR);
   argv[outind] = NULL;
   if( fileId < 0 ) {
      printf( "error creating file \n" );
      return;
   }
      
   dup2( fileId, 1);// copy fileID to stdout
   close( fileId ); // close fileId - no longer need 
   execvp(argv[0], argv);
   return;
 }

 if(inredir){
   if (argv[inind + 1] == NULL) { 
      printf("Usage: redir <filename.txt>\n"); 
      exit(0);
   }
   int fileId = open(argv[inind + 1],O_RDONLY);
      
   argv[inind] = NULL;
   if( fileId < 0 ){
      printf( "error creating file \n" );
      return;
   }
      
   dup2( fileId, 0);  //copy fileID to stdin 
   close( fileId ); // close fileId - no longer need 
   execvp(argv[0], argv);
   return;

 }
 execvp(argv[0], argv);
   
}

/* ----------------------------------------------------------------- */
/*                  parse input line into argc/argv format           */
/* ----------------------------------------------------------------- */
int parseline(char *cmdline, char **argv)
{
  int count = 0;
  char *separator = " \n\t";
  argv[count] = strtok(cmdline, separator);
  while ((argv[count] != NULL) && (count+1 < MAXARGS)) {
   argv[++count] = strtok((char *) 0, separator);
  }
  return count;
}
/* ----------------------------------------------------------------- */
/*                  The main program starts here                     */
/* ----------------------------------------------------------------- */
int main(void)
{
 char cmdline[MAXLINE];
 char *argv[MAXARGS];
 int argc;
 int status;
 pid_t pid;

 /* Loop forever to wait and process commands */
 while (1) {
  /* Step 1: Name your shell: csc60mshell - m for mini shell */ 
  printf("csc60mshell--> ");
  fgets(cmdline, MAXLINE, stdin);
  argc = parseline(cmdline, argv);
  if(argc==0)
    continue;
/* Step 1: If user hits enter key without a command, continue to loop again at the beginning */
  /*         Hint: look up for they keyword "continue" in C */
  /* Step 1: Call parseline to build argc/argv: argc/argv parameters declared above */ 
  /* Step 1: Handle build-in command: exit, pwd, or cd - if detect one              */
  /* Step 1: Else, fork off a process */ 
 
  if(strcmp(argv[0], "exit")==0){  //if exit is typed 
    exit(0);	
  }else
  if(strcmp(argv[0], "pwd")==0){   //if pwd is typed
    char* cwd;
    char buff[256];
    cwd = getcwd(buff,sizeof(buff));
    printf("%s\n",cwd);
  }else
  if(strcmp(argv[0], "cd")==0){    //if cd is typed
    if(argc>1)                     //if anything is typed after cd
      chdir(argv[1]);
    else                           //if nothing after cd, assume home
      chdir(getenv("HOME"));
  }else{
  pid = fork();
  if (pid == -1)
    perror("Shell Program fork error");
  else if (pid == 0) 
    /* I am child process. I will execute the command, call: execvp */
    process_input(argc, argv);
  else 
    /* I am parent process */
    if (wait(&status) == -1)
      perror("Shell Program error");
    else
      printf("Child returned status: %d\n",status);
 }
 }
}
