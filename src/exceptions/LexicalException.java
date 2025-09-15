package exceptions;

public class LexicalException extends Exception {

    public String extraMessage;
    public String message;
    public String lexeme;
    public Integer lineNumber;
    public Integer columnIndex;


    public LexicalException(String lexeme, Integer lineNum, String message, Integer col, String currentLine) {

        super("Error léxico en linea "+lineNum+", columna "+(col)+": "+lexeme+" no es un simbolo valido \n" +
                "Detalle: "+currentLine+"\n"+

                " ".repeat(8+col)+"^");
        this.lexeme = lexeme;
        this.lineNumber = lineNum;
        this.columnIndex = col;
        this.extraMessage = message;
        this.message = "[Error:"+this.lexeme+"|"+this.lineNumber+"]";
    }

}


