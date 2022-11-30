import java.util.HashMap;
import java.util.Scanner;

public class Parser {

    private final Scanner scanner;

    private HashMap<String, Integer> symbolTable;
    private int lineNumber;
    private int ramLoc; //init to 16
    private String currentInstruction;
    private InstructionType currentInstructionType;
    private final String A_INSTRUCTION_MATCHER = "^(\\s*)@([A-Za-z]|[0-9]|\\.|\\$|_|:)*(\\s*)$";
    private final String L_INSTRUCTION_MATCHER = "^(\\s*)\\(([A-Za-z]|[0-9]|\\.|\\$|_|:)*\\)(\\s*)$";
    private final String A_INSTRUCTION_VARIABLE_MATCHER = "^(\\s*)@[A-Za-z].+([0-9]|\\.|\\$|_|:)*(\\s*)$";
    private final String COMP_MATCHER = "^.*=|;.*$";
    private final String DEST_MATCHER = "=\\w*.+";
    private final String JUMP_MATCHER = ".*;";
    public Parser(Scanner scanner) {
        this.scanner = scanner;
    }
    public Parser(Scanner scanner, HashMap<String, Integer> symbolTable) {
        this(scanner);
        this.symbolTable = symbolTable;
        this.ramLoc = 15;
        this.currentInstructionType = null;
    }

    public void advance() {

        currentInstruction = scanner.nextLine();
        while(hasMoreLines() && currentInstruction.isEmpty() || currentInstruction.startsWith("//")) {
            currentInstruction = scanner.nextLine();
        }
        currentInstruction = currentInstruction.replaceAll("//*.+", ""); // inline comments
        currentInstruction = currentInstruction.trim();
        currentInstructionType = instructionType();
        if (currentInstructionType != InstructionType.L_INSTRUCTION)
            lineNumber = lineNumber + 1;
    }

    public InstructionType instructionType() {
        if (currentInstruction.matches(A_INSTRUCTION_MATCHER)) {
            return InstructionType.A_INSTRUCTION;
        } else if (currentInstruction.matches(L_INSTRUCTION_MATCHER)) {
            return InstructionType.L_INSTRUCTION;
        } else {
            return InstructionType.C_INSTRUCTION;
        }
    }

    public String symbol() {
        String currentInstructionTrimmed = currentInstruction.replaceAll("(\\)|\\(|@)", "");
        if (symbolTable.containsKey(currentInstructionTrimmed)) {
            return String.valueOf(symbolTable.get(currentInstructionTrimmed));
        } else {
            if (currentInstructionType == InstructionType.A_INSTRUCTION && currentInstruction.matches(A_INSTRUCTION_VARIABLE_MATCHER)) {
                symbolTable.put(currentInstructionTrimmed, ++ramLoc);
                currentInstructionTrimmed = String.valueOf(symbolTable.get(currentInstructionTrimmed));
            }
        }
        return currentInstructionTrimmed;
    }

    public void parseLabels() {
        String currentInstructionTrimmed = currentInstruction.replaceAll("(\\)|\\(|@)", "");
        if (currentInstructionType == InstructionType.L_INSTRUCTION) {
            symbolTable.put(currentInstructionTrimmed, lineNumber);
        }

    }

    public String comp() {
        if (instructionType() == InstructionType.C_INSTRUCTION) {
            return currentInstruction.replaceAll(COMP_MATCHER, "");
        }
        return null;
    }

    public String dest() {
        if (instructionType() == InstructionType.C_INSTRUCTION) {
            if (currentInstruction.contains("=")) {
                return currentInstruction.replaceAll(DEST_MATCHER, "");
            }
        }
        return "000";
    }

    public String jump() {
        if (instructionType() == InstructionType.C_INSTRUCTION && currentInstruction.contains(";")) {
            return currentInstruction.replaceAll(JUMP_MATCHER, "");
        }
        return "000";
    }

    public boolean hasMoreLines() {
        return scanner.hasNextLine();
    }

    public String getCurrentInstruction() {
        return currentInstruction;
    }

    public HashMap<String, Integer> getSymbolTable() {
        return symbolTable;
    }
}
