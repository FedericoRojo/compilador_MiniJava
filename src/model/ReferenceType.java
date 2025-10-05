package model;

public class ReferenceType extends Type {
    public Clase associatedClass;

    public ReferenceType(String nameClass) {
        super(nameClass);
    }

    public void setAssociatedClass(Clase c){
        this.associatedClass = c;
    }

    public Clase getAssociatedClass(){
        return associatedClass;
    }

    @Override
    public boolean isCompatible(Type otro) {
        return true;
       /* if (!(otro instanceof ReferenceType)) return false;
        ReferenceType t = (ReferenceType) otro;
        return this.associatedClass.esSubtipoDe(t.associatedClass()); */
    }
}

