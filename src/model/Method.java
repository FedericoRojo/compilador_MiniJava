package model;


import TablaSimbolo.TablaSimbolo;
import ast.NodoExpresion;
import ast.NodoSentencia;
import exceptions.SemanticException;
import java.util.Objects;

public class Method extends GenericMethod{
    String modifier;
    Type returnType;

    public Method(Token modifier, Type typeMethod, Token idMet, Clase c){
        super(idMet.getLexeme(), idMet, c);
        this.modifier = modifier != null ? modifier.getLexeme() : null;
        this.returnType = typeMethod;
    }

    public boolean isAbstract(){ return (modifier != null && modifier.equals("abstract")); }

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
        if(isAbstract() && hasBlock){
            throw new SemanticException(this.token, "Error: el metodo "+this.getName()+" es abstracto y tiene cuerpo");
        }
        if(!isAbstract() && !hasBlock){
            throw new SemanticException(this.token, "Error: el metodo "+this.getName()+" es concreto y no tiene cuerpo");
        }

        Clase a = TablaSimbolo.getInstance().getClassByString(this.returnType.getName());
        if( returnType instanceof ReferenceType refType){
            if(a != null){
                if(refType.getAssociatedClass() == null ){
                    refType.setAssociatedClass(a);
                }
            }else{
                throw new SemanticException(this.returnType.getToken().getLineNumber(), this.returnType.getName(),
                            "Error: el tipo del metodo "+this.getName()+" esta asociado a una clase que no existe");
            }
        }
        super.checkWellDefined();
    }

    public boolean isFinal(){
        return Objects.equals(modifier, "final");
    }

    public boolean isStatic(){
        return Objects.equals(modifier, "static");
    }

    public boolean hasSameSignature(Method m){
        if (!this.name.equals(m.getName())) return false;

        String thisReturn = (this.returnType != null) ? this.returnType.getName() : null;
        String otherReturn = (m.getReturnType() != null) ? m.getReturnType().getName() : null;
        if (!Objects.equals(thisReturn, otherReturn)) return false;


        if (this.isStatic() != m.isStatic()) return false;
        if (this.isFinal()  != m.isFinal())  return false;

        return super.hasSameParameters(m.getParameters());
    }

    public Type getReturnType() {
        return returnType;
    }


    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }


}
