package exceptions;

import model.Token;

public class SemanticException extends Exception {
    String message;

    public SemanticException(Token token, String message){
        super("[Error:"+token.getLexeme()+"|"+token.getLineNumber()+"]");
        this.message = message;
    }
}
