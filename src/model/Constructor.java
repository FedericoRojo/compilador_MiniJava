package model;

import exceptions.SemanticException;

public class Constructor extends GenericMethod{


    public Constructor(Token cToken, Clase aClass){
        super(cToken.getLexeme(), cToken, aClass);
    }

    public void checkWellDefined() throws SemanticException {
        if( !super.getName().equals(owner.getName()) ){
            throw new SemanticException(super.getToken(), "Error: el constructor no puede tener un nombre diferente a su clase asociada");
        }
        super.checkWellDefined();
    }

    public String getName(){
        return this.name;
    }
}
