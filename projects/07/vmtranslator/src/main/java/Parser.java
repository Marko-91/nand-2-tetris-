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
        while(scanner.hasNextLine() && currentInstruction.isEmpty() || currentInstruction.startsWith("//")) {
            currentInstruction = scanner.nextLine();
        }
        currentInstruction = currentInstruction.replaceAll("//*.+", ""); // inline comments
        currentInstruction = currentInstruction.trim().toLowerCase(Locale.ROOT);
        commandType = commandType();
    }

    public CommandType commandType() {
        if (currentInstruction.contains("push")) {
            return CommandType.C_PUSH;
        } else if (currentInstruction.contains("pop")) {
            return CommandType.C_POP;
        } else if(currentInstruction.matches(ARITHMETIC_MATCHER)) {
            return CommandType.C_ARITHMETIC;
        }
        return null;
    }

}
