package model;

import exceptions.SemanticException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GenericMethod {
    String name;
    Token token;
    List<Parameter> parameters;
    boolean hasBlock;

    public GenericMethod(String name, Token token){
        this.name = name;
        this.token = token;
        this.parameters = new ArrayList<>();
        this.hasBlock = false;
    }

    public void setHasBlock(boolean b){this.hasBlock = b;}

    public String getName(){ return this.name;}

    public Token getToken(){ return this.token; }

    public void checkWellDefined() throws SemanticException{
        for(Parameter p: parameters){
            p.checkWellDefined();
        }
    }

    public boolean hasSameParameters(List<Parameter> params){
        boolean toReturn = true;
        if( parameters.size() != params.size() ){
            toReturn = false;
        }

        for(int i = 0; i < parameters.size() && toReturn; i++){
            Parameter p1 = parameters.get(i);
            Parameter p2 = params.get(i);

            String name1 = (p1 != null && p1.getType()!= null) ? p1.getType().getName() : null;
            String name2 = (p2 != null && p2.getType()!= null) ? p2.getType().getName() : null;

            if(!Objects.equals(name1,name2)){
                toReturn=false;
            }
        }

        return toReturn;
    }


    public List<Parameter> getParameters(){ return this.parameters; }

    public void addParameter(Parameter p) throws SemanticException {
        for (Parameter existing : parameters) {
            if (existing.getName().equals(p.getName())) {
                throw new SemanticException(p.getToken(), "Error: este método ya tiene un parámetro con el mismo nombre");
            }
        }
        parameters.add(p);
    }
}
