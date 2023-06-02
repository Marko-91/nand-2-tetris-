// C_POP local 5 
//region access virtual memory location offset
@5
D=A
@LCL
A=D+M
//endregion
//region save address into tmp
D=A
@R15
M=D
//endregion
//region decrement stack pointer
@SP
M=M-1
//endregion
//region pop the value from the stack and put into the tmp
A=M
D=M
@R15
A=M
M=D
//region
(INFINITE_LOOP)
@INFINITE_LOOP
0;JMP 