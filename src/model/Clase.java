package model;

import TablaSimbolo.TablaSimbolo;
import exceptions.SemanticException;
import exceptions.SyntacticException;
import org.w3c.dom.Attr;
import sourcemanager.GeneratorManager;

import java.util.*;

public class Clase {

    public String name;
    Token token;
    int declaredInLineNumber;
    String modifier;
    HashMap<Token, Clase> parent;
    HashMap<String, Attribute> attributes;
    HashMap<String, Method> methods;
    LinkedList<Method> orderedMethods;
    LinkedList<Attribute> orderedAttributes;
    HashMap<String, Constructor> constructors;
    boolean consolidatedMethods;
    boolean consolidatedAttributes;
    String label;
    int lastMethodOffset = 1;
    int lastAttributeOffset = 1;

    public Clase(Token c){
        this.label = "lblVT"+c.getLexeme();
        this.parent = new HashMap<>();
        this.attributes = new HashMap<>();
        this.methods = new HashMap<>();
        this.constructors = new HashMap<>();
        this.orderedMethods = new LinkedList<>();
        this.orderedAttributes = new LinkedList<>();
        this.name = c.getLexeme();
        this.token = c;
        this.declaredInLineNumber = c.lineNumber;
        this.modifier = "";
        this.consolidatedMethods = false;
        this.consolidatedAttributes = false;
    }

    public int getLastMethodOffset(){return this.lastMethodOffset; }

    public void setLastMethodOffset(int i){ this.lastMethodOffset = i; }

    public String getLabel(){return this.label;}

