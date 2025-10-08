package exceptions;

import model.Token;

public class SemanticException extends Exception {
    public String message;

    public SemanticException(Token token, String message){
        super("[Error:"+token.getLexeme()+"|"+token.getLineNumber()+"]");
        this.message = message;
    }

    public SemanticException(int lineNumber, String name, String message){
        super("[Error:" + name + "|" + lineNumber + "]");
        this.message = message;
    }
}

