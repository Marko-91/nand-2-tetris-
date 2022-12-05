import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;

public class CodeWriter {

    private final FileOutputStream fileOutputStream;
    private static final HashMap<String, String> segmentMapping = new HashMap<String, String>() {{
        put("argument", "ARG");
        put("local", "LCL");
        put("static", "16");
        put("constant", "$");
        put("this", "THIS");
        put("that", "THAT");
        put("pointer", "3");
        put("temp", "5");

    }};

    public CodeWriter(FileOutputStream fileOutputStream) {
        this.fileOutputStream = fileOutputStream;
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
                asmCommand += generatePush((index == 0) ? "THIS" : "THAT", index);
                break;
            case "temp":
                asmCommand += generatePush("5", index);
                break;
            case "static":
                asmCommand += generatePush("16", index);
                break;
            case "constant":
                asmCommand += generatePush("0", index);
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
                asmCommand += generatePop((index == 0) ? "THIS" : "THAT", index);
                break;
            case "temp":
                asmCommand += generatePop("5", index);
                break;
            case "static":
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
        asmCommand += "(" + label + ")\n";
        write(asmCommand + "\n");
    }

    public void writeGoto(String label) {
        String asmCommand = "";
        write(String.format("//goto %s\n", label));
        asmCommand += "@" + label + ")\n" +
                "0;JMP\n";

        write(asmCommand + "\n");
    }

    /**
     * Pops the value from the stack and see if val != 0, if so jump to label in code
     * @param label the label inside the current function scope
     */
    public void writeIf(String label) {
        String asmCommand = "";
        write(String.format("//goto %s\n", label));
        asmCommand +=  "@SP\n" +
                "M=M-1\n" +
                "A=M\n" +
                "D=M\n" +
                "@" + label + ")\n" +
                "D;JNE\n";

        write(asmCommand + "\n");
    }

    public void endProgram() {
        write("(INFINITE_LOOP)\n" +
                "@INFINITE_LOOP\n" +
                "0;JMP ");
    }

    private String generatePush(String segment, int index) {

        if (segment.equalsIgnoreCase("constant")) {
            return "@" + index + "\n" + //put constant offset into memory
                    "D=A\n" +
                    "@SP\n" + //select stack pointer
                    "A=M\n" + //select the stack pointer address
                    "M=D\n" + //push the value of D onto the stack address
                    "@SP\n" + //increment stack by 1
                    "M=M+1";
        }

        return "@" + index + "\n" + //put constant offset into memory
                "D=A\n" +
                "@" + segment + "\n" + //select the ram segment
                "A=D+M\n" + //select the ram segment plus the offset
                "D=M\n" + //save the value of ram segment into D register
                "@SP\n" + //select stack pointer
                "A=M\n" + //select the stack pointer address
                "M=D\n" + //push the value of D onto the stack address
                "@SP\n" + //increment stack by 1
                "M=M+1";
    }

    private String generatePop(String segment, int index) {

        return "//region access virtual memory location offset\n" +
                "@" + index + "\n" +
                "D=A\n" +
                "@" + segment + "\n" +
                "A=D+M\n" +
                "//endregion\n" +
                "//region save address into tmp\n" +
                "D=A\n" +
                "@R15\n" +
                "M=D\n" +
                "//endregion\n" +
                "//region decrement stack pointer\n" +
                "@SP\n" +
                "M=M-1\n" +
                "//endregion\n" +
                "//region pop the value from the stack and put into the tmp\n" +
                "A=M\n" +
                "D=M\n" +
                "@R15\n" +
                "A=M\n" +
                "M=D\n" +
                "//region";
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

    private void write(String s) {
        try {
            fileOutputStream.write(s.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
