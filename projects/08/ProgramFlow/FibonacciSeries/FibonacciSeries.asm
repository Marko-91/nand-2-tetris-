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

// C_POP pointer 1 
@SP
M=M-1
A=M
D=M
@THAT
M=D

// C_PUSH constant 0 
@0
D=A
@SP
A=M
M=D
@SP
M=M+1

// C_POP that 0 
@0
D=A
@THAT
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

// C_PUSH constant 1 
@1
D=A
@SP
A=M
M=D
@SP
M=M+1

// C_POP that 1 
@1
D=A
@THAT
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

// C_PUSH constant 2 
@2
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

//label main_loop_start
(FibonacciSeries.main$main_loop_start)

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

//goto compute_element
@SP
M=M-1
A=M
D=M
@FibonacciSeries.main$compute_element
D;JNE

//goto end_program
@FibonacciSeries.main$end_program
0;JMP

//label compute_element
(FibonacciSeries.main$compute_element)

// C_PUSH that 0 
@0
D=A
@THAT
A=D+M
D=M
@SP
A=M
M=D
@SP
M=M+1

// C_PUSH that 1 
@1
D=A
@THAT
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

// C_POP that 2 
@2
D=A
@THAT
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

// C_PUSH pointer 1 
@THAT
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

// C_POP pointer 1 
@SP
M=M-1
A=M
D=M
@THAT
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

//goto main_loop_start
@FibonacciSeries.main$main_loop_start
0;JMP

//label end_program
(FibonacciSeries.main$end_program)

(INFINITE_LOOP)
@INFINITE_LOOP
0;JMP 