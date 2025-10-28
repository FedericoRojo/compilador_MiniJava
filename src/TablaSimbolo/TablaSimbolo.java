package TablaSimbolo;

import ast.Bloque;
import exceptions.SemanticException;
import model.*;

import java.util.HashMap;

public class TablaSimbolo {
    private static TablaSimbolo instance;
    HashMap<String, Clase> clases;
    TypeTable typeTable;
    Clase currentClass;
    GenericMethod currentMethod;
    Bloque currentBlock;

    private TablaSimbolo() throws SemanticException {
        clases = new HashMap<>();
        typeTable = new TypeTable();
        currentClass = null;
        currentMethod = null;
        initBaseClasses();
    }

    public Bloque getCurrentBlock(){
        return this.currentBlock;
    }

    public void setCurrentBlock(Bloque b){
        this.currentBlock = b;
    }

    public static TablaSimbolo getInstance() throws SemanticException {
        if(instance == null){
            instance = new TablaSimbolo();
        }
        return instance;
    }

    public static void removeInstance(){
        instance = null;
    }

    public void checkWellDefined() throws SemanticException{
        for(Clase c: clases.values()){
            c.checkWellDefined();
        }
    }

    public void consolidate() throws SemanticException{
        for(Clase c: clases.values()){
            c.consolidate();
        }
    }

    public void checkSentences() throws SemanticException{
        for(Clase c: clases.values()){
            c.checkSentences();
        }
    }


    private void initBaseClasses() throws SemanticException {
        Clase cObject = new Clase( new Token("-1", "Object", -1));
        Clase cSystem = new Clase( new Token("-2", "System", -2));
        Clase cString = new Clase( new Token("-3", "String", -3));


        Token voidType = new Token("-1", "void", -1);
        Token intType = new Token("-1", "int", -1);
        Token booleanType = new Token("-1", "boolean", -1);
        Token charType = new Token("-1", "char", -1);
        Token stringType = new Token("-1", "String", -1);

        Method debugIMethod = new
                Method( new Token("-1", "static", -1),
                resolveType(voidType),
                new Token("-1", "debugPrint", -1),
                cObject);
        debugIMethod.setHasBlock(true);
        debugIMethod.addParameter(new Parameter(new Token("-2", "i", -2), resolveType(intType)));
        cObject.addMethod( debugIMethod );


        Method mread = new Method( new Token("-2", "static", -2),
                resolveType(intType),
                new Token("-2", "read", -2),
                cSystem);
        mread.setHasBlock(true);
        cSystem.addMethod( mread );

        Method printBMethod = new Method( new Token("-2", "static", -2),
                            resolveType(voidType),
                            new Token("-2", "printB", -2),
                cSystem);
        printBMethod.addParameter(new Parameter(new Token("-2", "b", -2), resolveType(booleanType) ));
        printBMethod.setHasBlock(true);
        cSystem.addMethod(printBMethod);

        Method printCMethod = new Method( new Token("-2", "static", -2),
                                        resolveType(voidType),
                                        new Token("-2", "printC", -2), cSystem);
        printCMethod.addParameter(new Parameter(new Token("-2", "c", -2), resolveType(charType) ));
        printCMethod.setHasBlock(true);
        cSystem.addMethod(printCMethod);


        Method printIMethod = new Method( new Token("-2", "static", -2),
                resolveType(voidType),
                new Token("-2", "printI", -2), cSystem);
        printIMethod.addParameter(new Parameter(new Token("-2", "i", -2), resolveType(intType) ));
        printIMethod.setHasBlock(true);
        cSystem.addMethod(printIMethod);


        Method printSMethod = new Method( new Token("-2", "static", -2),
                resolveType(voidType),
                new Token("-2", "printS", -2), cSystem);
        printSMethod.addParameter(new Parameter(new Token("-2", "s", -2), resolveType(stringType) ));
        printSMethod.setHasBlock(true);
        cSystem.addMethod(printSMethod);


        Method printlnMethod = new Method( new Token("-2", "static", -2),
                resolveType(voidType),
                new Token("-2", "println", -2), cSystem);
        printlnMethod.setHasBlock(true);
        cSystem.addMethod(printlnMethod);


        Method printBlnMethod = new Method( new Token("-2", "static", -2),
                resolveType(voidType),
                new Token("-2", "printBln", -2), cSystem);
        printBlnMethod.addParameter(new Parameter(new Token("-2", "b", -2), resolveType(booleanType) ));
        printBlnMethod.setHasBlock(true);
        cSystem.addMethod(printBlnMethod);


        Method printClnMethod = new Method( new Token("-2", "static", -2),
                resolveType(voidType),
                new Token("-2", "printCln", -2), cSystem);
        printClnMethod.addParameter(new Parameter(new Token("-2", "c", -2), resolveType(charType) ));
        printClnMethod.setHasBlock(true);
        cSystem.addMethod(printClnMethod);


        Method printIlnMethod = new Method( new Token("-2", "static", -2),
                resolveType(voidType),
                new Token("-2", "printIln", -2), cSystem);
        printIlnMethod.addParameter(new Parameter(new Token("-2", "i", -2), resolveType(intType) ));
        printIlnMethod.setHasBlock(true);
        cSystem.addMethod(printIlnMethod);


        Method printSlnMethod = new Method( new Token("-2", "static", -2),
                resolveType(voidType),
                new Token("-2", "printSln", -2), cSystem);
        printSlnMethod.addParameter(new Parameter(new Token("-2", "s", -2), resolveType(stringType) ));
        printSlnMethod.setHasBlock(true);
        cSystem.addMethod(printSlnMethod);

        cSystem.setParent(cObject.getToken(), cObject);
        cString.setParent(cObject.getToken(), cObject);

        clases.put(cObject.getName(), cObject);
        clases.put(cString.getName(), cString);
        clases.put(cSystem.getName(), cSystem);

    }

    public void addClass(Clase c) throws SemanticException {
        if( !clases.containsKey( c.name )){
            clases.put(c.name , c);
        }else{
            if(c.getName().equals("Object") || c.getName().equals("System") || c.getName().equals("String")){
                throw new SemanticException(c.getToken(), "Error: no se puede redeclarar una clase predefinida");
            }else{
                throw new SemanticException(c.getToken(), "Error: ya existe una clase con ese nombre");
            }
        }
    }

    public void actualMethodHasBlock() throws SemanticException {
        currentMethod.setHasBlock(true);
    }

    public Type resolveType(Token typeToken){
        return typeTable.getOrCreateReferenceType(typeToken);
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

    public Clase getClassByString(String name){
        return clases.get(name);
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



}
