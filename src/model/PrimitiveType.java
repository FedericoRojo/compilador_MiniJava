package model;

public class PrimitiveType extends Type {

    public PrimitiveType(String nameType) {
        super(nameType);
    }

    @Override
    public boolean isCompatible(Type otro) {
        return true;
    }
}

