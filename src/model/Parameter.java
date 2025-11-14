package model;

import TablaSimbolo.TablaSimbolo;
import ast.VarGeneral;
import exceptions.SemanticException;

public class Parameter implements VarGeneral {
    String name;
    Token token;
    Type type;
    int totalParamSize;
    int offset;

    public Parameter(Token param, Type type){
        name = param.getLexeme();
        this.token = param;
        this.type = type;
    }

    public void setOffset(int i){offset = i;}
    public int getOffset(){return offset; }
    public void setTotalParamsSize(int i){ totalParamSize = i; }
    public int getTotalParamsSize(){return totalParamSize;}

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
