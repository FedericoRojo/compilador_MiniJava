import exceptions.LexicalException;
import exceptions.SyntacticException;
import model.Token;
import java.io.IOException;
import auxiliar.Primeros;

class SyntacticAnalyzer {
    LexicAnalyzer lexicAnalyzer;
    Token actualToken;

    SyntacticAnalyzer(LexicAnalyzer lexA) throws LexicalException, IOException, SyntacticException {
        lexicAnalyzer = lexA;
        actualToken = lexicAnalyzer.getNextToken();
        start();
    }

    void start() throws LexicalException, SyntacticException, IOException {
        listaClases();
        match("$");
    }

    void listaClases() throws LexicalException, SyntacticException, IOException {
        if(Primeros.pClase.contiene(actualToken.getId())){
            clase();
            listaClases();
        } else{

        }
    }

    void clase() throws LexicalException, SyntacticException, IOException {
        modificadorOpcional();
        match("class");
        match("idClase");
        herenciaOpcional();
        match("{");
        listaMiembros();
        match("}");
    }

    void modificadorOpcional() throws LexicalException, SyntacticException, IOException {
        if(actualToken.getId().equals("abstract")){
            match("abstract");
        }else if(actualToken.getId().equals("static")){
            match("static");
        }else if(actualToken.getId().equals("final")){
            match("final");
        }else{

        }
    }

    void herenciaOpcional() throws LexicalException, SyntacticException, IOException {
        if(actualToken.getId().equals("extends")){
            match("extends");
            match("idClase");
        }else{

        }
    }

    void listaMiembros() throws SyntacticException, LexicalException, IOException {
        if(Primeros.pListaMiembros.contiene(actualToken.getId())){
            miembro();
            listaMiembros();
        }else{

        }
    }

    void miembro() throws SyntacticException, LexicalException, IOException {
        if(Primeros.pMetodoOAtributo.contiene(actualToken.getId())){
            metodoOAtributo();
        }else if(Primeros.pConstructor.contiene(actualToken.getId())){
            constructor();
        }else if(Primeros.pMetodoConModificador.contiene(actualToken.getId())){
            metodoConModificador();
        }else{
            throw new SyntacticException(actualToken, actualToken.getLexeme());
        }
    }

    void tipoMetodo() throws SyntacticException, LexicalException, IOException {
        if(Primeros.pTipo.contiene(actualToken.getId())){
            tipo();
        }else if(actualToken.getId().equals("void")){
            match("void");
        }else{
            throw new SyntacticException(actualToken, actualToken.getLexeme());
        }
    }

    void metodoConModificador() throws LexicalException, SyntacticException, IOException {
        modificador();
        rMetodoConModificador();
    }

    void rMetodoConModificador() throws LexicalException, SyntacticException, IOException {
        tipoMetodo();
        match("idMetVar");
        rMetodo();
    }

    void modificador() throws LexicalException, SyntacticException, IOException {
        if(actualToken.getId().equals("abstract")){
            match("abstract");
        }else if(actualToken.getId().equals("static")){
            match("static");
        }else if(actualToken.getId().equals("final")){
            match("final");
        }else{
            throw new SyntacticException(actualToken, actualToken.getLexeme());
        }
    }

    void metodoOAtributo() throws LexicalException, SyntacticException, IOException {
        if(Primeros.pTipo.contiene(actualToken.getId())){
            tipo();
            match("idMetVar");
            rMetodoOAtributo();
        }else if(actualToken.getId().equals("void")){
            match("void");
            match("idMetVar");
            rMetodo();
        }else{
            throw new SyntacticException(actualToken, actualToken.getLexeme());
        }
    }

    void rMetodoOAtributo() throws LexicalException, SyntacticException, IOException {
        if(Primeros.pRMetodo.contiene(actualToken.getId())){
            rMetodo();
        }else if(actualToken.getId().equals(";")){
            match(";");
        }else{
            throw new SyntacticException(actualToken, actualToken.getLexeme());
        }
    }

    void rMetodo() throws LexicalException, SyntacticException, IOException {
        argsFormales();
        bloqueOpcional();
    }

    void constructor() throws LexicalException, SyntacticException, IOException {
        match("public");
        match("idClase");
        argsFormales();
        bloque();
    }

    void tipo() throws LexicalException, SyntacticException, IOException {
        if(Primeros.pTipoPrimitivo.contiene(actualToken.getId())){
            tipoPrimitivo();
        }else if(actualToken.getId().equals("idClase")){
            match("idClase");
        }else{
            throw new SyntacticException(actualToken, actualToken.getLexeme());
        }
    }

    void tipoPrimitivo() throws LexicalException, SyntacticException, IOException {
        if(actualToken.getId().equals("boolean")){
            match("boolean");
        }else if(actualToken.getId().equals("char")){
            match("char");
        }else if(actualToken.getId().equals("int")){
            match("int");
        }else{
            throw new SyntacticException(actualToken, actualToken.getLexeme());
        }
    }

    void argsFormales() throws LexicalException, SyntacticException, IOException {
        match("(");
        listaArgsFormalesOpcional();
        match(")");
    }

    void listaArgsFormalesOpcional() throws LexicalException, SyntacticException, IOException {
        if(Primeros.pListaArgsFormales.contiene(actualToken.getId())){
            listaArgsFormales();
        }else{

        }
    }

