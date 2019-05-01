#lab1.s
#Breeana George

.data				
message:			
	.ascii "Hello World\n"

length:
	.long 13

name:
	.ascii "My name is Breeana George\n"

namelength:
	.long 26

quote:
	.ascii "All ur base r belong to us\n"

quotelength:
	.long 27

.text
.global _start

_start:
	mov $4, %eax
	mov $1, %ebx
	mov $message, %ecx
	mov length, %edx
	int $0x80
	
	mov $4, %eax
	mov $1, %ebx
	mov $name, %ecx
	mov namelength, %edx
	int $0x80

	mov $4, %eax
	mov $1, %ebx
	mov $quote, %ecx
	mov quotelength, %edx
	int $0x80

	mov $1, %eax	#End the program
	int $0x80	#call Linux
