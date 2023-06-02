// C_PUSH constant 0 
@0
D=A
@SP
A=M
M=D
@SP
M=M+1

// C_POP local 0 
@0
D=A
@LCL
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

//label loop_start
(BasicLoop.main$loop_start)

// C_PUSH argument 0 
@0
D=A
@ARG
A=D+M
D=M
@SP
A=M
M=D
@SP
M=M+1

// C_PUSH local 0 
@0
D=A
@LCL
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

// C_POP local 0 
@0
D=A
@LCL
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

// C_PUSH argument 0 
@0
D=A
@ARG
A=D+M
D=M
@SP
A=M
M=D
@SP
M=M+1

// C_PUSH constant 1 
@1
D=A
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

// C_POP argument 0 
@0
D=A
@ARG
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

// C_PUSH argument 0 
@0
D=A
@ARG
A=D+M
D=M
@SP
A=M
M=D
@SP
M=M+1

//goto loop_start
@SP
M=M-1
A=M
D=M
@BasicLoop.main$loop_start
D;JNE

// C_PUSH local 0 
@0
D=A
@LCL
A=D+M
D=M
@SP
A=M
M=D
@SP
M=M+1

(INFINITE_LOOP)
@INFINITE_LOOP
0;JMP 