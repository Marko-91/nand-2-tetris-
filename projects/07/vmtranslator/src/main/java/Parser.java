import java.util.Locale;
import java.util.Scanner;

public class Parser {

    private final Scanner scanner;
    private String currentInstruction;
    private CommandType commandType;
    private final String ARITHMETIC_MATCHER = "^(add|sub|neg|eq|gt|lt|and|or|not)$";


    public Parser(Scanner scanner) {
        this.scanner = scanner;
    }

    public void advance() {
        currentInstruction = scanner.nextLine();

        while(scanner.hasNextLine() && currentInstruction.isEmpty() || currentInstruction.startsWith("//"))
            currentInstruction = scanner.nextLine();
        currentInstruction = currentInstruction.replaceAll("//*.+", "")
                .trim().toLowerCase(Locale.ROOT); // inline comments
        commandType = commandType(); // get current command type
    }

    public boolean hasMoreLines() {
        return scanner.hasNextLine();
    }

    public CommandType commandType() {
        if (currentInstruction.contains("push")) {
            return CommandType.C_PUSH;
        } else if (currentInstruction.contains("pop")) {
            return CommandType.C_POP;
        } else if(currentInstruction.matches(ARITHMETIC_MATCHER)) {
            return CommandType.C_ARITHMETIC;
        } else if(currentInstruction.contains("label")) {
            return CommandType.C_LABEL;
        }
        throw new IllegalArgumentException("Unknown symbol " + currentInstruction);
    }

    /**
     * Parses and returns the second string in the command
     * @return argument 2 in the code, example: label test -> test
     */
    public String arg1() {
        if (commandType == CommandType.C_ARITHMETIC)
            return currentInstruction;

        return currentInstruction.split("\\s")[1];
    }

    public int arg2() {
        return Integer.parseInt(currentInstruction.split("\\s")[2]);
    }

}