    void listaArgsFormales() throws LexicalException, SyntacticException, IOException {
        rArgFormalRListaArgsFormales();
    }

    void rArgFormalRListaArgsFormales() throws LexicalException, SyntacticException, IOException {
        argFormal();
        rListaArgsFormales();
    }

    void rListaArgsFormales() throws LexicalException, SyntacticException, IOException {
        if(actualToken.getId().equals(",")){
            match(",");
            argFormal();
            rListaArgsFormales();
        }else{

        }
    }

    void argFormal() throws LexicalException, SyntacticException, IOException {
        tipo();
        match("idMetVar");
    }

    void bloqueOpcional() throws LexicalException, SyntacticException, IOException {
        if(Primeros.pBloque.contiene(actualToken.getId())){
            bloque();
        }else{

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
            throw new SyntacticException(actualToken, actualToken.getLexeme());
        }
    }






    void sentencia() throws LexicalException, SyntacticException, IOException {
        if(actualToken.getId().equals(";")){
            match(";");
        }else if(actualToken.getId().equals(";")){
            asignacionOLlamada();
        }else if(actualToken.getId().equals(";")){
            varLocal();
        }else if(actualToken.getId().equals(";")){
            metReturn();
        }else if(actualToken.getId().equals(";")){
            metIf();
        }else if(actualToken.getId().equals(";")){
            metWhile();
        }else if(actualToken.getId().equals(";")){
            bloque();
        } else{
            throw new SyntacticException(actualToken, actualToken.getLexeme());
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

    void expresionConAsignacion() throws LexicalException, SyntacticException, IOException {
        if(Primeros.pOperadorAsignacion.contiene(actualToken.getId())){
            operadorAsignacion();
            expresionCompuesta();
        }else{

        }
    }

    void operadorAsignacion() throws LexicalException, SyntacticException, IOException {
        if(actualToken.getId().equals("==")){
            match("==");
        }else if(actualToken.getId().equals("+=")){
            match("+=");
        }else if(actualToken.getId().equals("-=")){
            match("-=");
        }else{
            throw new SyntacticException(actualToken, actualToken.getLexeme());
        }
    }

    void expresionCompuesta() throws LexicalException, SyntacticException, IOException {
        rExpresionBasicaConExpresionCompuesta();
    }

    void rExpresionBasicaConExpresionCompuesta() throws LexicalException, SyntacticException, IOException {
        expresionBasica();
        rExpresionCompuesta();
    }

    void rExpresionCompuesta() throws LexicalException, SyntacticException, IOException {
        if(Primeros.pOperadorBinario.contiene(actualToken.getId())){
            operadorBinario();
            rExpresionBasicaConExpresionCompuesta();
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
            default -> throw new SyntacticException(actualToken, actualToken.getLexeme());
        }
    }

    void expresionBasica() throws SyntacticException, LexicalException, IOException {
        if(Primeros.pOperadorUnario.contiene(actualToken.getId())){
            operadorUnario();
            operando();
        }else if(Primeros.pOperando.contiene(actualToken.getId())){
            operando();
        }else{
            throw new SyntacticException(actualToken, actualToken.getLexeme());
        }
    }

    void operadorUnario() throws LexicalException, SyntacticException, IOException {
        switch (actualToken.getId()) {
            case "+" -> match("+");
            case "++" -> match("++");
            case "-" -> match("-");
            case "--" -> match("--");
            case "!" -> match("!");
            default -> throw new SyntacticException(actualToken, actualToken.getLexeme());
        }
    }

    void operando() throws SyntacticException, LexicalException, IOException {
        if(Primeros.pPrimitivo.contiene(actualToken.getId())){
            primitivo();
        }else if(Primeros.pReferencia.contiene(actualToken.getId())){
            referencia();
        }else{
            throw new SyntacticException(actualToken, actualToken.getLexeme());
        }
    }

    void primitivo() throws LexicalException, SyntacticException, IOException {
        switch (actualToken.getId()) {
            case "true" -> match("true");
            case "false" -> match("false");
            case "intLiteral" -> match("intLiteral");
            case "charLiteral" -> match("charLiteral");
            case "null" -> match("null");
            default -> throw new SyntacticException(actualToken, actualToken.getLexeme());
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
        }else if(Primeros.pConstructor.contiene(actualToken.getId())){
            llamadaConstructor();
        }else if(Primeros.pLlamadaMetodoEstatico.contiene(actualToken.getId())){
            llamadaMetodoEstatico();
        }else if(Primeros.pExpresionParentizada.contiene(actualToken.getId())){
            expresionParentizada();
        }else{
            throw new SyntacticException(actualToken, actualToken.getLexeme());
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
        match("idClase");
        argsActuales();
    }

    void expresionParentizada() throws LexicalException, SyntacticException, IOException {
        match("(");
        expresion();
        match(")");
    }

    void llamadaMetodoEstatico() throws LexicalException, SyntacticException, IOException {
        match("idClase");
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
        rExpresionRListaExps();
    }

    void rExpresionRListaExps() throws LexicalException, SyntacticException, IOException {
        expresion();
        rListaExps();
    }

    void rListaExps() throws LexicalException, SyntacticException, IOException {
        if(actualToken.getId().equals(",")){
            match(",");
            rExpresionRListaExps();
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
