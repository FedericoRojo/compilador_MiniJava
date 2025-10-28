package model;

import exceptions.SemanticException;

public class NullType extends Type{
    public NullType(){
        super(new Token("null", "null", -1));
    }

    @Override
    public void esCompatible(Type t) throws SemanticException {
        if( !(t instanceof ReferenceType) && !(t instanceof NullType) ) {
            throw new SemanticException(super.getToken(), "Null no es compatible con "+t.getName());
        }
    }

    @Override
    public void esCompatible(Type t, Token token) throws SemanticException {
        if( !(t instanceof ReferenceType) && !(t instanceof NullType) ) {
            throw new SemanticException(token, "Null no es compatible con "+t.getName());
        }
    }
}
