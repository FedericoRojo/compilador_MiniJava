package TablaSimbolo;

import exceptions.SemanticException;
import model.*;

import java.util.HashMap;

public class TablaSimbolo {
    private static TablaSimbolo instance;
    HashMap<String, Clase> clases;
    TypeTable typeTable;
    Clase currentClass;
    GenericMethod currentMethod;


    private TablaSimbolo() throws SemanticException {
        clases = new HashMap<>();
        typeTable = new TypeTable();
        currentClass = null;
        currentMethod = null;
        initBaseClasses();
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


    private void initBaseClasses() throws SemanticException {
        Clase cObject = new Clase( new Token("-1", "Object", -1));
        Clase cSystem = new Clase( new Token("-2", "System", -2));
        Clase cString = new Clase( new Token("-3", "String", -3));


        Token voidType = new Token("-1", "void", -1);
        Token intType = new Token("-1", "int", -1);
        Token booleanType = new Token("-1", "boolean", -1);
        Token charType = new Token("-1", "char", -1);
        Token stringType = new Token("-1", "String", -1);

        Method debugIMethod = new Method( new Token("-1", "static", -1),
                resolveType(voidType),
                new Token("-1", "debugPrint", -1));
        debugIMethod.addParameter(new Parameter(new Token("-2", "i", -2), resolveType(intType)));
        cObject.addMethod( debugIMethod );


        cSystem.addMethod( new Method( new Token("-2", "static", -2),
                           resolveType(intType),
                           new Token("-2", "read", -2)));

        Method printBMethod = new Method( new Token("-2", "static", -2),
                            resolveType(voidType),
                            new Token("-2", "printB", -2));
        printBMethod.addParameter(new Parameter(new Token("-2", "b", -2), resolveType(booleanType) ));
        cSystem.addMethod(printBMethod);

        Method printCMethod = new Method( new Token("-2", "static", -2),
                                        resolveType(voidType),
                                        new Token("-2", "printC", -2));
        printCMethod.addParameter(new Parameter(new Token("-2", "c", -2), resolveType(charType) ));
        cSystem.addMethod(printCMethod);


        Method printIMethod = new Method( new Token("-2", "static", -2),
                resolveType(voidType),
                new Token("-2", "printI", -2));
        printIMethod.addParameter(new Parameter(new Token("-2", "i", -2), resolveType(intType) ));
        cSystem.addMethod(printIMethod);


        Method printSMethod = new Method( new Token("-2", "static", -2),
                resolveType(voidType),
                new Token("-2", "printS", -2));
        printSMethod.addParameter(new Parameter(new Token("-2", "s", -2), resolveType(stringType) ));
        cSystem.addMethod(printSMethod);


        Method printlnMethod = new Method( new Token("-2", "static", -2),
                resolveType(voidType),
                new Token("-2", "println", -2));
        cSystem.addMethod(printlnMethod);


        Method printBlnMethod = new Method( new Token("-2", "static", -2),
                resolveType(voidType),
                new Token("-2", "printBln", -2));
        printBlnMethod.addParameter(new Parameter(new Token("-2", "b", -2), resolveType(booleanType) ));
        cSystem.addMethod(printBlnMethod);


        Method printClnMethod = new Method( new Token("-2", "static", -2),
                resolveType(voidType),
                new Token("-2", "printCln", -2));
        printClnMethod.addParameter(new Parameter(new Token("-2", "c", -2), resolveType(charType) ));
        cSystem.addMethod(printClnMethod);


        Method printIlnMethod = new Method( new Token("-2", "static", -2),
                resolveType(voidType),
                new Token("-2", "printIln", -2));
        printIlnMethod.addParameter(new Parameter(new Token("-2", "i", -2), resolveType(intType) ));
        cSystem.addMethod(printIlnMethod);


        Method printSlnMethod = new Method( new Token("-2", "static", -2),
                resolveType(voidType),
                new Token("-2", "printSln", -2));
        printSlnMethod.addParameter(new Parameter(new Token("-2", "s", -2), resolveType(stringType) ));
        cSystem.addMethod(printSlnMethod);

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
