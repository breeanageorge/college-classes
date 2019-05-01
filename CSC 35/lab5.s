#lab5.s
#Breeana George

.data

intro:
	.ascii"Ever played \n"
q1:
	.ascii"Maybe it's Maybeline, maybe it's "
a1:
	.space 100
q2:
	.ascii"This is the way the world ends, this is the way the world ends, not with a bang but with a "
a2:
	.space 100
q3:
	.ascii"Next from JK Rowling, Harry Potter and the Chamber of "
a3:
	.space 100
what:
	.ascii"Wasn't that FUN?!!"

.text
.include "PARC.asm"
.global _start

_start:
	mov $intro, %ecx
	call PCT
	
	mov $q1, %ecx
        call PCT
	
	mov $a1, %ecx
        call RCT
	
	mov $q2, %ecx
        call PCT
	
	mov $a2, %ecx
        call RCT
	
	mov $q3, %ecx
        call PCT

	mov $a3, %ecx
        call RCT

	mov $what, %ecx
        call PCT

		

	mov $1, %eax
        int $0x80

