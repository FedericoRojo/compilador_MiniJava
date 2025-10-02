package model;

import exceptions.SemanticException;

import java.util.HashMap;

public class GenericMethod {
    String name;
    Token token;
    HashMap<String, Parameter> parameters;

    public GenericMethod(String name, Token token){
        this.name = name;
        this.token = token;
        this.parameters = new HashMap<>();
    }

    public Token getToken(){ return this.token; }

    public void addParameter(Parameter p) throws SemanticException {
        if(!parameters.containsKey(p.getName())){
            parameters.put(p.getName(), p);
        }else{
            throw new SemanticException(p.getToken(), "Error: este metodo ya tiene un atributo con el mismo nombre");
        }
    }
}
