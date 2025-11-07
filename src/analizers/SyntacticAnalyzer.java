package analizers;

import TablaSimbolo.TablaSimbolo;
import ast.*;
import exceptions.LexicalException;
import exceptions.SemanticException;
import exceptions.SyntacticException;
import model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import auxiliar.Primeros;

public class SyntacticAnalyzer {
    LexicAnalyzer lexicAnalyzer;
    Token actualToken;
    TablaSimbolo ts;
    Clase claseActual;
    Method methodActual;

    public SyntacticAnalyzer(LexicAnalyzer lexA) throws LexicalException, IOException, SyntacticException, SemanticException {
        lexicAnalyzer = lexA;
        actualToken = lexicAnalyzer.getNextToken();
        ts = TablaSimbolo.getInstance();
        start();
    }

    public void start() throws LexicalException, SyntacticException, IOException, SemanticException {
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
        Token parentToken = herenciaOpcional();
        ts.setParentOfCurrentClass(parentToken);
        match("{");
        listaMiembros();
        match("}");
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

        Method newMethod = new Method(modifier, type, currentMethod, ts.getCurrentClass());
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

            Method newMethod = new Method(null, type, actualT, ts.getCurrentClass());

            ts.setCurrentMethod(newMethod);
            ts.addMethodToCurrentClass(newMethod);

            rMetodo();
        }else{
            throw new SyntacticException(actualToken, "tipo o void");
        }
    }

    void rMetodoOAtributo(Token actualT, Type type) throws LexicalException, SyntacticException, IOException, SemanticException {
        if(Primeros.pRMetodo.contiene(actualToken.getId())){

            Method newMethod = new Method(null, type, actualT, ts.getCurrentClass());

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
        Bloque b = bloqueOpcional();
        ts.setCurrentBlock(null);
        ts.getCurrentMethod().addSentenceNodeToBlock(b);
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

    Bloque bloqueOpcional() throws LexicalException, SyntacticException, IOException, SemanticException {
        if(Primeros.pBloque.contiene(actualToken.getId())){
            return bloque();
        }else if(actualToken.getId().equals(";")){
            match(";");
        }else{
            throw new SyntacticException(actualToken, "; o bloque");
        }
        return new BloqueNulo();
    }

    Bloque bloque() throws LexicalException, SyntacticException, IOException, SemanticException {
        Token token = actualToken;
        match("{");
        Bloque b = new Bloque(token);
        if( ts.getCurrentBlock() != null ){
            b.setParent(ts.getCurrentBlock());
        }
        ts.setCurrentBlock(b);
        listaSentencias(b);
        match("}");
        ts.actualMethodHasBlock();
        if( ts.getCurrentBlock().getParent() != null ){
            ts.setCurrentBlock( ts.getCurrentBlock().getParent() );
        }
        return b;
    }

    void listaSentencias(Bloque b) throws SyntacticException, LexicalException, IOException, SemanticException {

        if(Primeros.pSentencia.contiene(actualToken.getId())){

            NodoSentencia nSentencia = sentencia();
            b.addSentence(nSentencia);
            listaSentencias(b);

        }else{

        }
    }

    NodoSentencia sentencia() throws LexicalException, SyntacticException, IOException, SemanticException {
        NodoSentencia newSentenceNode;
        if(actualToken.getId().equals(";")){
            match(";");
            return new NodoSentenciaVacia();
        }else if(Primeros.pAsignacionOLlamada.contiene(actualToken.getId())){
            newSentenceNode = asignacionOLlamada();
            if(newSentenceNode.getToken() == null){
                newSentenceNode.setToken(actualToken);
            }
            match(";");
            return newSentenceNode;
        }else if(Primeros.pVarLocal.contiene(actualToken.getId())){
            newSentenceNode = varLocal();
            match(";");
            return newSentenceNode;
        }else if(Primeros.pReturn.contiene(actualToken.getId())){
            newSentenceNode = metReturn();
            match(";");
            return newSentenceNode;
        }else if(Primeros.pIf.contiene(actualToken.getId())){
            return metIf();
        }else if(Primeros.pWhile.contiene(actualToken.getId())){
            return metWhile();
        }else if(Primeros.pBloque.contiene(actualToken.getId())){
            return bloque();
        } else{
            throw new SyntacticException(actualToken, ";, asignación o llamada, variable local, return, if, while o bloque");
        }
    }

    NodoSentencia asignacionOLlamada() throws LexicalException, SyntacticException, IOException, SemanticException {
        NodoAsignacionOLlamada newNodo = new NodoAsignacionOLlamada();
        expresion(newNodo);
        return newNodo;
    }

    void expresion(NodoAsignacionOLlamada nodo) throws LexicalException, SyntacticException, IOException, SemanticException {
        NodoExpresion nExpresion = expresionCompuesta();
        nodo.setLadoIzquierdo(nExpresion);
        if(actualToken.getLexeme().equals("=")) {
            nodo.setToken(actualToken);
            expresionConAsignacion(nodo);
        }else{

        }
    }

    void expresionConAsignacion(NodoAsignacionOLlamada nodo) throws LexicalException, SyntacticException, IOException, SemanticException {
        if(Primeros.pOperadorAsignacion.contiene(actualToken.getId())){
            operadorAsignacion();
            NodoExpresion expresionLadoDerecho = expresionCompuesta();
            nodo.setLadoDerecho(expresionLadoDerecho);
        }else{

        }
    }

    NodoExpresion expresion() throws LexicalException, SyntacticException, IOException, SemanticException {
        NodoExpresion nExpresion = expresionCompuesta();
        if(actualToken.getLexeme().equals("=")) {
            return expresionConAsignacion(nExpresion);
        }else{
            return nExpresion;
        }
    }

    NodoExpresion expresionConAsignacion(NodoExpresion expresionLadoIzq) throws LexicalException, SyntacticException, IOException, SemanticException {
        if(Primeros.pOperadorAsignacion.contiene(actualToken.getId())){
            Token tkOperador = actualToken;
            operadorAsignacion();
            NodoExpresion expresionLadoDer = expresionCompuesta();
            return new NodoExpresionAsignacion(tkOperador, expresionLadoIzq, expresionLadoDer);
        }else{
            return new NodoExpresionVacia();
        }
    }

    NodoSentencia varLocal() throws LexicalException, SyntacticException, IOException, SemanticException {
        match("var");
        Token tk = actualToken;
        match("idMetVar");
        match("=");
        NodoExpresion contenido = expresionCompuesta();
        return new NodoVarLocal(tk, contenido, ts.getCurrentMethod(),ts.getCurrentBlock());
    }

    NodoSentencia metReturn() throws LexicalException, SyntacticException, IOException, SemanticException {
        Token aToken = actualToken;
        match("return");
        NodoExpresion expresion = expresionOpcional();
        return new NodoReturn(aToken, expresion, (Method) ts.getCurrentMethod());
    }

    NodoExpresion expresionOpcional() throws LexicalException, SyntacticException, IOException, SemanticException {
        if(Primeros.pExpresion.contiene(actualToken.getId())){
            return expresion();
        }else{
            return new NodoExpresionVacia();
        }
    }

    NodoSentencia metIf() throws LexicalException, SyntacticException, IOException, SemanticException {
        Token token = actualToken;
        match("if");
        match("(");
        NodoExpresion condicion = expresion();
        match(")");
        NodoSentencia sentenciaThen = sentencia();
        NodoSentencia sentenciaElse = ifConElse();
        return new NodoIf(token, condicion, sentenciaThen, sentenciaElse);
    }

    NodoSentencia ifConElse() throws LexicalException, SyntacticException, IOException, SemanticException {
        if(actualToken.getId().equals("else")){
            match("else");
            return sentencia();
        }else{
            return new NodoSentenciaVacia();
        }
    }

    NodoSentencia metWhile() throws LexicalException, SyntacticException, IOException, SemanticException {
        Token aToken = actualToken;
        match("while");
        match("(");
        NodoExpresion expresionWhile = expresion();
        match(")");
        NodoSentencia sentenciaWhile = sentencia();
        return new NodoWhile(aToken, expresionWhile, sentenciaWhile);
    }


    void operadorAsignacion() throws LexicalException, SyntacticException, IOException {
        if(actualToken.getId().equals("=")){
            match("=");
        }else{
            throw new SyntacticException(actualToken, "=");
        }
    }

    NodoExpresion expresionCompuesta() throws LexicalException, SyntacticException, IOException, SemanticException {
        NodoExpresion expBasica = expresionBasica();
        if(Primeros.pOperadorBinario.contiene(actualToken.getId())){
            return rExpresionCompuesta(expBasica);
        }else{
            return expBasica;
        }
    }

    NodoExpresion rExpresionCompuesta(NodoExpresion expLadoIzq) throws LexicalException, SyntacticException, IOException, SemanticException {
        if(Primeros.pOperadorBinario.contiene(actualToken.getId())){
            Token operador = operadorBinario();
            NodoExpresion expLadoDer = expresionCompuesta();
            return new NodoExpresionBinaria(expLadoIzq, operador, expLadoDer);
        }else{
            return new NodoExpresionVacia();
        }
    }

    Token operadorBinario() throws SyntacticException, LexicalException, IOException {
        Token tk = actualToken;
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
        return tk;
    }

    NodoExpresion expresionBasica() throws SyntacticException, LexicalException, IOException, SemanticException {
        if(Primeros.pOperadorUnario.contiene(actualToken.getId())){
            Token operadorU = operadorUnario();
            NodoOperando nodoOperando = (NodoOperando) operando();
            return new NodoExpresionUnaria(operadorU, nodoOperando);
        }else if(Primeros.pOperando.contiene(actualToken.getId())){
            return operando();
        }else{
            throw new SyntacticException(actualToken, "operador unario u operando");
        }
    }

    Token operadorUnario() throws LexicalException, SyntacticException, IOException {
        Token tokenToReturn = actualToken;
        switch (actualToken.getId()) {
            case "+" -> match("+");
            case "++" -> match("++");
            case "-" -> match("-");
            case "--" -> match("--");
            case "!" -> match("!");
            default -> throw new SyntacticException(actualToken, "operador unario");
        }
        return tokenToReturn;
    }

    NodoOperando operando() throws SyntacticException, LexicalException, IOException, SemanticException {
        if(Primeros.pPrimitivo.contiene(actualToken.getId())){
            return primitivo();
        }else if(Primeros.pReferencia.contiene(actualToken.getId())){
            return referencia();
        }else{
            throw new SyntacticException(actualToken, "operando");
        }
    }

    NodoPrimitivo primitivo() throws LexicalException, SyntacticException, IOException {
        NodoPrimitivo nodoPrimitivo;
        Token actualtk = actualToken;
        switch (actualToken.getId()) {
            case "true" -> {
                match("true");
                nodoPrimitivo = new NodoBoolean(actualtk);
            }
            case "false" -> {
                match("false");
                nodoPrimitivo = new NodoBoolean(actualtk);
            }
            case "intLiteral" -> {
                match("intLiteral");
                nodoPrimitivo = new NodoInt(actualtk);
            }
            case "charLiteral" -> {
                match("charLiteral");
                nodoPrimitivo = new NodoChar(actualtk);
            }
            case "null" -> {
                match("null");
                nodoPrimitivo = new NodoNull(actualtk);
            }
            default -> throw new SyntacticException(actualToken, "true, false, intLiteral, charLiteral o null");
        }
        return nodoPrimitivo;
    }

    NodoOperando referencia() throws LexicalException, SyntacticException, IOException, SemanticException {
        NodoPrimario nodoPrim = primario();
        rReferencia(nodoPrim);
        return nodoPrim;
    }

    void rReferencia(NodoPrimario nodoPrim) throws LexicalException, SyntacticException, IOException, SemanticException {
        if(Primeros.pVarOMetodoEncadenado.contiene(actualToken.getId())){
            NodoPrimario nodoEncadenado = varOMetodoEncadenado(nodoPrim);
            rReferencia(nodoEncadenado);
        }else{

        }
    }

    NodoPrimario primario() throws LexicalException, SyntacticException, IOException, SemanticException {
        NodoPrimario nodoPrimario;
        Token tk = actualToken;
        if(actualToken.getId().equals("this")){
            match("this");
            Type thisType = ts.resolveType(ts.getCurrentClass().getToken());
            ((ReferenceType)thisType).setAssociatedClass(ts.getCurrentClass());
            nodoPrimario = new NodoThis(tk, thisType, ts.getCurrentMethod() );
        }else if(actualToken.getId().equals("stringLiteral")){
            match("stringLiteral");
            nodoPrimario = new NodoString(tk);
        }else if(Primeros.pAccesoVarOLlamadaMetodo.contiene(actualToken.getId())){
            nodoPrimario = accesoVarOLlamadaMetodo();
        }else if(Primeros.pLlamadaConstructor.contiene(actualToken.getId())){
            nodoPrimario = llamadaConstructor();
        }else if(Primeros.pLlamadaMetodoEstatico.contiene(actualToken.getId())){
            nodoPrimario = llamadaMetodoEstatico();
        }else if(Primeros.pExpresionParentizada.contiene(actualToken.getId())){
            nodoPrimario = expresionParentizada();
        }else{
            throw new SyntacticException(actualToken, "this, stringLiteral, idMetVar, new, idClass o (");
        }
        return nodoPrimario;
    }

    NodoPrimario accesoVarOLlamadaMetodo() throws LexicalException, SyntacticException, IOException, SemanticException {
        Token tk = actualToken;
        match("idMetVar");
        return rAccesoVarOLlamadaMetodo(tk);
    }

    NodoPrimario rAccesoVarOLlamadaMetodo(Token tk) throws LexicalException, SyntacticException, IOException, SemanticException {
        NodoPrimario toR;
        if(Primeros.pArgsActuales.contiene(actualToken.getId())){
            List<NodoExpresion> argsList = argsActuales();
            toR = new NodoLlamadaMetodo(tk, argsList, ts.getCurrentClass(), ts.getCurrentMethod());
        }else{
            toR = new NodoVar(tk, ts.getCurrentClass(), ts.getCurrentMethod(), ts.getCurrentBlock());
        }
        return toR;
    }

    NodoPrimario llamadaConstructor() throws LexicalException, SyntacticException, IOException, SemanticException {
        match("new");
        Token tk = actualToken;
        match("idClass");

        List<NodoExpresion> list = argsActuales();

        return new NodoLlamadaConstructor(tk, list);
    }

    NodoPrimario expresionParentizada() throws LexicalException, SyntacticException, IOException, SemanticException {
        Token tk = actualToken;
        match("(");
        NodoExpresion nodoExpresion = expresion();
        NodoExpresionParentizada nodo = new NodoExpresionParentizada(actualToken, nodoExpresion);
        match(")");
        return nodo;
    }

    NodoPrimario llamadaMetodoEstatico() throws LexicalException, SyntacticException, IOException, SemanticException {
        Token className = actualToken;
        match("idClass");
        match(".");
        Token methodName = actualToken;
        match("idMetVar");
        List<NodoExpresion> args = argsActuales();
        return new NodoLlamadaMetodoEstatico(methodName, args, className);
    }

    //IMPORTANTE VER SI se añaden correctamente los parametros, es importante el orden
    List<NodoExpresion> argsActuales() throws LexicalException, SyntacticException, IOException, SemanticException {
        match("(");
        List<NodoExpresion> argsList = listaExpsOpcional();
        match(")");
        return argsList;
    }

    List<NodoExpresion> listaExpsOpcional() throws LexicalException, SyntacticException, IOException, SemanticException {
        List<NodoExpresion> listR = new ArrayList<>();
        if(Primeros.pListaExps.contiene(actualToken.getId())){
            listaExps(listR);
        }else{

        }
        return listR;
    }

    void listaExps(List<NodoExpresion> list) throws LexicalException, SyntacticException, IOException, SemanticException {
        NodoExpresion nodoExp = expresion();
        list.add(nodoExp);
        rListaExps(list);
    }

    void rListaExps(List<NodoExpresion> list) throws LexicalException, SyntacticException, IOException, SemanticException {
        if(actualToken.getId().equals(",")){
            match(",");
            listaExps(list);
        }else{

        }
    }

    NodoPrimario varOMetodoEncadenado(NodoPrimario nodoPrim) throws LexicalException, SyntacticException, IOException, SemanticException {
        match(".");
        Token tk = actualToken;
        match("idMetVar");
        NodoPrimario nodoEncadenado = rVarOMetodoEncadenado(tk);
        nodoPrim.setEncadenado(nodoEncadenado);
        return nodoEncadenado;
    }

    NodoPrimario rVarOMetodoEncadenado(Token tk) throws LexicalException, SyntacticException, IOException, SemanticException {
        NodoPrimario newEncadenado;
        if(Primeros.pArgsActuales.contiene(actualToken.getId())){
            List<NodoExpresion> argsList = argsActuales();
            newEncadenado = new NodoLlamadaMetodo(tk, argsList, ts.getCurrentClass(), ts.getCurrentMethod());
        }else{
            newEncadenado = new NodoVar(tk, ts.getCurrentClass(), ts.getCurrentMethod(), ts.getCurrentBlock());
        }
        return newEncadenado;
    }


    void match(String tokenName) throws IOException, LexicalException, SyntacticException {
        if(tokenName.equals(actualToken.getId())){
            actualToken = lexicAnalyzer.getNextToken();
        }else{
            throw new SyntacticException(actualToken, tokenName);
        }
    }
}
