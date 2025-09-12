package exceptions;
import model.Token;

public class SyntacticException extends Exception {
    public SyntacticException(Token actualToken, String tokenName){
        super("Error sintactico en el token: "+actualToken+" con nombre "+tokenName);
    }
}




