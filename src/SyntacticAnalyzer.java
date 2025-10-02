import TablaSimbolo.TablaSimbolo;
import exceptions.LexicalException;
import exceptions.SemanticException;
import exceptions.SyntacticException;
import model.*;

import java.io.IOException;
import auxiliar.Primeros;

class SyntacticAnalyzer {
    LexicAnalyzer lexicAnalyzer;
    Token actualToken;
    TablaSimbolo ts;
    Clase claseActual;
    Method methodActual;

    SyntacticAnalyzer(LexicAnalyzer lexA, TablaSimbolo tabla) throws LexicalException, IOException, SyntacticException, SemanticException {
        lexicAnalyzer = lexA;
        actualToken = lexicAnalyzer.getNextToken();
        ts = tabla;
        start();
    }

    void start() throws LexicalException, SyntacticException, IOException, SemanticException {
        listaClases();
        match("$");
    }

    void listaClases() throws LexicalException, SyntacticException, IOException, SemanticException {
        if(Primeros.pClase.contiene(actualToken.getId())){
            clase();
            listaClases();
        } else{

        }
    }

    void clase() throws LexicalException, SyntacticException, IOException, SemanticException {
        String modifier = modificadorOpcional();
        match("class");
        Token actualT = actualToken;
        match("idClass");
        Clase newClass = new Clase(actualT);
        ts.addClass(newClass);
        ts.setCurrentClass(newClass);
        ts.addModifierToCurrentClass(modifier);
        //parametroGenericoOpcional();
        Token parentToken = herenciaOpcional();
        ts.setParentOfCurrentClass(parentToken);
        match("{");
        listaMiembros();
        match("}");
    }

    void parametroGenericoOpcional() throws LexicalException, SyntacticException, IOException {
        if(actualToken.getId().equals("<")){
            match("<");
            match("idClass");
            match(">");
        }else{

        }
    }

    String modificadorOpcional() throws LexicalException, SyntacticException, IOException {
        String toReturn = null;
        if(actualToken.getId().equals("abstract")){
            match("abstract");
            toReturn = "abstract";
        }else if(actualToken.getId().equals("static")){
            match("static");
            toReturn = "static";
        }else if(actualToken.getId().equals("final")){
            match("final");
            toReturn = "final";
        }else{

        }
        return toReturn;
    }

    Token herenciaOpcional() throws LexicalException, SyntacticException, IOException {
        Token toReturn = new Token("idClass", "Object", 100000);
        if(actualToken.getId().equals("extends")){
            match("extends");
            toReturn = actualToken;
            match("idClass");
        }else{

        }
        return toReturn;
    }

    void listaMiembros() throws SyntacticException, LexicalException, IOException, SemanticException {

        if(Primeros.pListaMiembros.contiene(actualToken.getId())){
            miembro();
            listaMiembros();
        }else{

        }
    }

    void miembro() throws SyntacticException, LexicalException, IOException, SemanticException {
        if(Primeros.pMetodoOAtributo.contiene(actualToken.getId())){
            metodoOAtributo();
        }else if(Primeros.pConstructor.contiene(actualToken.getId())){
            constructor();
        }else if(Primeros.pMetodoConModificador.contiene(actualToken.getId())){
            metodoConModificador();
        }else{
            throw new SyntacticException(actualToken, "metodoOAtributo, constructor o metodoConModificador");
        }
    }

    Token tipoMetodo() throws SyntacticException, LexicalException, IOException {
        Token toReturn = null;
        if(Primeros.pTipo.contiene(actualToken.getId())){
            toReturn = tipo();
        }else if(actualToken.getId().equals("void")){
            toReturn = actualToken;
            match("void");
        }else{
            throw new SyntacticException(actualToken, "tipo o void");
        }
        return toReturn;
    }

    void metodoConModificador() throws LexicalException, SyntacticException, IOException, SemanticException {
        Token auxModifier = modificador();
        rMetodoConModificador(auxModifier);
    }

    void rMetodoConModificador(Token modifier) throws LexicalException, SyntacticException, IOException, SemanticException {
        Token typeMethod = tipoMetodo();

        Type type = ts.resolveType(typeMethod);
        Token currentMethod = actualToken;

        match("idMetVar");

        Method newMethod = new Method(modifier, type, currentMethod);
        ts.setCurrentMethod(newMethod);
        ts.addMethodToCurrentClass(newMethod);
        rMetodo();
    }

    Token modificador() throws LexicalException, SyntacticException, IOException, SemanticException {
        Token toReturn = actualToken;
        if(actualToken.getId().equals("abstract")){
            match("abstract");
        }else if(actualToken.getId().equals("static")){
            match("static");
        }else if(actualToken.getId().equals("final")){
            match("final");
        }else{
            throw new SyntacticException(actualToken, "abstract, static o final");
        }
        return toReturn;
    }

