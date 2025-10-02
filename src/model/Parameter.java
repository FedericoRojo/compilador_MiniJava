package model;

public class Parameter {
    String name;
    Token token;
    Type type;

    public Parameter(Token param, Type type){
        name = param.getLexeme();
        this.token = param;
        this.type = type;
    }

    public Token getToken(){ return this.token; }

    public String getName(){
        return this.name;
    }

    public Type getType() { return type; }
}
