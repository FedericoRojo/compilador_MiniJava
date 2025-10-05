package model;

import TablaSimbolo.TablaSimbolo;
import exceptions.SemanticException;

public class Attribute {

    String name;
    Type type;
    Token token;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Token getToken(){ return this.token; }

    public void checkWellDefined() throws SemanticException {
        Clase a = TablaSimbolo.getInstance().getClassByString(this.type.getName());
        if( type instanceof ReferenceType refType){
            if(a != null){
                if(refType.getAssociatedClass() == null ){
                    refType.setAssociatedClass(a);
                }
            }else{
                throw new SemanticException(token, "Error: el tipo del atributo esta asociado a una clase que no existe");
            }
        }
    }

    public Attribute(Token tokenAttribute, Type type){
        this.name = tokenAttribute.getLexeme();
        this.type = type;
        this.token= tokenAttribute;
    }
}
