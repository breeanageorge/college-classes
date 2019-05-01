/******************************************************************************
CSC 135: Homework 1
Due: 10/9/2016 at midnight
Breeana George
recognizer.c

User must input using tokens: 
B E D A ~ I T L W , ( ) R O < = > ! + - * / X Y Z 0 1
followed by a $. Anything after the first $ will not be recognized.
Program will tell you if input is legal or has errors.

**Note that grammar rule while has been changed to while1, and char 
to char 1 for compiling reasons.

To compile and run:
1.Use Athena, and load Recognizer.c
2."gcc -o Recognizer Recognizer.c"
3."./Recognizer"
********************************************************************************/
#include <stdio.h>

void block(void);
void statemnt(void);
void asignmnt(void);
void ifstmt(void);
void while1(void);
void inpout(void);
void comprsn(void);
void exprsn(void);
void factor(void);
void oprnd(void);
void ident(void);
void char1(void);
void integer(void);
void iosym(void);
void opratr(void);
void sumop(void);
void prodop(void);
void letter(void);
void digit(void);

char str[100];
int pointer = 0;
char token = 'k';
char error = 'k';

void main(void){
	
	printf("Enter a stream of legal tokens: ");
	scanf("%s", str);
	
	token = str[pointer];
	
	//call block to start
	block();
	if(error=='t'){
		printf("One or more errors have occurred. \n");	
	}else{
		printf("This is a legal token stream. \n");
	}
}

void match(t){	
		/*TEST for token==t
		printf("\nExpecting token: ");
		printf("%c", token);
		printf("\nReceived token: ");
		printf("%c", t);*/
	if(token==t){
		//advance token pointer
		pointer = pointer+1;
		token = str[pointer];
			//TEST for token advancement
			//printf("\n token at: ");
			//printf("%c", token);
	}else if(token=='$'){
		//end of input symbol
		return;
	}else{
		printf("Error in match: Unxpected token\n");
		error = 't';
	}
	
}

void block(void){
	match('B');
	while(token=='A'||token=='I'||token=='W'||token=='R'||token=='O'||token=='B'){
		statemnt();
	}
	match('E');
	if(token=='D'){
		match('D');
	}
}

void statemnt(void){
	switch(token){
		case 'A' :
			asignmnt();
			break;
		case 'I' :
			ifstmt();
			break;
		case 'W' :
			while1();
			break;	
		case 'R' :
			inpout();
			break;
		case 'O' :
			inpout();
			break;
		case 'B' :
			block();
			break;
		default :
			printf("Error in block: Unxpected token\n");
			break;
	}
}

void asignmnt(void){
	match('A');
	ident();
	match('~');
	exprsn();
}

void ifstmt(void){
	match('I');
	comprsn();
	match('T');
	block();
	if(token=='L'){
		match('L');
		block();
	}
}

void while1(void){
	match('W');
	comprsn();
	block();
}

void inpout(void){
	iosym();
	ident();
	while(token==';'){
		match(';');
		ident();
	}
}

void comprsn(void){
	match('(');
	oprnd();
	opratr();
	oprnd();
	match(')');
}

void exprsn(void){
	factor();
	while(token=='+'||token=='-'){
		sumop();
		factor();
	}
}

void factor(void){
	oprnd();
	while(token=='*'||token=='/'){
		prodop();
		oprnd();
	}
}

void oprnd(void){
	if(token=='0'||token=='1'){
		integer();
	}else if(token=='X'||token=='Y'||token=='Z'){
		ident();
	}else if(token=='('){
		match('(');
		exprsn();
		match(')');
	}else{
		printf("Error in oprnd: Unxpected token\n");
		error = 't';
	}
}

void ident(void){
	letter();
	while(token=='X'||token=='Y'||token=='Z'||token=='0'||token=='1'){
		char1();
	}
}

void char1(void){
	if(token=='X'||token=='Y'||token=='Z'){
		letter();
	}else if(token=='0'||token=='1'){
		digit();
	}else{
		printf("Error in char: Unxpected token\n");
		error = 't';
	}
}

void integer(void){
	while(token=='0'||token=='1'){
		digit();
	}
}

void iosym(void){
	if(token=='R'){
		match('R');
	}else if(token=='O'){
		match('O');
	}else{
		printf("Error in iosym: Unxpected token\n");
		error = 't';
	}
}

void opratr(void){
	switch(token){
		case '<' :
			match('<');
			break;
		case '=' :
			match('=');
			break;
		case '>' :
			match('>');
			break;	
		case '!' :
			match('!');
			break;
		default :
			printf("Error in opratr: Unxpected token\n");
			error = 't';
			break;
	}
}

void sumop(void){
	if(token=='+'){
		match('+');
	}else if(token=='-'){
		match('-');
	}else{
		printf("Error in sumop: Unxpected token\n");
		error = 't';
	}
}

void prodop(void){
	if(token=='*'){
		match('*');
	}else if(token=='/'){
		match('/');
	}else{
		printf("Error in prodop: Unxpected token\n");
		error = 't';
	}
}

void letter(void){
	if(token=='X'){
		match('X');
	}else if(token=='Y'){
		match('Y');
	}else if(token=='Z'){
		match('Z');
	}else{
		printf("Error in letter: Unxpected token\n");
		error = 't';
	}
}

void digit(void){
	if(token=='0'){
		match('0');
	}else if(token=='1'){
		match('1');
	}else{
		printf("Error in digit: Unxpected token\n");
		error = 't';
	}
}