    void metodoOAtributo() throws LexicalException, SyntacticException, IOException, SemanticException {
        Token typeToken;
        Type type;
        Token actualT;

        if(Primeros.pTipo.contiene(actualToken.getId())){

            typeToken = tipo();
            type = ts.resolveType(typeToken);
            actualT = actualToken;
            match("idMetVar");
            rMetodoOAtributo(actualT, type);

        }else if(actualToken.getId().equals("void")){
            typeToken = actualToken;
            match("void");
            type = ts.resolveType(typeToken);

            actualT = actualToken;
            match("idMetVar");

            Method newMethod = new Method(null, type, actualT);

            ts.setCurrentMethod(newMethod);
            ts.addMethodToCurrentClass(newMethod);

            rMetodo();
        }else{
            throw new SyntacticException(actualToken, "tipo o void");
        }
    }

    void rMetodoOAtributo(Token actualT, Type type) throws LexicalException, SyntacticException, IOException, SemanticException {
        if(Primeros.pRMetodo.contiene(actualToken.getId())){

            Method newMethod = new Method(null, type, actualT);

            ts.setCurrentMethod(newMethod);
            ts.addMethodToCurrentClass(newMethod);

            rMetodo();

        }else if(actualToken.getId().equals(";")){

            Attribute newAttribute = new Attribute(actualT, type);
            ts.addAttributeToCurrentClass(newAttribute);

            match(";");
        }else{
            throw new SyntacticException(actualToken, "; o argumentos formales");
        }
    }

    void rMetodo() throws LexicalException, SyntacticException, IOException, SemanticException {
        argsFormales();
        bloqueOpcional();
    }

    void constructor() throws LexicalException, SyntacticException, IOException, SemanticException {

        match("public");
        Token name = actualToken;

        match("idClass");
        Constructor newC = new Constructor(name, ts.getCurrentClass());
        ts.associateConstructorToCurrentClass(newC);
        ts.setCurrentMethod(newC);

        argsFormales();
        bloque();
    }

    Token tipo() throws LexicalException, SyntacticException, IOException {
        Token toReturn = null;
        if(Primeros.pTipoPrimitivo.contiene(actualToken.getId())){
            toReturn = tipoPrimitivo();
        }else if(actualToken.getId().equals("idClass")){
            toReturn = actualToken;
            match("idClass");
            //parametroGenericoOpcional();
        }else{
            throw new SyntacticException(actualToken, "boolean, char, int o idClass");
        }
        return toReturn;
    }

    Token tipoPrimitivo() throws LexicalException, SyntacticException, IOException {
        Token toReturn = actualToken;
        if(actualToken.getId().equals("boolean")){
            match("boolean");
        }else if(actualToken.getId().equals("char")){
            match("char");
        }else if(actualToken.getId().equals("int")){
            match("int");
        }else{
            throw new SyntacticException(actualToken, "boolean, char o int");
        }
        return toReturn;
    }

    void argsFormales() throws LexicalException, SyntacticException, IOException, SemanticException {
        match("(");
        listaArgsFormalesOpcional();
        match(")");
    }

    void listaArgsFormalesOpcional() throws LexicalException, SyntacticException, IOException, SemanticException {
        if(Primeros.pListaArgsFormales.contiene(actualToken.getId())){
            listaArgsFormales();
        }else{

        }
    }

    void listaArgsFormales() throws LexicalException, SyntacticException, IOException, SemanticException {
        rArgFormalRListaArgsFormales();
    }

    void rArgFormalRListaArgsFormales() throws LexicalException, SyntacticException, IOException, SemanticException {
        argFormal();
        rListaArgsFormales();
    }

    void rListaArgsFormales() throws LexicalException, SyntacticException, IOException, SemanticException {
        if(actualToken.getId().equals(",")){
            match(",");
            argFormal();
            rListaArgsFormales();
        }else{

        }
    }

    void argFormal() throws LexicalException, SyntacticException, IOException, SemanticException {
        Token typeToken = tipo();

        Type type = ts.resolveType(typeToken);
        Token actualT = actualToken;

        match("idMetVar");

        Parameter newParam = new Parameter(actualT, type);
        ts.addParamToCurrentMethod(newParam);
    }

    void bloqueOpcional() throws LexicalException, SyntacticException, IOException {
        if(Primeros.pBloque.contiene(actualToken.getId())){
            bloque();
        }else if(actualToken.getId().equals(";")){
            match(";");
        }else{
            throw new SyntacticException(actualToken, "; o bloque");
        }
    }

