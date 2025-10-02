package model;


public class Method extends GenericMethod{
    String modifier;
    Type returnType;

    public Method(Token modifier, Type typeMethod, Token idMet){
        super(idMet.getLexeme());
        this.modifier = modifier != null ? modifier.getLexeme() : null;
        this.returnType = typeMethod;
    }


    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }


}
