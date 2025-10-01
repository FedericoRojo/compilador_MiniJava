package TablaSimbolo;

import exceptions.SemanticException;
import model.*;

import java.util.HashMap;

public class TablaSimbolo {
    HashMap<String, Clase> clases;
    TypeTable typeTable;
    Clase currentClass;
    Method currentMethod;

    void addClass(Clase c){
        if( !clases.containsKey( c.name )){
            clases.put(c.name , c);
        }else{
            throw new SemanticException("Error, entrada duplicada");
        }
    }

    public Type resolveType(Token typeToken){
        return typeTable.getOrCreateReferenceType(typeToken.getLexeme());
    }

    public Clase getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(Clase currentClass) {
        this.currentClass = currentClass;
    }

    public Method getCurrentMethod() {
        return currentMethod;
    }

    public void setCurrentMethod(Method currentMethod) {
        this.currentMethod = currentMethod;
    }

    public HashMap<String, Clase> getClases() {
        return clases;
    }

    public void setClases(HashMap<String, Clase> clases) {
        this.clases = clases;
    }

    public void addMethodToCurrentClass(Method m){
        currentClass.addMethod(m);
    }

    public void addParamToCurrentMethod(Parameter param){
        currentMethod.addParameter(param);
    }


    void estaBienDeclarada(){
        //Aca va el recorrido de las clases que llama a sus hijos para seguir recorriendo todo
    }

}
