package ast;

import model.NullType;
import model.Token;
import model.Type;

public class NodoExpresionVacia extends NodoExpresion{
    public NodoExpresionVacia(){
        super(new Token("tokenExpresionVacia", "tokenExpresionVacia", 10000));
    }

    @Override
    public Type check() {
        return new NullType();
    }

    public void generate(){

    }
}
