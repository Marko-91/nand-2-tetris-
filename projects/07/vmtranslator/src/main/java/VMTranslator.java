import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VMTranslator {
    public static void main(String[] args) throws FileNotFoundException {

        if (args[0].contains(".vm")) { // determine if it's a single file input

            Pattern p = Pattern.compile("\\w+.vm$");
            Matcher m = p.matcher(args[0]);
            String fileName = "";
            if (m.find())
                fileName = m.group(0);

            Parser parser = new Parser(new Scanner(new File(args[0])));
            CodeWriter codeWriter = new CodeWriter(
                    new FileOutputStream(args[0].replaceAll("vm$", "asm")),
                   fileName.replaceAll("\\.\\w+", ""));

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
                    case C_LABEL:
                        codeWriter.writeLabel(parser.arg1());
                        break;
                    case C_IF:
                        codeWriter.writeIf(parser.arg1());
                        break;
                    case C_GOTO:
                        codeWriter.writeGoto(parser.arg1());
                        break;
                    case C_CALL:
                        codeWriter.writeCall(parser.arg1(), parser.arg2());
                        break;
                    case C_FUNCTION:
                        codeWriter.writeFunction(parser.arg1(), parser.arg2());
                        break;
                    case C_RETURN:
                        codeWriter.writeReturn();
                        break;

                }


            }
            //end program
            codeWriter.endProgram();
        }
    }
}
