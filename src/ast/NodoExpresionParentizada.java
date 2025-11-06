package ast;

import exceptions.SemanticException;
import model.Token;
import model.Type;

public class NodoExpresionParentizada extends NodoPrimario {
    NodoExpresion exp;

    public NodoExpresionParentizada(Token tk, NodoExpresion nodo){
        super(tk);
        this.exp = nodo;
    }

    @Override
    public Type check() throws SemanticException {
        return exp.check();
    }


    public void generate(){

    }
}
