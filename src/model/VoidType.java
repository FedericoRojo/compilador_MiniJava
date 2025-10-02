package model;

public class VoidType extends Type{
    public VoidType(){
        super("void");
    }

    public boolean isCompatible(Type otro){
        return true;
    };

}
