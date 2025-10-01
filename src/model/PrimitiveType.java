package model;

public class PrimitiveType extends Type {
    public enum Primitive { INT, BOOLEAN, CHAR, VOID }

    public Primitive tipoPrimitivo;

    public PrimitiveType(Primitive tipoPrimitivo) {
        super(tipoPrimitivo.name().toLowerCase());
        this.tipoPrimitivo = tipoPrimitivo;
    }

    @Override
    public boolean isCompatible(Type otro) {
        if (otro instanceof PrimitiveType) {
            return this.tipoPrimitivo == ((PrimitiveType) otro).tipoPrimitivo;
        }
        return false;
    }
}

