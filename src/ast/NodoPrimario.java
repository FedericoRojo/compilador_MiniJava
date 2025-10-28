package ast;

import model.Token;

public abstract class NodoPrimario extends NodoOperando{
    NodoPrimario encadenado;

    public NodoPrimario(Token t){
        super(t);
    }

    public void setEncadenado(NodoPrimario encadenado){
        this.encadenado = encadenado;
    }

    public Token getToken(){return token;}
}
