package TablaSimbolo;

import exceptions.SemanticException;
import model.*;

import java.util.HashMap;

public class TablaSimbolo {
    HashMap<String, Clase> clases;
    TypeTable typeTable;
    Clase currentClass;
    GenericMethod currentMethod;

    public TablaSimbolo(){
        clases = new HashMap<>();
        typeTable = new TypeTable();
        currentClass = null;
        currentMethod = null;
    }

    public void addClass(Clase c) throws SemanticException {
        if( !clases.containsKey( c.name )){
            clases.put(c.name , c);
        }else{
            throw new SemanticException(c.getToken(), "Error: ya existe una clase con ese nombre");
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

    public void addMethodToCurrentClass(Method m) throws SemanticException {
        currentClass.addMethod(m);
    }

    public void associateConstructorToCurrentClass(Constructor c) throws SemanticException {
        currentClass.addConstructor(c);
    }

    public void addParamToCurrentMethod(Parameter param) throws SemanticException{ currentMethod.addParameter(param); }

    public void addAttributeToCurrentClass(Attribute a) throws SemanticException{
        currentClass.addAttribute(a);
    }

    public void addModifierToCurrentClass(String modif){ currentClass.setModifier(modif); }

    public void setParentOfCurrentClass(Token token){
        currentClass.setParent(token, null);
    }

    void estaBienDeclarada(){
        //Aca va el recorrido de las clases que llama a sus hijos para seguir recorriendo todo
    }

}
