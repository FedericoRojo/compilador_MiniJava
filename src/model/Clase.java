package model;

import TablaSimbolo.TablaSimbolo;
import exceptions.SemanticException;
import exceptions.SyntacticException;
import org.w3c.dom.Attr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Clase {

    public String name;
    Token token;
    int declaredInLineNumber;
    String modifier;
    HashMap<Token, Clase> parent;
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
        this.modifier = "";
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


    public Clase getParent() throws SemanticException {
        Token key = parent.keySet().iterator().next();
        Clase c = TablaSimbolo.getInstance().getClassByString(key.getLexeme());
        return c;
    }

    public void setParent(Token t, Clase clase) {
        parent.put(t, clase);
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

    public void checkWellDefined() throws SemanticException{
        checkParent();
        isAbstractAndHasConstructor();
        isConcrete();
        consolidateMethods();
        consolidateAtrributes();
    }

    public HashMap<String, Method> getMethods(){ return this.methods; }

    public void consolidateAtrributes() throws SemanticException{
        Clase currentParent = getParent();
        if( !currentParent.getName().equals("Object")){
            currentParent.consolidateAtrributes();
            HashMap<String, Attribute> actualAttributes = this.attributes;
            for(Attribute aParent: currentParent.getAttributes().values()){
                Attribute aActual = actualAttributes.get(aParent.getName());
                if(aActual == null){
                    actualAttributes.put(aParent.getName(), aParent);
                }else{
                    throw new SemanticException(aActual.getToken(), "Error: clase actual tiene un atributo de instancia con el mismo nombre que el de su padre");
                }
            }
        }
        checkAttributes();
    }

    public HashMap<String, Attribute> getAttributes(){ return this.attributes; }

    public void consolidateMethods() throws SemanticException {
        Clase currentParent = getParent();
        if( !currentParent.getName().equals("Object")){
            currentParent.consolidateMethods();
            HashMap<String, Method> actualMethods = this.methods;

            for(Method mParent: currentParent.getMethods().values()){
                Method mActual = actualMethods.get(mParent.getName());
                if(mActual == null){
                    handleInheritedMethod(mParent, actualMethods);
                }else{
                    handleOverridenMethod(mActual, mParent);
                }
            }

        }
        checkMethods();
    }

    private void handleOverridenMethod(Method mActual, Method mParent) throws SemanticException {
        if( !mActual.hasSameSignature(mParent)){
            throw new SemanticException(token, "Error: clase actual sobreescribe un metodo heredado pero no coinciden en signatura");
        }
        if( mParent.isFinal() || mParent.isStatic() ){
            throw new SemanticException(token, "Error: clase actual sobreescribe un metodo static o final");
        }
    }

    public boolean isAbstractClass(){
        return (this.modifier != null && this.modifier.equals("abstract"));
    }

    private void handleInheritedMethod(Method mParent, HashMap<String, Method> actualMethods) throws SemanticException {
        if(mParent.isAbstract()){
            if( isAbstractClass() ){
                throw new SemanticException(token, "Error: clase actual es concreta y no redefine un metodo abstracto heredado");
            }else{
                actualMethods.put(mParent.getName(), mParent);
            }
        }else{
            actualMethods.put(mParent.getName(), mParent);
        }
    }

    public void checkAttributes() throws SemanticException {
        for(Attribute a: attributes.values()){
            a.checkWellDefined();
        }
    }

    public Constructor getConstructor() throws SemanticException {
        String key = constructors.keySet().iterator().next();
        Constructor c = constructors.get(key);
        return c;
    }


    public void checkMethods() throws SemanticException {
        for(Method m: methods.values()){
            m.checkWellDefined();
        }
    }

    public void checkParent()throws SemanticException{
        if(parent.isEmpty()){
            Clase c = TablaSimbolo.getInstance().getClassByString("Object");
            parent.put(c.getToken(), c);
        }else{
            Token key = parent.keySet().iterator().next();
            Clase c = TablaSimbolo.getInstance().getClassByString(key.getLexeme());

            if(c != null){
                if( this.isAbstractClass() && !c.isAbstractClass() && !c.getName().equals("Object")){
                    throw new SemanticException(token, "Error: una clase asbtracta intenta heredar de una concreta");
                }

                if(c.isStaticClass() || c.isFinalClass()){
                    throw new SemanticException(key, "Error: se intenta heredar de una clase static o final");
                }

                setParent(key, c);
                checkCircularity(c);

            }else{
                throw new SemanticException(key, "Error: se intenta heredar de una clase que no existe");
            }

        }
    }

    public boolean isStaticClass(){
        return (this.modifier != null && this.modifier.equals("static"));
    }

    public boolean isFinalClass(){
        return (this.modifier != null && this.modifier.equals("final"));
    }

    public void checkCircularity(Clase c) throws SemanticException {
        Set<Clase> visitados = new HashSet<>();

        Clase actual = c;
        while ( !actual.getParent().getToken().getLexeme().equals("Object") ) {
            Clase aParent = actual.getParent();

            if (visitados.contains(aParent)) {
                throw new SemanticException(c.getToken(), "Error: hay circularidad en la herencia");
            }

            visitados.add(aParent);
            actual = aParent;
        }
    }

    public void isAbstractAndHasConstructor() throws SemanticException{
        if( this.isAbstractClass() ){
            if( !constructors.isEmpty() ){
                throw new SemanticException(this.token, "Error: clase abstracta no puede tener constructor asociado");
            }
        }
    }

    public void isConcrete()throws SemanticException{
        if( !this.isAbstractClass() ){
            if(constructors.isEmpty()){
                Constructor c = new Constructor(token, this);
                constructors.put(c.getName(), c);
            }else{
                Constructor c = getConstructor();
                c.checkWellDefined();
            }
        }
    }

}
