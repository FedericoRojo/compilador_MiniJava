package ast;

import model.Token;

public abstract class NodoPrimario extends NodoOperando{
    NodoPrimario encadenado;
    boolean esAsignable = false;

    public NodoPrimario(Token t){
        super(t);
    }

    public void setEncadenado(NodoPrimario encadenado){
        this.encadenado = encadenado;
    }

    public void setEsAsignable(){ this.esAsignable = true; }

    public NodoPrimario terminoEnLlamadaAMetodo(){
        if(encadenado != null){
            return encadenado.terminoEnLlamadaAMetodo();
        }else{
            if( this instanceof NodoLlamadaMetodo llamadaMetodo){
                return llamadaMetodo;
            }else if( this instanceof NodoLlamadaMetodoEstatico llamadaMetodoEstatico){
                return llamadaMetodoEstatico;
            }
        }
        return null;
    }

    public Token getToken(){return token;}
}
