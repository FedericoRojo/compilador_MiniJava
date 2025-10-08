package model;

public abstract class Type {
    public String name;
    public Token tk;

    public Type(Token t) {
        this.name = t.getLexeme();
        this.tk = t;
    }

    public String getName(){return this.name;}

    public Token getToken(){return this.tk; }

}


