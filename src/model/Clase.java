package model;

import java.util.HashMap;

public class Clase {

    public String name;
    int declaredInLineNumber;
    String modifier;
    HashMap<String, Clase> parent;
    HashMap<String, Attribute> attributes;
    HashMap<String, Method> methods;
    HashMap<String, Constructor> constructors;

    public Clase(Token c){
        this.name = c.lexeme;
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
        parent.put(t.lexeme, clase);
    }

    public void addMethod(Method m){
        methods.put(m.getName(), m);
    }

    public void addAttribute(Attribute a){
        attributes.put(a.getName(), a);
    }

    public void addConstructor(Constructor c){
        constructors.put(c.getName(), c);
    }

    void estaBienDeclarada(){

    }

}
