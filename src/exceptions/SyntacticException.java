package exceptions;
import model.Token;

public class SyntacticException extends Exception {
    public String message;

    public SyntacticException(Token actualToken, String wantedToken){
        super("[Error:"+actualToken.getLexeme()+"|"+actualToken.getLineNumber()+"]");
        this.message = "Error sintactico en linea "+actualToken.getLineNumber()+" se esperaba "+wantedToken+" se encontró "+actualToken.getLexeme();
    }
}




