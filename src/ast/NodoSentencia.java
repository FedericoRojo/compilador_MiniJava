package ast;

import exceptions.SemanticException;
import model.Token;
import model.Type;

public abstract class NodoSentencia {
    Token token;

    public Token getToken(){
        return this.token;
    }

    public void setToken(Token tk){ this.token = tk;}

    public abstract void check() throws SemanticException;
}
