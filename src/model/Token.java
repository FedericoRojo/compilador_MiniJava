package model;

public class Token {
    String id;
    String lexeme;
    Integer lineNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }


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
