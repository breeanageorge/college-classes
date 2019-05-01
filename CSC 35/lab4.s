#Breeana George
#Lab 4

PrintText:

	push %eax
	push %ebx

	mov $4, %eax
	mov $1, %ebx
	
	int $0x80

	pop %ebx
	pop %eax


ReadText:
