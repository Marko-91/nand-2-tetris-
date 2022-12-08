import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VMTranslator {
    public static void main(String[] args) throws FileNotFoundException {
        if (args[0] == null) {
            System.out.println("The file should be present");
            System.exit(1);
        }

        File f = new File(args[0]);
        CodeWriter codeWriter = null;

        if (f.isDirectory()) {
            FileOutputStream fileOutputStream = new FileOutputStream(f.getAbsolutePath() + File.separator + "source.asm");
            boolean wroteBootstrap = false;
            for (File fileLocal : f.listFiles()) {
                if (fileLocal.getName().contains(".vm")) {

                    Parser parser = new Parser(new Scanner(fileLocal));
                    codeWriter =
                            new CodeWriter(fileOutputStream, "Source");
                    if (!wroteBootstrap) {
                        codeWriter.writeBootstrap();
                        wroteBootstrap = true;
                    }
                    advance(parser, codeWriter);
                }
            }
        } else if(f.isFile()) {
            if (args[0].contains(".vm")) { // determine if it's a single file input

                Parser parser = new Parser(new Scanner(new File(args[0])));
                codeWriter = new CodeWriter(
                        new FileOutputStream(args[0].replaceAll("vm$", "asm")),
                        parseFileName(args[0]));
                advance(parser, codeWriter);
            }
            else {
                System.out.println("The file should have {fileName}.vm format");
                System.exit(1);
            }
        }

        //end program
        codeWriter.endProgram();
    }

    private static String parseFileName(String fullName) {
        Pattern p = Pattern.compile("\\w+.vm$");
        Matcher m = p.matcher(fullName);
        String fileName = "";
        if (m.find())
            fileName = m.group(0);

        return fileName.replaceAll("\\.\\w+", "");
    }

    private static void advance(Parser parser, CodeWriter codeWriter) {
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

    }


}
