package model;

import ast.NodoSentencia;
import exceptions.SemanticException;
import sourcemanager.GeneratorManager;

public class Constructor extends GenericMethod{
    String label;

    public Constructor(Token cToken, Clase aClass){
        super(cToken.getLexeme(), cToken, aClass);
        this.label = "lblConstructor@"+aClass.getName();
    }

    public String getLabel(){ return label; }

    public void checkWellDefined() throws SemanticException {
        if( !super.getName().equals(owner.getName()) ){
            throw new SemanticException(super.getToken(), "Error: el constructor no puede tener un nombre diferente a su clase asociada");
        }
        super.checkWellDefined();
    }

    public String getName(){
        return this.name;
    }

    public void generate(){
        if(codeBlock != null) {
            GeneratorManager generator = GeneratorManager.getInstance();
            generator.gen(label + ":LOADFP ; apila el valor del RA");
            generator.gen("LOADSP ; apila el valor del registro sp");
            generator.gen("STOREFP; almacena el tope de la pila en el registro");
            for (NodoSentencia sentence : codeBlock) {
                sentence.generate();
            }
            generator.gen("STOREFP ; Alamcena el topo de la pila en el registro");
            generator.gen("RET " + (parameters.size()+1));
            generator.gen("");
        }
    }
}
