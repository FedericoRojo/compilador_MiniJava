package model;

public class Parameter {
    String name;
    Type type;

    public Parameter(Token param, Type type){
        name = param.getLexeme();
        this.type = type;
    }

    public String getName(){
        return this.name;
    }

    public Type getType() { return type; }
}
