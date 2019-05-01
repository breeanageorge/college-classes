//*********************************************************
// NAME: Breeana George
// Phase 0, Exercise 4 -- Timer Event
// main.c
//*********************************************************

//handy LOOP to time 0.6 microseconds
#define LOOP 1666000 
#include "spede.h"
//needs addr of TimerEvent
#include "events.h"

typedef void (* func_ptr_t)();
struct i386_gate *IDT_pl

void RunningProcess(void){
	int i;
	
	//forever loop
	while(1){
		//break loop when TargetPC keyboard pressed
		if(cons_kbhit()){		
			break;
		}
		//loop LOOP times doing: asm("inb $0x80"); to delay CPU for about 1 second
		for (i=0; i<1666000; i++) IO_DELAY();
		//show 'z' per second
		cons_putchar('.');
	}
}

int main(){
	//get IDT location
	IDT_p = get_idt_base();
	//show: "IDT located at DRAM addr %x (%d).\n" (both address of IDT)
	printf("IDT located at DRAM addr %x (%d).\n", IDT_p, IDT_p);
        cons_printf("IDT located at DRAM addr %x (%d).\n", IDT_p, IDT_p);

	fill_gate(&IDT_p[TIMER_EVENT], (int)TimerEvent, get_cs(), ACC_INTR_GATE, 0);
	//0x21 is PIC mask, ~1 is mask
	outportb(0x21, ~1);
	//set/enable intr in CPU EFLAGS reg
	asm("sti");

	//call RunningProcess here to run intil a key is pressed
	while(1){
		RunningProcess();
		if(cons_kbhit()){
			break;
		}
	}
	return 0;
}
