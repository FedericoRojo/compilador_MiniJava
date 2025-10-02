package model;

import java.util.HashMap;

public class GenericMethod {
    String name;
    HashMap<String, Parameter> parameters;

    public GenericMethod(String name){
        this.name = name;
    }

    public void addParameter(Parameter p){
        parameters.put(p.getName(), p);
    }
}
