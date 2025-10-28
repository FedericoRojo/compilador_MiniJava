package model;

import exceptions.SemanticException;

public class BooleanType extends PrimitiveType{
    public BooleanType(){
        super(new Token("boolean", "boolean", -1));
    }

    @Override
    public void esCompatible(Type t) throws SemanticException {
        if( !(t instanceof BooleanType)){
            throw new SemanticException(t.getToken(), "El tipo "+t.getName()+" no es compatible con Boolean" );
        }
    }

    @Override
    public void esCompatible(Type t, Token token) throws SemanticException {
        if( !(t instanceof BooleanType)){
            throw new SemanticException(token, "El tipo "+t.getName()+" no es compatible con Boolean" );
        }
    }
}
