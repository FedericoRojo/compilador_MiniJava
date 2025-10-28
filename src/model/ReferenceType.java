package model;

import TablaSimbolo.TablaSimbolo;
import exceptions.SemanticException;

public class ReferenceType extends Type {
    public Clase associatedClass;

    public ReferenceType(Token token) {
        super(token);
    }

    public void setAssociatedClass(Clase c){
        this.associatedClass = c;
    }

    public Clase getAssociatedClass(){
        return associatedClass;
    }

    @Override
    public void esCompatible(Type t) throws SemanticException {
        if( !(t instanceof ReferenceType) ){
            throw new SemanticException(super.getToken(), "El tipo ReferenceType "+getName()+" no es comptabile con PrimitiveType "+t.getName());
        }

        Clase claseDeT = TablaSimbolo.getInstance().getClassByString(t.getName());

        if( !associatedClass.esSubtipoDe(claseDeT) ){
            throw new SemanticException(super.getToken(), "El tipo ReferenceType "+getName()+" no es compatible con ReferenceType "+t.getName());
        }
    }

    @Override
    public void esCompatible(Type t, Token token) throws SemanticException {
        if( !(t instanceof ReferenceType) ){
            throw new SemanticException(token, "El tipo ReferenceType "+getName()+" no es comptabile con PrimitiveType "+t.getName());
        }

        Clase claseDeT = TablaSimbolo.getInstance().getClassByString(t.getName());

        if( !associatedClass.esSubtipoDe(claseDeT) ){
            throw new SemanticException(token, "El tipo ReferenceType "+getName()+" no es compatible con ReferenceType "+t.getName());
        }
    }

}

