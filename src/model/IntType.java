package model;

import exceptions.SemanticException;

public class IntType extends PrimitiveType{
    public IntType(){
        super(new Token("int", "int", -1));
    }

    @Override
    public void esCompatible(Type t) throws SemanticException {
        if(!(t instanceof IntType)){
            throw new SemanticException(super.getToken(), "El tipo "+t.getToken().getLexeme()+" no es compatible con TipoInt");
        }
    }

    @Override
    public void esCompatible(Type t, Token token) throws SemanticException {
        if(!(t instanceof IntType)){
            throw new SemanticException(token, "El tipo "+t.getToken().getLexeme()+" no es compatible con TipoInt");
        }
    }
}
