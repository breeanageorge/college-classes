Simple Makefile for csc60mshell program
CC=gcc
CFLAGS=-g -Wall
OBJ=george.o
EXE=a.out

all: $(EXE)
$(EXE): $(OBJ)
        $(CC) $(OBJ) -o $(EXE)
csc60_stat.o: george.c
        $(CC) $(CFLAGS) -c george.c
clean:
        rm $(OBJ)
