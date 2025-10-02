package TablaSimbolo;

import exceptions.SemanticException;
import model.*;

import java.util.HashMap;

public class TablaSimbolo {
    HashMap<String, Clase> clases;
    TypeTable typeTable;
    Clase currentClass;
    GenericMethod currentMethod;

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

    public GenericMethod getCurrentMethod() {
        return currentMethod;
    }

    public void setCurrentMethod(GenericMethod currentMethod) {
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

    public void associateConstructorToCurrentClass(Constructor c){ currentClass.addConstructor(c); }

    public void addParamToCurrentMethod(Parameter param){ currentMethod.addParameter(param); }

    public void addAttributeToCurrentClass(Attribute a){
        currentClass.addAttribute(a);
    }

    void estaBienDeclarada(){
        //Aca va el recorrido de las clases que llama a sus hijos para seguir recorriendo todo
    }

}