    public int getDeclaredInLineNumber() {
        return declaredInLineNumber;
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

    public Attribute existeVariable(Token tk){
        for(Attribute a: attributes.values()){
            if(a.getName().equals(tk.getLexeme())){
                return a;
            }
        }
        return null;
    }

    public Type existeMetodo(Token tk){
        for(Method m: methods.values()){
            if(m.getName().equals(tk.getLexeme())){
                return m.getReturnType();
            }
        }
        return null;
    }

    public Method getMethod(Token tk){
        for(Method m: methods.values()){
            if(m.getName().equals(tk.getLexeme())){
                return m;
            }
        }
        return null;
    }


    public void checkSentences() throws SemanticException {
        for (Method m : methods.values()) {
            if(m.owner.getName().equals(this.getName())) {
                m.checkSentences();
            }
        }
        if(!isAbstractClass()) {
            getConstructor().checkSentences();
        }
    }

    public Clase getParent() throws SemanticException {
        Clase c = null;
        if(!this.getName().equals("Object")){
            Token key = parent.keySet().iterator().next();
            c = TablaSimbolo.getInstance().getClassByString(key.getLexeme());
        }
        return c;
    }

    public void setParent(Token t, Clase clase) {
        parent.put(t, clase);
    }

    public Token getToken(){ return this.token; }

    public void addMethod(Method m) throws SemanticException {
        if( !methods.containsKey(m.getName()) ){
            methods.put(m.getName(), m);
            orderedMethods.add(m);
        }else{
            throw new SemanticException(m.getToken(), "Error: Ya existe un metodo con nombre "+m.getName()+" en la clase "+this.getName());
        }
    }

    public void addAttribute(Attribute a) throws SemanticException {
        if( !attributes.containsKey(a.getName()) ){
            attributes.put(a.getName(), a);
            orderedAttributes.add(a);
        }else{
            throw new SemanticException(a.getToken(), "Error: ya existe un atributo "+a.getName()+" con el mismo nombre en la clase "+this.getName());
        }

    }

    public void addConstructor(Constructor c) throws SemanticException{
        if(constructors.isEmpty()){
            constructors.put(c.getName(), c);
        }else{
            throw new SemanticException(c.getToken(), "Error: la clase "+this.getName()+" ya tiene un constructor asociado");
        }
    }

    public void checkWellDefined() throws SemanticException{
        checkParent();
        isAbstractAndHasConstructor();
        isConcrete();
    }

    public void consolidate() throws SemanticException{
            consolidateMethods();
            consolidateAtrributes();
    }

    public HashMap<String, Method> getMethods(){ return this.methods; }

    public void consolidateAtrributes() throws SemanticException{
        if(!consolidatedAttributes && !isObjectClass()) {
            Clase currentParent = getParent();
            if (currentParent != null) {
                currentParent.consolidateAtrributes();
                checkAttributes();
                HashMap<String, Attribute> actualAttributes = this.attributes;
                for (Attribute aParent : currentParent.getAttributes().values()) {
                    Attribute aActual = actualAttributes.get(aParent.getName());
                    if (aActual == null) {
                        actualAttributes.put(aParent.getName(), aParent);
                    } else {
                        throw new SemanticException(aActual.getToken(), "Error: la clase " + this.getName() + " tiene un atributo '" + aActual.getName() + "' con el mismo nombre que el de su padre");
                    }
                }
            }
            consolidatedAttributes = true;
        }
    }

    public HashMap<String, Attribute> getAttributes(){ return this.attributes; }

    public boolean isObjectClass(){
        return this.getName().equals("Object");
    }

    public void consolidateMethods() throws SemanticException {
        if(!this.consolidatedMethods && !isObjectClass()) {
            Clase currentParent = getParent();
            if (currentParent != null) {
                currentParent.consolidateMethods();
                checkMethods();
                HashMap<String, Method> actualMethods = this.methods;

                for (Method mParent : currentParent.getMethods().values()) {
                    Method mActual = actualMethods.get(mParent.getName());
                    if (mActual == null) {
                        handleInheritedMethod(mParent, actualMethods);
                    } else {
                        handleOverridenMethod(mActual, mParent);
                        mActual.setOffset(mParent.getOffset());
                    }
                }

            }
            consolidatedMethods = true;
        }
    }

    private void handleOverridenMethod(Method mActual, Method mParent) throws SemanticException {
        if( !mActual.hasSameSignature(mParent)){
            throw new SemanticException(mActual.getToken(), "Error: la clase "+this.getName()+" sobreescribe un metodo heredado pero no coinciden en signatura");
        }
        if( mParent.isFinal() || mParent.isStatic() ){
            throw new SemanticException(mActual.getToken(), "Error: la clase "+this.getName()+" sobreescribe un metodo static o final");
        }
        if( mParent.isAbstract() && !mActual.hasBlock){
            throw new SemanticException(mActual.getToken(), "Error: el metodo "+mActual.getName()+" redefine un metodo abstracto pero no tiene cuerpo");
        }
    }

    public boolean isAbstractClass(){
        return (this.modifier != null && this.modifier.equals("abstract"));
    }

    private void handleInheritedMethod(Method mParent, HashMap<String, Method> actualMethods) throws SemanticException {
        if(mParent.isAbstract()){
            if( !isAbstractClass() ){
                throw new SemanticException(token, "Error: la clase "+this.getName()+" es concreta y no redefine un metodo abstracto heredado");
            }else{
                actualMethods.put(mParent.getName(), mParent);
            }
        }else{
            actualMethods.put(mParent.getName(), mParent);
        }
    }

    public void setLastAttributeOffset(int i){
        this.lastAttributeOffset = i;
    }

    public int getLastAttributeOffset(){
        return lastAttributeOffset;
    }

    public void checkAttributes() throws SemanticException {

        Clase parent = getParent();
        if(parent != null){
            setLastAttributeOffset(parent.getLastAttributeOffset());
        }

        for(Attribute a: orderedAttributes){
            a.checkWellDefined();

            a.setOffset(lastAttributeOffset);
            setLastAttributeOffset(lastAttributeOffset+1);
        }
    }

    public Constructor getConstructor() throws SemanticException {
        String key = constructors.keySet().iterator().next();
        Constructor c = constructors.get(key);
        return c;
    }


    public void checkMethods() throws SemanticException {

        Clase parent = getParent();
        if(parent != null){
            setLastMethodOffset(parent.getLastMethodOffset());
        }

        for(Method m: orderedMethods){
            if(m.isAbstract()){
                if(!this.isAbstractClass()){
                    throw new SemanticException(m.getToken(), "Error: la clase "+this.getName()+" es concreta y tiene un metodo abstracto");
                }
            }
            m.checkWellDefined();

            m.setOffset(lastMethodOffset);
            setLastMethodOffset(lastMethodOffset+1);
        }
    }

    public void checkParent()throws SemanticException{
        if(!isObjectClass()) {
            if (parent.isEmpty()) {
                Clase c = TablaSimbolo.getInstance().getClassByString("Object");
                parent.put(c.getToken(), c);
            } else {
                Token key = parent.keySet().iterator().next();
                Clase c = TablaSimbolo.getInstance().getClassByString(key.getLexeme());

                if (c != null) {
                    if (this.isAbstractClass() && !c.isAbstractClass() && !c.isObjectClass()) {
                        throw new SemanticException(token, "Error: la clase "+this.getName()+" es asbtracta e intenta heredar de una concreta "+c.getName());
                    }
                    if (c.isStaticClass() || c.isFinalClass()) {
                        throw new SemanticException(token, "Error: la clase "+this.getName()+" intenta heredar de una clase static o final");
                    }

                    setParent(key, c);
                    checkCircularity(c);

                } else {
                    throw new SemanticException(key, "Error: la clase "+this.getName()+" intenta heredar de una clase que no existe");
                }

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
        while ( !actual.isObjectClass() && !actual.getParent().getToken().getLexeme().equals("Object") ) {
            Clase aParent = actual.getParent();

            if (visitados.contains(aParent)) {
                throw new SemanticException(this.getToken(), "Error: hay circularidad en la herencia");
            }

            visitados.add(aParent);
            actual = aParent;
        }
    }

    public void isAbstractAndHasConstructor() throws SemanticException{
        if( this.isAbstractClass() ){
            if( !constructors.isEmpty() ){
                throw new SemanticException(getConstructor().getToken(), "Error: la clase abstracta "+this.getName()+" no puede tener constructor asociado");
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

    public boolean esSubtipoDe(Clase otraClase) throws SemanticException {
        Clase actual = this;
        while (actual != null) {
            if (actual.getName().equals(otraClase.getName())) {
                return true;
            }
            actual = actual.getParent();
        }
        return false;
    }

    public void generate(){
        GeneratorManager generator = GeneratorManager.getInstance();
        generator.gen(".DATA");
        generateVT();
        generator.gen(".CODE ; Genero los metodos de la clase");
        generator.gen("");

        for(Method m: methods.values()){
            if(m.owner.getName().equals(this.getName())){
                m.generate();
            }
        }

        if(!constructors.isEmpty()) {
            for (Constructor c : constructors.values()) {
                c.generate();
            }
        }
    }

    public void generateVT(){
        GeneratorManager generator = GeneratorManager.getInstance();

        LinkedList<Method> orderedMethodsByOffset = new LinkedList<>(methods.values());
        orderedMethodsByOffset.sort(Comparator.comparingInt(Method::getOffset));


        generator.gen(this.label+": NOP");
        for(Method m: orderedMethodsByOffset){
           generator.gen("DW " + m.getLabel());
        }
        generator.gen("");
    }

}
