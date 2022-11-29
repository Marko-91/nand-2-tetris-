import java.util.Locale;

public class CodeModule {
    public static String dest(String destSymbol) {
        switch (destSymbol.toUpperCase(Locale.ROOT)) {
            case "M":
                return "001";
            case "D" :
                return "010";
            case "DM" :
            case "MD" :
                return "011";
            case "A" :
                return "100";
            case "AM" :
                return "101";
            case "AD" :
                return "110";
            case "ADM" :
                return "111";
            default:
                return "000";
        }
    }

    public static String jump(String destSymbol) {
        switch (destSymbol.toUpperCase(Locale.ROOT)) {
            case "JGT":
                return "001";
            case "JEQ" :
                return "010";
            case "JGE" :
                return "011";
            case "JLT" :
                return "100";
            case "JNE" :
                return "101";
            case "JLE" :
                return "110";
            case "JMP" :
                return "111";
            default:
                return "000";
        }
    }

    public static String comp(String destSymbol) {

        String code = "";
        if (destSymbol.contains("M"))  //a=1
            code += "1";
         else  //a=0
            code += "0";

        switch (destSymbol.toUpperCase(Locale.ROOT)) {
            case "0":
                code += "101010";
                break;
            case "1":
                code += "111111";
                break;
            case "-1":
                code += "111010";
                break;
            case "D":
                code += "001100";
                break;
            case "A":
            case "M":
                code += "110000";
                break;
            case "!D":
                code += "001101";
                break;
            case "!A":
            case "!M":
                code += "110001";
                break;
            case "-D":
                code += "001111";
                break;
            case "-A":
            case "-M":
                code += "110011";
                break;
            case "D+1":
                code += "011111";
                break;
            case "A+1":
            case "M+1":
                code += "110111";
                break;
            case "D-1":
                code += "001110";
                break;
            case "A-1":
            case "M-1":
                code += "110010";
                break;
            case "D+A":
            case "D+M":
                code += "000010";
                break;
            case "D-A":
            case "D-M":
                code += "010011";
                break;
            case "A-D":
            case "M-D":
                code += "000111";
                break;
            case "D&A":
            case "D&M":
                code += "000000";
                break;
            case "D|A":
            case "D|M":
                code += "010101";
                break;
        }

       return code;
    }

}
