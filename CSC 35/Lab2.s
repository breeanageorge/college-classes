#Lab2.s
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
.global _start

_start:

	mov $4, %eax
	mov $1, %ebx
	mov $wiye, %ecx
	mov wiyeLength, %edx
	int $0x80
	
	mov $3, %eax
	mov $0, %ebx
	mov $Excuse, %ecx
	mov $100, %edx
	int $0x80

	SUB $1, %eax
	mov %eax, ExcuseLength
		
	mov $4, %eax
	mov $1, %ebx
	mov $ant, %ecx
	mov antLength, %edx
	int $0x80
	
	mov $3, %eax
	mov $1, %ebx
	mov $Remedy, %ecx
	mov $100, %edx
	int $0x80

	mov %eax, RemedyLength
		
	mov $4, %eax
	mov $1, %ebx
	mov $lsb, %ecx
	mov lsbLength, %edx
	int $0x80

	mov $4, %eax
	mov $1, %ebx
	mov $Excuse, %ecx
	mov ExcuseLength, %edx
	int $0x80

	mov $4, %eax
	mov $1, %ebx
	mov $awn, %ecx
	mov awnLength, %edx
	int $0x80

	mov $4, %eax
	mov $1, %ebx
	mov $Remedy, %ecx
	mov RemedyLength, %edx
	int $0x80	

	mov $1, %eax
	int $0x80
