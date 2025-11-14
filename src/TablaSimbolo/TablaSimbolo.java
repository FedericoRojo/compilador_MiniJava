package TablaSimbolo;

import ast.*;
import exceptions.SemanticException;
import model.*;
import sourcemanager.GeneratorManager;

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

    public void generate() throws SemanticException {
        initGenerate();
        for(Clase c: clases.values()){
            c.generate();
        }
    }


    public void initGenerate() throws SemanticException {

        GeneratorManager generator = GeneratorManager.getInstance();
        generator.gen(".CODE");
        Clase mainClass = findMainClass();
        generator.gen("PUSH lblMetMain@"+mainClass.getName());
        generator.gen("CALL");
        generator.gen("HALT");
        generator.gen("simple_heap_init: RET 0 ; Retorna inmediatamenete");
        generator.gen("simple_malloc: LOADFP ; Inicializacion unidad");
        generator.gen("LOADSP");
        generator.gen("STOREFP ;Finaliza inicializacion del RA");
        generator.gen("LOADHL ;hl");
        generator.gen("DUP ;hl");
        generator.gen("PUSH 1 ;1");
        generator.gen("ADD ;hl+1");
        generator.gen("STORE 4 ;Guarda el resultado(un puntero a base del bloque)");
        generator.gen("LOAD 3 ;Carga cantidad de celdas a alojar(parametro)");
        generator.gen("ADD");
        generator.gen("STOREHL ;Mueve el heap limit (hl)");
        generator.gen("STOREFP");
        generator.gen("RET 1 ;Retornaeliminandoelparametro");
        generator.gen("");
        generator.gen("");
    }

    private Clase findMainClass() throws SemanticException {
        Token tk = new Token("1000", "main", 1000);
        for (Clase c : clases.values()) {
            Method main = c.getMethod(tk);
            if (main != null) {
                return c;
            }
        }
        throw new SemanticException(tk, "No se encontró un método static void main(String[])");
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
        debugIMethod.addSentenceNodeToBlock(new BloqueDebugPrint());
        debugIMethod.setOffset(1);
        cObject.addMethod( debugIMethod );
        cObject.setLastMethodOffset(2);


        Method mread = new Method( new Token("-2", "static", -2),
                resolveType(intType),
                new Token("-2", "read", -2),
                cSystem);
        mread.setHasBlock(true);
        mread.addSentenceNodeToBlock(new BloqueRead());
        cSystem.addMethod( mread );

        Method printBMethod = new Method( new Token("-2", "static", -2),
                            resolveType(voidType),
                            new Token("-2", "printB", -2),
                cSystem);
        printBMethod.addParameter(new Parameter(new Token("-2", "b", -2), resolveType(booleanType) ));
        printBMethod.addSentenceNodeToBlock( new BloquePrintB() );
        printBMethod.setHasBlock(true);
        cSystem.addMethod(printBMethod);


        Method printCMethod = new Method( new Token("-2", "static", -2),
                                        resolveType(voidType),
                                        new Token("-2", "printC", -2), cSystem);
        printCMethod.addParameter(new Parameter(new Token("-2", "c", -2), resolveType(charType) ));
        printCMethod.setHasBlock(true);
        printCMethod.addSentenceNodeToBlock(new BloquePrintC());
        cSystem.addMethod(printCMethod);


        Method printIMethod = new Method( new Token("-2", "static", -2),
                resolveType(voidType),
                new Token("-2", "printI", -2), cSystem);
        printIMethod.addParameter(new Parameter(new Token("-2", "i", -2), resolveType(intType) ));
        printIMethod.setHasBlock(true);
        printIMethod.addSentenceNodeToBlock(new BloquePrintI());
        cSystem.addMethod(printIMethod);


        Method printSMethod = new Method( new Token("-2", "static", -2),
                resolveType(voidType),
                new Token("-2", "printS", -2), cSystem);
        printSMethod.addParameter(new Parameter(new Token("-2", "s", -2), resolveType(stringType) ));
        printSMethod.setHasBlock(true);
        printSMethod.addSentenceNodeToBlock(new BloquePrintS());
        cSystem.addMethod(printSMethod);

        Method printlnMethod = new Method( new Token("-2", "static", -2),
                resolveType(voidType),
                new Token("-2", "println", -2), cSystem);
        printlnMethod.setHasBlock(true);
        printlnMethod.addSentenceNodeToBlock(new BloquePrintLn());
        cSystem.addMethod(printlnMethod);


        Method printBlnMethod = new Method( new Token("-2", "static", -2),
                resolveType(voidType),
                new Token("-2", "printBln", -2), cSystem);
        printBlnMethod.addParameter(new Parameter(new Token("-2", "b", -2), resolveType(booleanType) ));
        printBlnMethod.setHasBlock(true);
        printBlnMethod.addSentenceNodeToBlock(new BloquePrintBLn());
        cSystem.addMethod(printBlnMethod);


        Method printClnMethod = new Method( new Token("-2", "static", -2),
                resolveType(voidType),
                new Token("-2", "printCln", -2), cSystem);
        printClnMethod.addParameter(new Parameter(new Token("-2", "c", -2), resolveType(charType) ));
        printClnMethod.setHasBlock(true);
        printClnMethod.addSentenceNodeToBlock(new BloquePrintCLn());
        cSystem.addMethod(printClnMethod);


        Method printIlnMethod = new Method( new Token("-2", "static", -2),
                resolveType(voidType),
                new Token("-2", "printIln", -2), cSystem);
        printIlnMethod.addParameter(new Parameter(new Token("-2", "i", -2), resolveType(intType) ));
        printIlnMethod.addSentenceNodeToBlock(new BloquePrintILn());
        printIlnMethod.setHasBlock(true);
        cSystem.addMethod(printIlnMethod);


        Method printSlnMethod = new Method( new Token("-2", "static", -2),
                resolveType(voidType),
                new Token("-2", "printSln", -2), cSystem);
        printSlnMethod.addParameter(new Parameter(new Token("-2", "s", -2), resolveType(stringType) ));
        printSlnMethod.setHasBlock(true);
        printSlnMethod.addSentenceNodeToBlock(new BloquePrintSLn());
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
