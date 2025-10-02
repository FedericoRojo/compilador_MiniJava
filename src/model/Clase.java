package model;

import exceptions.SemanticException;

import java.util.HashMap;

public class Clase {

    public String name;
    Token token;
    int declaredInLineNumber;
    String modifier;
    HashMap<String, Clase> parent;
    HashMap<String, Attribute> attributes;
    HashMap<String, Method> methods;
    HashMap<String, Constructor> constructors;

    public Clase(Token c){
        this.parent = new HashMap<>();
        this.attributes = new HashMap<>();
        this.methods = new HashMap<>();
        this.constructors = new HashMap<>();
        this.name = c.getLexeme();
        this.token = c;
        this.declaredInLineNumber = c.lineNumber;
    }

    public int getDeclaredInLineNumber() {
        return declaredInLineNumber;
    }

    public void setDeclaredInLineNumber(int declaredInLineNumber) {
        this.declaredInLineNumber = declaredInLineNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }


    public HashMap<String, Clase> getParent() {
        return parent;
    }

    public void setParent(Token t, Clase clase) {
        parent.put(t.getLexeme(), clase);
    }

    public Token getToken(){ return this.token; }

    public void addMethod(Method m) throws SemanticException {
        if( !methods.containsKey(m.getName()) ){
            methods.put(m.getName(), m);
        }else{
            throw new SemanticException(m.getToken(), "Error: Ya existe un metodo con ese nombre en la clase");
        }
    }

    public void addAttribute(Attribute a) throws SemanticException {
        if( !attributes.containsKey(a.getName()) ){
            attributes.put(a.getName(), a);
        }else{
            throw new SemanticException(a.getToken(), "Error: ya existe un atributo de instancia con el mismo nombre");
        }

    }

    public void addConstructor(Constructor c) throws SemanticException{
        if(constructors.isEmpty()){
            constructors.put(c.getName(), c);
        }else{
            throw new SemanticException(c.getToken(), "Error: la clase actual ya tiene un constructor asociado");
        }
    }

    void estaBienDeclarada(){

    }

}
