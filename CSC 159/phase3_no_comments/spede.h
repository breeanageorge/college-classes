// spede.h, CpE/CSc159
// SPEDE library headers, ~spede2/Target-i386/Common/include/spede

#ifndef _SPEDE_H_
#define _SPEDE_H_

#include <spede/flames.h> 
#include <spede/sys/cdefs.h> 
#include <spede/stdio.h> 
#include <spede/assert.h> 
#include <spede/machine/io.h> 
#include <spede/machine/proc_reg.h>
#include <spede/machine/seg.h> 
#include <spede/machine/asmacros.h> 
#include <spede/machine/rs232.h>    

// #include <spede/machine/pic.h> // IRQ_VECTOR(), IRQ_TIMER, IRQ_COM1, ICU0_IOBASE, SPECIFIC_EOI(), etc.
// #include <spede/stdlib.h> // TRUE, FALSE, NULL, etc.
// #include <spede/time.h> // CLK_TCK, time_t, clock(), time(), etc.

#endif // ifndef _SPEDE_H_

/*  I/O port 0x80 is reserved and will never be used.  If you
 *  access this port, you create a delay of three cycles at 8.33MHz,
 *  plus the I/O access, which takes two clock cycles (total of 5).
 *  An unmapped I/O port goes out to the ISA bus, which runs with
 *  a 8.33MHz clock. Especially true in PCI systems.
 *  Two of these in a row give a 1.2 microsecond delay.
 *       Attributed to James van Artsdalen.
 */
// #define  IO_DELAY()  ASM ("inb $0x80,%%al" : : : "eax")
