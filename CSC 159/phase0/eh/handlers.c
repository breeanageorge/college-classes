//*********************************************************
// handlers.c, Phase 0, Exercise 4 -- Timer Event
//*********************************************************
#include "spede.h"

//2-byte (unsigned short) ptr points to video memory location
//assume screen has 24 rows, 80 cols, upper-left corner (b8000)
char my_name[] = "Breeana George";
//which char in my name
int i = 0;
//count # of timer events
int tick_count = 0;

//position video memory location to show 1st char in name
//vid mem
unsigned short *char_p = (unsigned short *) 0xB8000+12*80+34;

//evoked from TimerEvent
void TimerHandler(){
	int j;
	if(tick_count == 0){
		//use char_p to show i-th char in name + 0xf00 (color mask)
		*char_p = my_name[i] + 0xf00;
	}
	
	tick_count++;
	if(tick_count == 75){
		tick_count = 0;
		i++;
		char_p++;
		if(i == sizeof(my_name)){
			i = 0;
			//set char_p to beginning screen location 
			*char_p = (unsigned short *) 0xB8000+12*80+34;;
			for(j = 0; j < sizeof(my_name); j++){
				char_p[j] = ' ';
			}
		}
	}
	//dismiss timer event (IRQ 0), otherwise, new event from timer
	//won't be recognized by CPU since circut uses edge-trigger flipflop
	//0x20 is PIC control reg, 0x60 dismissed IRQ 0
	outportb(0x20, 0x60);
}
