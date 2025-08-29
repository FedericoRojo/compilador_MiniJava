public class Token {
    String id;
    String lexeme;
    Integer lineNumber;

    public Token(String id, String lexeme, Integer lineNumber){
        this.id = id;
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
    }

    @Override
    public String toString() {
        return "(" + id + ", " + lexeme + ", " + lineNumber + ")";
    }
}
