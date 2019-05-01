#Lab4
#LeftSharkRedo.s
#Breeana George

.data

Excuse:
        .space 100

ExcuseLength:
        .long 0

Remedy:
        .space 100

RemedyLength:
        .long 0

wiye:
        .ascii "What is your excuse?\n"

wiyeLength:
        .long 21

ant:
        .ascii "...and next time?\n"

antLength:
        .long 18

lsb:
        .ascii "Left shark blames "

lsbLength:
        .long 18

awn:
        .ascii " and will now "

awnLength:
        .long 14

.text
.include "PrintAndRead.asm"
.global _start

_start:

	mov $wiye, %ecx
        mov wiyeLength, %edx
	call PrintText

	mov $Excuse, %ecx
	call ReadText
	mov %edx, ExcuseLength

	mov $ant, %ecx
        mov antLength, %edx
	call PrintText

	mov $Remedy, %ecx
	call ReadText
	mov %edx, RemedyLength

	mov $lsb, %ecx
        mov lsbLength, %edx
	call PrintText

	mov $Excuse, %ecx
        mov ExcuseLength, %edx
	call PrintText

	mov $awn, %ecx
        mov awnLength, %edx
	call PrintText

	mov $Remedy, %ecx
        mov RemedyLength, %edx
	call PrintText

	mov $1, %eax
        int $0x80	
	
	