    void bloque() throws LexicalException, SyntacticException, IOException {
        match("{");
        listaSentencias();
        match("}");
    }

    void listaSentencias() throws SyntacticException, LexicalException, IOException {

        if(Primeros.pSentencia.contiene(actualToken.getId())){
            sentencia();
            listaSentencias();
        }else{

        }
    }



    void sentencia() throws LexicalException, SyntacticException, IOException {
        if(actualToken.getId().equals(";")){
            match(";");
        }else if(Primeros.pAsignacionOLlamada.contiene(actualToken.getId())){
            asignacionOLlamada();
            match(";");
        }else if(Primeros.pVarLocal.contiene(actualToken.getId())){
            varLocal();
            match(";");
        }else if(Primeros.pReturn.contiene(actualToken.getId())){
            metReturn();
            match(";");
        }else if(Primeros.pIf.contiene(actualToken.getId())){
            metIf();
        }else if(Primeros.pWhile.contiene(actualToken.getId())){
            metWhile();
        }else if(Primeros.pBloque.contiene(actualToken.getId())){
            bloque();
        } else{
            throw new SyntacticException(actualToken, ";, asignación o llamada, variable local, return, if, while o bloque");
        }
    }

    void asignacionOLlamada() throws LexicalException, SyntacticException, IOException {
        expresion();
    }

    void varLocal() throws LexicalException, SyntacticException, IOException {
        match("var");
        match("idMetVar");
        match("=");
        expresionCompuesta();
    }

    void metReturn() throws LexicalException, SyntacticException, IOException {
        match("return");
        expresionOpcional();
    }

    void expresionOpcional() throws LexicalException, SyntacticException, IOException{
        if(Primeros.pExpresion.contiene(actualToken.getId())){
            expresion();
        }else{

        }
    }

    void metIf() throws LexicalException, SyntacticException, IOException {
        match("if");
        match("(");
        expresion();
        match(")");
        sentencia();
        ifConElse();
    }

    void ifConElse() throws LexicalException, SyntacticException, IOException {
        if(actualToken.getId().equals("else")){
            match("else");
            sentencia();
        }else{

        }
    }

    void metWhile() throws LexicalException, SyntacticException, IOException {
        match("while");
        match("(");
        expresion();
        match(")");
        sentencia();
    }

    void expresion() throws LexicalException, SyntacticException, IOException {
        expresionCompuesta();
        expresionConAsignacion();
    }

    //Logro expresion ternaria
    void expresionTernaria() throws LexicalException, SyntacticException, IOException {
        if(actualToken.getId().equals("?")){
            match("?");
            expresionCompuesta();
            match(":");
            expresionCompuesta();
        }else{

        }
    }

    void expresionConAsignacion() throws LexicalException, SyntacticException, IOException {
        if(Primeros.pOperadorAsignacion.contiene(actualToken.getId())){
            operadorAsignacion();
            expresionCompuesta();
        }else{

        }
    }

    void operadorAsignacion() throws LexicalException, SyntacticException, IOException {
        if(actualToken.getId().equals("=")){
            match("=");
        }else{
            throw new SyntacticException(actualToken, "=");
        }
    }

    void expresionCompuesta() throws LexicalException, SyntacticException, IOException {
        expresionBinaria();
        expresionTernaria();
    }

    void expresionBinaria() throws LexicalException, SyntacticException, IOException {
        expresionBasica();
        rExpresionBinaria();
    }

    void rExpresionBinaria() throws LexicalException, SyntacticException, IOException {
        if(Primeros.pOperadorBinario.contiene(actualToken.getId())){
            operadorBinario();
            expresionBinaria();
        }else{

        }
    }

    void rExpresionCompuesta() throws LexicalException, SyntacticException, IOException {
        if(Primeros.pOperadorBinario.contiene(actualToken.getId())){
            operadorBinario();
            expresionCompuesta();
        }else{

        }
    }

    void operadorBinario() throws SyntacticException, LexicalException, IOException {
        switch (actualToken.getId()) {
            case "||" -> match("||");
            case "&&" -> match("&&");
            case "==" -> match("==");
            case "!=" -> match("!=");
            case "<" -> match("<");
            case ">" -> match(">");
            case "<=" -> match("<=");
            case ">=" -> match(">=");
            case "+" -> match("+");
            case "-" -> match("-");
            case "*" -> match("*");
            case "/" -> match("/");
            case "%" -> match("%");
            default -> throw new SyntacticException(actualToken, "operador binario");
        }
    }

