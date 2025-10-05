package model;


import TablaSimbolo.TablaSimbolo;
import exceptions.SemanticException;

import java.util.HashMap;

public class Method extends GenericMethod{
    String modifier;
    Type returnType;

    public Method(Token modifier, Type typeMethod, Token idMet){
        super(idMet.getLexeme(), idMet);
        this.modifier = modifier != null ? modifier.getLexeme() : null;
        this.returnType = typeMethod;
    }

    public boolean isAbstract(){ return modifier.equals("abstract"); }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void checkWellDefined() throws SemanticException {
        Clase a = TablaSimbolo.getInstance().getClassByString(this.returnType.getName());
        if( returnType instanceof ReferenceType refType){
            if(a != null){
                if(refType.getAssociatedClass() == null ){
                    refType.setAssociatedClass(a);
                }
            }else{
                throw new SemanticException(token, "Error: el tipo del metodo esta asociado a una clase que no existe");
            }
        }
        super.checkWellDefined();
    }

    public void consolidateMethod(){

    }

    public boolean isFinal(){
        return modifier.equals("final");
    }

    public boolean isStatic(){
        return modifier.equals("static");
    }

    public boolean hasSameSignature(Method m){
        return (this.name.equals(m.getName())
                && this.modifier.equals(m.getModifier())
                && this.returnType.getName().equals(m.getReturnType().getName())
                && super.hasSameParameters( m.getParameters() )
                );
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }


}
