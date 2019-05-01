#Breeana George
#Lab 5

PCT:

        push %eax
        push %ebx
	push %edx

	mov $0, %edx

   While:
	movb (%ecx, %edx), %al	
	
	cmp $0, %al
	je After
	
	inc %edx	

	jmp While	

   After:	

        mov $4, %eax
        mov $1, %ebx
        int $0x80 
	
	pop %edx
        pop %ebx
        pop %eax
	
	int $0x80	

        ret


RCT:

        push %eax
        push %ebx
	
        mov $3, %eax
        mov $0, %ebx
	mov $1000, %edx
	int $0x80

	sub $1, %eax
        movb $0, (%ecx, %eax)

	pop %ebx
        pop %eax

        ret