    void expresionBasica() throws SyntacticException, LexicalException, IOException {
        if(Primeros.pOperadorUnario.contiene(actualToken.getId())){
            operadorUnario();
            operando();
        }else if(Primeros.pOperando.contiene(actualToken.getId())){
            operando();
        }else{
            throw new SyntacticException(actualToken, "operador unario u operando");
        }
    }

    void operadorUnario() throws LexicalException, SyntacticException, IOException {
        switch (actualToken.getId()) {
            case "+" -> match("+");
            case "++" -> match("++");
            case "-" -> match("-");
            case "--" -> match("--");
            case "!" -> match("!");
            default -> throw new SyntacticException(actualToken, "operador unario");
        }
    }

    void operando() throws SyntacticException, LexicalException, IOException {
        if(Primeros.pPrimitivo.contiene(actualToken.getId())){
            primitivo();
        }else if(Primeros.pReferencia.contiene(actualToken.getId())){
            referencia();
        }else{
            throw new SyntacticException(actualToken, "operando");
        }
    }

    void primitivo() throws LexicalException, SyntacticException, IOException {
        switch (actualToken.getId()) {
            case "true" -> match("true");
            case "false" -> match("false");
            case "intLiteral" -> match("intLiteral");
            case "charLiteral" -> match("charLiteral");
            case "null" -> match("null");
            default -> throw new SyntacticException(actualToken, "true, false, intLiteral, charLiteral o null");
        }
    }

    void referencia() throws LexicalException, SyntacticException, IOException {
        primario();
        rReferencia();
    }

    void rReferencia() throws LexicalException, SyntacticException, IOException {
        if(Primeros.pVarOMetodoEncadenado.contiene(actualToken.getId())){
            varOMetodoEncadenado();
            rReferencia();
        }else{

        }
    }

    void primario() throws LexicalException, SyntacticException, IOException {
        if(actualToken.getId().equals("this")){
            match("this");
        }else if(actualToken.getId().equals("stringLiteral")){
            match("stringLiteral");
        }else if(Primeros.pAccesoVarOLlamadaMetodo.contiene(actualToken.getId())){
            accesoVarOLlamadaMetodo();
        }else if(Primeros.pLlamadaConstructor.contiene(actualToken.getId())){
            llamadaConstructor();
        }else if(Primeros.pLlamadaMetodoEstatico.contiene(actualToken.getId())){
            llamadaMetodoEstatico();
        }else if(Primeros.pExpresionParentizada.contiene(actualToken.getId())){
            expresionParentizada();
        }else{
            throw new SyntacticException(actualToken, "this, stringLiteral, idMetVar, new, idClass o (");
        }
    }

    void accesoVarOLlamadaMetodo() throws LexicalException, SyntacticException, IOException {
        match("idMetVar");
        rAccesoVarOLlamadaMetodo();
    }

    void rAccesoVarOLlamadaMetodo() throws LexicalException, SyntacticException, IOException {
        if(Primeros.pArgsActuales.contiene(actualToken.getId())){
            argsActuales();
        }else{

        }
    }

    void llamadaConstructor() throws LexicalException, SyntacticException, IOException {
        match("new");
        match("idClass");
        //parametroGenericoOpcional();
        argsActuales();
    }

    void expresionParentizada() throws LexicalException, SyntacticException, IOException {
        match("(");
        expresion();
        match(")");
    }

    void llamadaMetodoEstatico() throws LexicalException, SyntacticException, IOException {
            match("idClass");
        match(".");
        match("idMetVar");
        argsActuales();
    }

    void argsActuales() throws LexicalException, SyntacticException, IOException {
        match("(");
        listaExpsOpcional();
        match(")");
    }

    void listaExpsOpcional() throws LexicalException, SyntacticException, IOException {
        if(Primeros.pListaExps.contiene(actualToken.getId())){
            listaExps();
        }else{

        }
    }

    void listaExps() throws LexicalException, SyntacticException, IOException {
        expresion();
        rListaExps();
    }

    void rListaExps() throws LexicalException, SyntacticException, IOException {
        if(actualToken.getId().equals(",")){
            match(",");
            listaExps();
        }else{

        }
    }

    void varOMetodoEncadenado() throws LexicalException, SyntacticException, IOException {
        match(".");
        match("idMetVar");
        rVarOMetodoEncadenado();
    }

    void rVarOMetodoEncadenado() throws LexicalException, SyntacticException, IOException {
        if(Primeros.pArgsActuales.contiene(actualToken.getId())){
            argsActuales();
        }else{

        }
    }


    void match(String tokenName) throws IOException, LexicalException, SyntacticException {
        if(tokenName.equals(actualToken.getId())){
            actualToken = lexicAnalyzer.getNextToken();
        }else{
            throw new SyntacticException(actualToken, tokenName);
        }
    }
}
