// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
//
// This program only needs to handle arguments that satisfy
// R0 >= 0, R1 >= 0, and R0*R1 < 32768.

// Put your code here.
@n
M=1 // n=0
@R2
M=0 // sum=0

(LOOP)
//while(n < R1) {
//  sum = sum + R0
//}
@n
D=M
@R1
D=D-M //n-R1
@END
D;JGT //n-R1<0 continue in loop else jump to end

@R0
D=M
@R2
D=D+M
M=D //sum = sum + R0

@n
M=M+1 //n++
@LOOP
0;JMP

(END)
@END
0;JMP // infinite loop