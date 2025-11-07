package ast;
import exceptions.SemanticException;
import model.Token;
import model.Type;

public abstract class NodoExpresion {
    Token token;

    public NodoExpresion(Token tk){
        token = tk;
    }

    public Token getToken(){
        return token;
    }

    public abstract Type check() throws SemanticException;

    public abstract void generate();
}
