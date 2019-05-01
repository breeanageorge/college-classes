.data
test:
        .ascii"asdfkjasdhffjkasdfkhasdf\n"

idk:
	.space 100	

.text
.include "PARC.asm"
.global _start

_start:

	mov $test, %ecx
	call PCT

	mov $idk, %ecx
	call RCT
	call PCT		

 	mov $1, %eax    #End the program
 	int $0x80       #call Linux
