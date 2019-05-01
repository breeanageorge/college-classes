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

 	ret 

ReadText:
	
	push %eax
        push %ebx

        mov $3, %eax
        mov $0, %ebx

	mov $100, %edx

        int $0x80

	sub $1, %eax
	mov %eax, %edx

        pop %ebx
        pop %eax	

	ret
