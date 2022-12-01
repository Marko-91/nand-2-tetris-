import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;

public class VMTranslator {
    public static void main(String[] args) throws FileNotFoundException {
        Parser parser = new Parser(new Scanner(new File(args[0])));
        CodeWriter codeWriter = new CodeWriter
                (new FileOutputStream(args[0].replaceAll("vm", "asm")));
        while (parser.hasMoreLines()) {
            parser.advance();
            CommandType commandType = parser.commandType();

            switch (commandType) {
                case C_PUSH:
                    codeWriter.writePush(commandType, parser.arg1(), parser.arg2());
                    break;
                case C_POP:
                    codeWriter.writePop(commandType, parser.arg1(), parser.arg2());
                    break;
                case C_ARITHMETIC:
                    codeWriter.writeArithmetic(parser.arg1());
                    break;

            }


        }
        //end program
        codeWriter.endProgram();
    }
}
