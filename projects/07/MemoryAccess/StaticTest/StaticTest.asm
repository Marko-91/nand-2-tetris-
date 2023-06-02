// C_PUSH constant 111 
@111
D=A
@SP
A=M
M=D
@SP
M=M+1

// C_PUSH constant 333 
@333
D=A
@SP
A=M
M=D
@SP
M=M+1

// C_PUSH constant 888 
@888
D=A
@SP
A=M
M=D
@SP
M=M+1

// C_POP static 8 
@8
D=A
@16
A=D+M
D=A
@R15
M=D
@SP
M=M-1
A=M
D=M
@R15
A=M
M=D

// C_POP static 3 
@3
D=A
@16
A=D+M
D=A
@R15
M=D
@SP
M=M-1
A=M
D=M
@R15
A=M
M=D

// C_POP static 1 
@1
D=A
@16
A=D+M
D=A
@R15
M=D
@SP
M=M-1
A=M
D=M
@R15
A=M
M=D

// C_PUSH static 3 
@3
D=A
@16
A=D+M
D=M
@SP
A=M
M=D
@SP
M=M+1

// C_PUSH static 1 
@1
D=A
@16
A=D+M
D=M
@SP
A=M
M=D
@SP
M=M+1

// sub 
@SP
M=M-1
A=M
D=M
@R13
M=D
@SP
M=M-1
A=M
D=M
@R13
D=D-M
@SP
A=M
M=D
@SP
M=M+1

// C_PUSH static 8 
@8
D=A
@16
A=D+M
D=M
@SP
A=M
M=D
@SP
M=M+1

// add 
@SP
M=M-1
A=M
D=M
@R13
M=D
@SP
M=M-1
A=M
D=M
@R13
D=D+M
@SP
A=M
M=D
@SP
M=M+1

(INFINITE_LOOP)
@INFINITE_LOOP
0;JMP
