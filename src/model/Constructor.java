package model;

import exceptions.SemanticException;
import exceptions.SyntacticException;

import java.util.HashMap;

public class Constructor extends GenericMethod{
    Clase associatedClass;

    public Constructor(Token cToken, Clase aClass){
        super(cToken.getLexeme(), cToken);
        this.associatedClass = aClass;
    }

    public void checkWellDefined() throws SemanticException {
        if( !super.getName().equals(associatedClass.getName()) ){
            throw new SemanticException(super.getToken(), "Error: el constructor no puede tener un nombre diferente a su clase asociada");
        }
        super.checkWellDefined();
    }

    public String getName(){
        return this.name;
    }
}
