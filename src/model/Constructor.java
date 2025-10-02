package model;

import java.util.HashMap;

public class Constructor extends GenericMethod{
    Clase associatedClass;

    public Constructor(Token cToken, Clase aClass){
        super(cToken.getLexeme());
        this.associatedClass = aClass;
    }

    public String getName(){
        return this.name;
    }
}
