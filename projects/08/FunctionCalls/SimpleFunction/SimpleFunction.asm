//function simplefunction.test 2
(SimpleFunction.simplefunction.test)
@2
D=A
@R14
M=D
(LOOP_SimpleFunction.simplefunction.test)
@R14
D=M
@END_LOOP_SimpleFunction.simplefunction.test
D;JLE
@0
D=A
@SP
A=M
M=D
@SP
M=M+1
@R14
M=M-1 //n--
@LOOP_SimpleFunction.simplefunction.test
0;JMP
(END_LOOP_SimpleFunction.simplefunction.test)

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

// C_PUSH local 1 
@1
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

// not 
@SP
M=M-1
A=M
M=!M
@SP
M=M+1

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

// C_PUSH argument 1 
@1
D=A
@ARG
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

//return
@LCL
D=M
@R13
M=D
@5
D=A
@R13
D=M-D
@R14
M=D
@SP
M=M-1
A=M
D=M
@ARG
A=M
M=D
@ARG
D=M+1
@SP
M=D
@1
D=A
@R13
D=M-D
A=D
D=M
@THAT
M=D
@2
D=A
@R13
D=M-D
A=D
D=M
@THIS
M=D
@3
D=A
@R13
D=M-D
A=D
D=M
@ARG
M=D
@4
D=A
@R13
D=M-D
A=D
D=M
@LCL
M=D
@R14
A=M
A=M
0;JMP

(INFINITE_LOOP)
@INFINITE_LOOP
0;JMP 