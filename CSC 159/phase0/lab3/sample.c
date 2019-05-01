/* sample.c - Sample Main for SPEDE*/

#include <spede/stdio.h>
#include <spede/flames.h>

int DisplayMsg(int j)
{
	printf("%d Hello world %d \nECS", j, 2*j);
        cons_printf("--> Hello world <--\nCPE/CSC");
	return 0;
}

int main(void)
{
	long i;
	i = 111;
	for(i; i<116; i++)
	{	
		DisplayMsg(i);
	}
	//return 0;
}
	
