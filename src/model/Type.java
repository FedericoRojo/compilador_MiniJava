package model;

import exceptions.SemanticException;

public abstract class Type {
    public String name;
    public Token tk;

    public Type(Token t) {
        this.name = t.getLexeme();
        this.tk = t;
    }

    public String getName(){ return this.name;}

    public Token getToken(){ return this.tk; }

    public abstract void esCompatible(Type t) throws SemanticException;
    public abstract void esCompatible(Type t, Token token) throws SemanticException;
}


