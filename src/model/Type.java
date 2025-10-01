package model;

public abstract class Type {
    public String name;

    public Type(String nombre) {
        this.name = nombre;
    }

    public abstract boolean isCompatible(Type otro);

    @Override
    public String toString() {
        return name;
    }
}


