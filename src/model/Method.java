package model;

import java.util.HashMap;

public class Method {
    String name;
    String modifier;
    Type returnType;
    HashMap<String, Parameter> parameters;

    public Method(Token modificador, Token tipoMetodo, Token idMet){
        name = idMet.getLexeme();
        modifier = modificador.getLexeme();
        setReturnType(tipoMetodo.getLexeme());
    }

    public void addParameter(Parameter param){
        parameters.put(param.name, param);
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

    void setReturnType(String tipo){

    }

}
