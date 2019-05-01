# This library contains a two subroutines that can help read/write 32-bit
# integers and text. I used stack frames and a few minor tricks to avoid the 
# need for global storage. So, this file can be included directly into your
# project using the .include directive (after the .text directive).
#
# .include "ascii-int.asm"
#
# The code can be optimized at bit, but I wanted it to be as readable as 
# possible. So, I didn't use any crazy tricks.
#
# PrintInt   : Print a 32-bit unsigned integer
# ReadInt    : Read a 32-bit unsigned integer  

#-----------------------------------------------------------------------------
# Subroutine: PrintInt
#
# Arguments:
#    eax: The unsigned 32-bit value to be printed to the screen
#
# Returned:
#    None
#
# Author(s):  Devin Cook
#
# Description:
#
# This subroutine prints a binary number to the screen using ASCII characters.
# The algorithm is implemented as right-to-left which results in the 
# characters being computed in reverse order. I am using the stack to push the
# characters, and then later pop them, which reverses the order. The EDI 
# register used to count the characters on the stack (by incrementing) and then
# looped (and decremented) to pop characters.
#
# I also use a stack frames and a local "variable" to call Linux/UNIX. The LEA 
# instruction computes the effective address, but just stores it in a 
# register. I didn't cover it in class. 
#-----------------------------------------------------------------------------
	
PrintInt:
        push  %ebp              #Stack Frames: backup old EBP
        mov   %esp, %ebp        #Stack Frames: use EBP as an anchor - using current stack pointeer
        push  $0                #Stack Frames: local variable: Buffer

        pusha                   #Save registers. PushA = Push All. Must be matched with PopA
	
        mov  $0, %edi           #Initialize EDI. This will be a counter.

PrintIntLoop:
        mov   $0, %edx          #The x86 uses D and A as input. We must clear D.
        mov   $10, %ebx         #We must divide by a register
        div   %ebx              #Divide (D:A) by 10.
        add   $48, %edx         #Convert the remainder 0..9 to ASCII '0'..'9'
	
        push  %edx              #Push the character, we will pop later and print
        add   $1,  %edi         #"inc %edi" is also valid x86

        cmp   $0, %eax          #We will divide A until it is zero. Test last so value of 0 will print.
        jne   PrintIntLoop

PrintIntOutput:
        cmp   $0, %edi          #Loop until we popped everything we pushed
        je    PrintIntDone

        pop   %edx
        movb  %dl, -4(%ebp)     #Put the popped character into our temp variable        

        mov   $4, %eax           #Linux/UNIX command for Write
        mov   $1, %ebx           #Linux/UNIX to output to the screen
        lea   -4(%ebp), %ecx     #Linux/UNIX needs an address. This will (L)oad the (E)ffective (A)ddress of the local variable and put it in ECX
        mov   $1, %edx           #Output just one character
        int   $0x80

        sub   $1, %edi
        jmp   PrintIntOutput     #Next loop
	
PrintIntDone:
        popa                     #Restore general purpose registers
        mov   %ebp, %esp         #Stack Frames: Restore old stack pointer
        pop   %ebp               #Stack Frames: Restore old base pointer
        ret


#-----------------------------------------------------------------------------
# Subroutine: ReadInt
#
# Arguments:
#    None
#
# Returned:
#    eax: The value of the inputted number
#
# Author(s):  Devin Cook
#
# Description:
#
# This subroutine reads an ASCII string from the keyboard and returns the
# binary value in EAX. Linux/UNIX requires a buffer to read characters from
# the keyboard. I used stack frames to create two local variables. One acts
# as the buffer from reading characters. I read one character at a time - so
# a buffer of 4 bytes (one 32-bit word) is sufficient. The  second variable
# stores the running total. This was required since Linux/UNIX requires EAX 
# for the command (read = 3).
#
# This sub also has logic to ignore the rest of a line if a non 0..9 character
# is read. This makes it function like the BASIC VAL() function.
#-----------------------------------------------------------------------------

ReadInt:
        push  %ebp              #Stack Frames: backup old EBP
        mov   %esp, %ebp        #Stack Frames: use EBP as an anchor - using current stack pointeer
        push  $0                #Stack Frames: local (-4): input buffer
        push  $0                #Stack Frames: local (-8): running total
        push  $0                #Stack Frames: local (-12): Ignore rest of line? 0 = No. 1 = Yes 

        push  %ebx              #Save registers
        push  %ecx
        push  %edx
        push  %edi

ReadIntLoop:
        mov   $3, %eax           #Linux/UNIX command for Read
        mov   $1, %ebx           #Linux/UNIX to input from the keyboard
        lea   -4(%ebp), %ecx     #Linux/UNIX needs an address. This will (L)oad the (E)ffective (A)ddress of the local variable and put it in ECX
        mov   $1, %edx           #Read just one character
        int   $0x80

        mov    $0, %ecx          #Clear out ECX
        movb   -4(%ebp), %cl     #Get the character

        cmp    $10, %cl          #Is the character the newline?
        je     ReadIntDone

        cmp    $1, -12(%ebp)     #Are we ignoring the rest of the line? If so, skip the math code below.
        je     ReadIntLoop

        cmp    $48, %cl          #Check if the character is < '0' 
        jl     ReadIntInvalid    #Too low
        cmp    $57, %cl          #Check if the character is > '9'
        jg     ReadIntInvalid    #Too high

        sub    $48, %cl          #Convert '0'...'9' to 0..9

        mov    -8(%ebp), %eax    #Get running total local variable
        mov    $0, %edx          #Clear out EDX. x86 multiplication uses A and D  
        mov    $10, %ebx         #Must multiply by a register
        mul    %ebx              #Multiply (D:A) by 10
        add    %ecx, %eax
        mov    %eax, -8(%ebp)    #Save the running total

        jmp    ReadIntLoop

ReadIntInvalid:
        movl   $1, -12(%ebp)     #Ignore the rest of the line. We will still read characters
        jmp    ReadIntLoop

ReadIntDone:
        pop   %edi               #Restore registers 
        pop   %edx
        pop   %ecx
        pop   %ebx

        mov   -8(%ebp), %eax     #Result: Running Total       

        mov   %ebp, %esp         #Stack Frames: Restore old stack pointer
        pop   %ebp               #Stack Frames: Restore old base pointer
        ret

