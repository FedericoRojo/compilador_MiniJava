package model;

import exceptions.SemanticException;

public class VoidType extends Type{
    public VoidType(){
        super(new Token("void", "void", -1));
    }

    @Override
    public void esCompatible(Type t) throws SemanticException {
        if( !(t instanceof VoidType)){
            throw new SemanticException(t.getToken(), "El tipo "+t.getName()+" no es compatible con Char" );
        }
    }

    @Override
    public void esCompatible(Type t, Token token) throws SemanticException {
        if( !(t instanceof VoidType)){
            throw new SemanticException(token, "El tipo "+t.getName()+" no es compatible con Char" );
        }
    }

}
