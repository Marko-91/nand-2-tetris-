// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.



(LOOP_KBD)
    @SCREEN
    D=A
    @offset
    M=0
    @KBD
    D=M
    @BLACK
    D;JNE
    @WHITE
    D;JEQ
@LOOP_KBD
0;JMP

(BLACK)
    @offset
    D=M
    @8191
    D=D-A //n-R1
    @LOOP_KBD
    D;JGT //n-R1<0 continue in loop else jump to end

    @offset
    D=M
    @SCREEN
    A=A+D
    M=-1

    @offset
    M=M+1
@BLACK
0;JMP

(WHITE)
    @offset
    D=M
    @8191
    D=D-A //n-R1
    @LOOP_KBD
    D;JGT //n-R1<0 continue in loop else jump to end

    @offset
    D=M
    @SCREEN
    A=A+D
    M=0

    @offset
    M=M+1
@WHITE
0;JMP