import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class CodeWriter {

    private final FileOutputStream fileOutputStream;
    private int callCommandCount;
    private String fileName;
    private String currentRunningFunctionName = "main"; //default function name

    public CodeWriter(FileOutputStream fileOutputStream, String fileName) {
        this.fileOutputStream = fileOutputStream;
        this.fileName = fileName;
    }

    public void writeArithmetic(String command) {

        write(String.format("// %s \n", command));
        String asmCommand = "";
        switch (command.toLowerCase(Locale.ROOT)) {
            case "add":
                asmCommand += generateBinaryOperation("+");
                break;
            case "sub":
                asmCommand += generateBinaryOperation("-");
                break;
            case "and":
                asmCommand += generateBinaryOperation("&");
                break;
            case "or":
                asmCommand += generateBinaryOperation("|");
                break;
            case "neg":
                asmCommand += generateUnaryOperation("-");
                break;
            case "not":
                asmCommand += generateUnaryOperation("!");
                break;
            case "eq":
                asmCommand += generateCompareOperation("JEQ");
                break;
            case "gt":
                asmCommand += generateCompareOperation("JGT");
                break;
            case "lt":
                asmCommand += generateCompareOperation("JLT");
                break;

        }
        write(asmCommand + "\n");

    }

    public void writePush(CommandType command, String segment, int index) {
        String asmCommand = "";
        write(String.format("// %s %s %d \n", command, segment, index));

        switch (segment.toLowerCase(Locale.ROOT)) {
            case "argument":
                asmCommand += generatePush("ARG", index);
                break;
            case "local":
                asmCommand += generatePush("LCL", index);
                break;
            case "this":
                asmCommand += generatePush("THIS", index);
                break;
            case "that":
                asmCommand += generatePush("THAT", index);
                break;
            case "pointer":
                asmCommand += generatePointerPush(index);
                break;
            case "temp":
                asmCommand += generatePush("5", index);
                break;
            case "static":
                asmCommand += "@" + fileName + "." + index;
                asmCommand += generatePush("16", index);
                break;
            case "constant":
                asmCommand += pushConstant(index);
                break;
        }
        write(asmCommand + "\n");
    }

    public void writePop(CommandType command, String segment, int index) {
        String asmCommand = "";
        write(String.format("// %s %s %d \n", command, segment, index));

        switch (segment.toLowerCase(Locale.ROOT)) {
            case "argument":
                asmCommand += generatePop("ARG", index);
                break;
            case "local":
                asmCommand += generatePop("LCL", index);
                break;
            case "this":
                asmCommand += generatePop("THIS", index);
                break;
            case "that":
                asmCommand += generatePop("THAT", index);
                break;
            case "pointer":
                asmCommand += generatePointerPop(index);
                break;
            case "temp":
                asmCommand += generatePop("5", index);
                break;
            case "static":
                asmCommand += "@" + fileName + "." + index;
                asmCommand += generatePop("16", index);
                break;
            case "constant":
                asmCommand += generatePop("0", index);
                break;
        }
        write(asmCommand + "\n");
    }

    public void writeLabel(String label) {
        String asmCommand = "";
        write(String.format("//label %s\n", label));
        asmCommand += "(" + prefixLabels(label) + ")\n";
        write(asmCommand + "\n");
    }

    public void writeGoto(String label) {
        String asmCommand = "";
        write(String.format("//goto %s\n", label));
        asmCommand += "@" + prefixLabels(label) + "\n" +
                "0;JMP\n";

        write(asmCommand + "\n");
    }

    /**
     * Pops the value from the stack and see if val != 0, if so jump to label in code
     *
     * @param label the label inside the current function scope
     */
    public void writeIf(String label) {
        String asmCommand = "";
        write(String.format("//goto %s\n", label));
        asmCommand += "@SP\n" +
                "M=M-1\n" +
                "A=M\n" +
                "D=M\n" +
                "@" + prefixLabels(label) + "\n" +
                "D;JNE\n";

        write(asmCommand + "\n");
    }

    public void writeCall(String functionName, int nArgs) {
        String asmCommand = "";
        write(String.format("//call %s %d\n", functionName, nArgs));
        asmCommand += "@" + functionName + "$ret." + ++callCommandCount + "\n" +
                "D=A\n" +
                pushD() + //pushD return address onto stack
                "@LCL\n" +
                "D=M\n" +
                pushD() +
                "@ARG\n" +
                "D=M\n" +
                pushD() +
                "@THIS\n" +
                "D=M\n" +
                pushD() +
                "@THAT\n" +
                "D=M\n" +
                pushD() +
                "@5\n" +
                "D=A\n" +
                "@" + nArgs + "\n" +
                "D=D+A\n" +
                "@SP\n" +
                "D=M-D\n" +
                "@ARG\n" +
                "M=D\n" + // ARG=SP-5-nArgs
                "@SP\n" +
                "D=M\n" +
                "@LCL\n" +
                "M=D\n" + // LCL = SP
                "@" + prefixFunctionName(functionName) + "\n" +
                "0;JMP\n" + //goto function
                "(" + functionName + "$ret." + callCommandCount + ")\n"; //generate (returnAddress)


        write(asmCommand + "\n");
    }

    public void writeFunction(String functionName, int nVars) {
        String asmCommand = "";
        write(String.format("//function %s %d\n", functionName, nVars));
        currentRunningFunctionName = functionName;

        asmCommand += "(" + prefixFunctionName(functionName) + ")\n" +
                "@" + nVars + "\n" +
                "D=A\n" +
                "@R14\n" +
                "M=D\n" +
                "(LOOP_" + prefixFunctionName(functionName) + ")\n" + //name collision (LOOP) ?
                "@R14\n" +
                "D=M\n" +
                "@END_LOOP_" + prefixFunctionName(functionName) + "\n" + //name collision (LOOP) ?
                "D;JLE\n" +
                "@0\n" +
                "D=A\n" +
                pushD() +
                "@R14\n" +
                "M=M-1 //n--\n" +
                "@LOOP_" + prefixFunctionName(functionName) + "\n" + //name collision (LOOP) ?
                "0;JMP\n" +
                "(END_LOOP_" + prefixFunctionName(functionName) + ")\n"; //name collision (LOOP) ?


                write(asmCommand + "\n");
    }


    public void writeReturn() {
        String asmCommand = "";
        write("//return\n");

        asmCommand += "@LCL\n" +
                "D=M\n" +
                "@R13\n" +
                "M=D\n" + // var frame (R13) = lcl
                "@5\n" +
                "D=A\n" +
                "@R13\n" +
                "D=M-D\n" +
                "@R14\n" +
                "M=D\n" + // var retAddr (R14) = *(frame - 5)
                "@SP\n" +
                "M=M-1\n" +
                "A=M\n" +
                "D=M\n" +
                "@ARG\n" +
                "A=M\n" +
                "M=D\n" + // *ARG = POP()
                "@ARG\n" +
                "D=M+1\n" +
                "@SP\n" +
                "M=D\n" + // SP = ARG++
                restoreFrameSegmentToCaller(1, "THAT") +
                restoreFrameSegmentToCaller(2, "THIS") +
                restoreFrameSegmentToCaller(3, "ARG") +
                restoreFrameSegmentToCaller(4, "LCL") +
                "@R14\n" +
                "A=M\n" +
                "A=M\n" +
                "0;JMP\n"; //return to the caller


        write(asmCommand + "\n");
    }

    public void writeBootstrap() {
        write("// custom bootstrap code \n");
        String asmCommand = "@256\n" +
                "D=A\n" +
                "@SP\n" +
                "M=D\n";

        write(asmCommand);
        writeCall("sys.init", 0);
    }

    public void endProgram() {
        write("(INFINITE_LOOP)\n" +
                "@INFINITE_LOOP\n" +
                "0;JMP\n");
    }

    private String restoreFrameSegmentToCaller(int index, String segment) {
        return "@" + index + "\n" +
                "D=A\n" +
                "@R13\n" +
                "D=M-D\n" +
                "A=D\n" +
                "D=M\n" +
                "@" + segment + "\n" +
                "M=D\n"; // segment = *(frame - index)
    }

    private String pushConstant(int index) {
        return "@" + index + "\n" + //put constant offset into memory
                "D=A\n" +
                pushD();
    }

    private String generatePointerPush(int index) {
        return "@"+ ((index == 0) ? "THIS" : "THAT") + "\n" +
                "D=M\n" +
                pushD();
    }

    private String generatePointerPop(int index) {
        return "@SP\n" +
                "M=M-1\n" +
                "A=M\n" +
                "D=M\n" +
                "@"+ ((index == 0) ? "THIS" : "THAT") + "\n" +
                "M=D\n";
    }

    private String generatePush(String segment, int index) {

        return "@" + index + "\n" + //put constant offset into memory
                "D=A\n" +
                "@" + segment + "\n" + //select the ram segment
                "A=D+M\n" + //select the ram segment plus the offset
                "D=M\n" + //save the value of ram segment into D register
                pushD();
    }

    private String generatePop(String segment, int index) {

        return "@" + index + "\n" +
                "D=A\n" +
                "@" + segment + "\n" +
                "A=D+M\n" +
                "D=A\n" +
                "@R15\n" +
                "M=D\n" +
                "@SP\n" +
                "M=M-1\n" +
                "A=M\n" +
                "D=M\n" +
                "@R15\n" +
                "A=M\n" +
                "M=D\n";
    }

    private String generateBinaryOperation(String symbol) {
        return "@SP\n" +
                "M=M-1\n" +
                "A=M\n" +
                "D=M\n" +
                "@R13\n" +
                "M=D\n" +
                "@SP\n" +
                "M=M-1\n" +
                "A=M\n" +
                "D=M\n" +
                "@R13\n" +
                "D=D" + symbol + "M\n" +
                "@SP\n" +
                "A=M\n" +
                "M=D\n" +
                "@SP\n" +
                "M=M+1\n";
    }

    private String generateCompareOperation(String symbol) {
        return "@SP\n" +
                "M=M-1\n" +
                "A=M\n" +
                "D=M\n" +
                "@R13\n" +
                "M=D\n" +
                "@SP\n" +
                "M=M-1\n" +
                "A=M\n" +
                "D=M\n" +
                "@R13\n" +
                "D=D-M\n" +
                "@COMPARE\n" +
                "D;" + symbol + "\n" +
                "@SP\n" +
                "A=M\n" +
                "M=0\n" +
                "@ELSE\n" +
                "0;JMP\n" +
                "(COMPARE)\n" +
                "@SP\n" +
                "A=M\n" +
                "M=-1\n" +
                "(ELSE)\n" +
                "@SP\n" +
                "M=M+1\n";
    }

    private String generateUnaryOperation(String symbol) {
        return "@SP\n" +
                "M=M-1\n" +
                "A=M\n" +
                "M=" + symbol + "M\n" +
                "@SP\n" +
                "M=M+1\n";
    }

    /**
     * Push the value of D register onto the stack
     *
     * @return
     */
    private String pushD() {
        return "@SP\n" +
                "A=M\n" +
                "M=D\n" + //pushD return address onto the stack
                "@SP\n" + //increment stack by 1
                "M=M+1\n";
    }

    private String prefixFunctionName(String functionName) {
         return functionName;
    }

    private String prefixLabels(String label) {
        return fileName.concat(".").concat(currentRunningFunctionName).concat("$").concat(label);
    }

    private void write(String s) {
        try {
            fileOutputStream.write(s.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
