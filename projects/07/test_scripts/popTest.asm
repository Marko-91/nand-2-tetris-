//region access virtual memory location offset
@55
D=A
@ARG
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
//region pop the value from the stack and put into the temp
 AD=M
// A=M
// D=M
@R15
A=M
M=D
//region