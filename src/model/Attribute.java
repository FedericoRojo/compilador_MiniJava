package model;

public class Attribute {

    String name;
    Type type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }


    public Attribute(Token tokenAttribute, Type type){
        this.name = tokenAttribute.getLexeme();
        this.type = type;
    }
}
