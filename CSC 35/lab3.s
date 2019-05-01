#lab3.s
#Breeana George

.data
bottles:
	.ascii" bottles of beer on the wall; Take one down; "
bottlesLen:
	.long 45
bottle:
	.ascii" bottle of beer on the wall; Take one down; "
bottleLen: 
	.long 44
bottles2:
	.ascii" bottles of beer on the wall.\n"
bottles2Len:
	.long 30
bottle2:
	.ascii" bottle of beer on the wall.\n"
bottle2Len:
	.long 29
var:
	.word 0

.text
.include "ascii-int.asm"
.global _start

_start:
      	
	mov $99, %eax
	

While:	

	cmp $0, %eax
	jle End
If:
	cmp $2, %eax
	jle Then

	call PrintInt
	dec %eax
	mov %eax, var	

	mov $4, %eax
	mov $1, %ebx
	mov $bottles, %ecx
	mov bottlesLen, %edx
	int $0x80	

	mov var, %eax	
	
	call PrintInt	

	mov $4, %eax
        mov $1, %ebx
        mov $bottles2, %ecx
        mov bottles2Len, %edx
        int $0x80

	mov var, %eax
	jmp While	
Then:
	call PrintInt
        dec %eax
        mov %eax, var

        mov $4, %eax
        mov $1, %ebx
        mov $bottles, %ecx
        mov bottlesLen, %edx
        int $0x80

        mov var, %eax

        call PrintInt

        mov $4, %eax
        mov $1, %ebx
        mov $bottle2, %ecx
        mov bottle2Len, %edx
        int $0x80

	mov var, %eax
	call PrintInt
        dec %eax
        mov %eax, var

        mov $4, %eax
        mov $1, %ebx
        mov $bottle, %ecx
        mov bottleLen, %edx
        int $0x80

        mov var, %eax

        call PrintInt

        mov $4, %eax
        mov $1, %ebx
        mov $bottles2, %ecx
        mov bottles2Len, %edx
        int $0x80
	jmp End

End:

        mov $1, %eax    #End the program
        int $0x80       #call Linux


