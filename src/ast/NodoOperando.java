package ast;

import model.Token;
import model.Type;

public abstract class NodoOperando extends NodoExpresion{
    public NodoOperando(Token tk){
        super(tk);
    }
}
