Simple Makefile for csc60mshell program
CC=gcc
CFLAGS=-g -Wall
OBJ=csc60mshell.o
EXE=a.out

all: $(EXE)
$(EXE): $(OBJ)
        $(CC) $(OBJ) -o $(EXE)
csc60_stat.o: csc60mshell.c
        $(CC) $(CFLAGS) -c csc60mshell.c
clean:
        rm $(OBJ)
