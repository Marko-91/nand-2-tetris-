import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;

public class HackAssembler {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Dest should be specified");
            System.exit(1);
        }
        HashMap<String, Integer> symbolTable = new HashMap<>();
        initSymbols(symbolTable);
        Parser parser = new Parser(new Scanner(new File(args[0])), symbolTable);
        Parser labelParser = new Parser(new Scanner(new File(args[0])), symbolTable);
        FileOutputStream fileOutputStream = new FileOutputStream(args[0].replaceAll("asm", "hack"));
        while(labelParser.hasMoreLines()) {
            labelParser.advance();
            labelParser.symbol();
        }
        while(parser.hasMoreLines()) {
            StringBuilder binaryInstruction = new StringBuilder();
            parser.advance();
            InstructionType instructionType = parser.instructionType();
            if (instructionType == InstructionType.A_INSTRUCTION) {
                String symbol = parser.symbol();
                String binaryString = Integer.toBinaryString(Integer.parseInt(symbol));
                appendBase16(binaryString, binaryInstruction);
            } else if (instructionType == InstructionType.C_INSTRUCTION && !parser.getCurrentInstruction().isEmpty()) {
                binaryInstruction.append("111");
                binaryInstruction.append(CodeModule.comp(parser.comp()))
                        .append(CodeModule.dest(parser.dest()))
                        .append(CodeModule.jump(parser.jump()));

            }
            if (!binaryInstruction.toString().isEmpty()) {
                binaryInstruction.append("\n");
                fileOutputStream.write(binaryInstruction.toString().getBytes(StandardCharsets.UTF_8));
            }
        }

        fileOutputStream.close();
    }

    private static void appendBase16(String binaryString, StringBuilder binaryInstruction) {

        if (binaryString == null || binaryString.equals("")) return;

        StringBuilder zeroPadding = new StringBuilder();

        for (int i = 0; i < 16 - binaryString.length(); i++) {
            zeroPadding.append("0");
        }
        binaryInstruction.append(zeroPadding);
        binaryInstruction.append(binaryString);
    }

    public static void initSymbols(HashMap<String, Integer> map) {
        for (int i = 0; i <= 15; i++) {
            String key = "R" + i;
            map.put(key, i);
        }
        map.put("SP", 0);
        map.put("LCL", 1);
        map.put("ARG", 2);
        map.put("THIS", 3);
        map.put("THAT", 4);
        map.put("SCREEN", 16384);
        map.put("KBD", 24576);
    }
}
