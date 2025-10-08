package model;

public class VoidType extends Type{
    public VoidType(){
        super(new Token("void", "void", -1));
    }

    public boolean isCompatible(Type otro){
        return true;
    };

}
