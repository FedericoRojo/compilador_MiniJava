package model;

public class ReferenceType extends Type {
    public Clase associatedClass;

    public ReferenceType(String nameClass) {
        super(nameClass);
    }

    @Override
    public boolean isCompatible(Type otro) {
        return true;
       /* if (!(otro instanceof ReferenceType)) return false;
        ReferenceType t = (ReferenceType) otro;
        return this.associatedClass.esSubtipoDe(t.associatedClass()); */
    }
}

