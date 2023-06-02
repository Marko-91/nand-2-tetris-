@55  //put constant offset into memory
D=A
@ARG //select the ram segment
A=D+M  //select the ram segment plus the offset
D=M  //save the value of ram segment into D register
@SP  //select stack pointer
A=M  //select the stack pointer address
M=D  //push the value of D onto the stack address
@SP  //increment stack by 1
M=M+1
