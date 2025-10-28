package model;

import exceptions.SemanticException;

public class CharType extends PrimitiveType{
    public CharType(){
        super(new Token("char", "char", -1));
    }

    @Override
    public void esCompatible(Type t) throws SemanticException {
        if( !(t instanceof CharType)){
            throw new SemanticException(t.getToken(), "El tipo "+t.getName()+" no es compatible con Char" );
        }
    }

    @Override
    public void esCompatible(Type t, Token token) throws SemanticException {
        if( !(t instanceof CharType)){
            throw new SemanticException(token, "El tipo "+t.getName()+" no es compatible con Char" );
        }
    }
}
