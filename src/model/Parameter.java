package model;

import TablaSimbolo.TablaSimbolo;
import exceptions.SemanticException;

public class Parameter {
    String name;
    Token token;
    Type type;

    public Parameter(Token param, Type type){
        name = param.getLexeme();
        this.token = param;
        this.type = type;
    }

    public void checkWellDefined() throws SemanticException {
        Clase a = TablaSimbolo.getInstance().getClassByString(this.type.getName());
        if( type instanceof ReferenceType refType){
            if(a != null){
                if(refType.getAssociatedClass() == null ){
                    refType.setAssociatedClass(a);
                }
            }else{
                throw new SemanticException(this.type.getToken().getLineNumber(), this.type.getName(), "Error: el tipo del parametro "+this.getName()+" esta asociado a una clase que no existe");
            }
        }
    }

    public Token getToken(){ return this.token; }

    public String getName(){
        return this.name;
    }

    public Type getType() { return type; }
}
