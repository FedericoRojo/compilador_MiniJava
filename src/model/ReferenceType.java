package model;

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
}

