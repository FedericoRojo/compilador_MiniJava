package auxiliar;

import java.util.Set;
import java.util.HashSet;

public enum Primeros {
    pInicial(Set.of("$", "class", "abstract", "static", "final")),
    pListaClases(Set.of("class", "abstract", "static", "final")),
    pClase(Set.of("class", "abstract", "static", "final")),
    pModificadorOpcional(Set.of("abstract", "static", "final")),
    pHerenciaOpcional(Set.of("extends")),
    pListaMiembros(Set.of("idClass", "boolean", "char", "int", "public", "abstract", "static", "final", "void")),

    pMiembro(Set.of("idClass", "boolean", "char", "int", "public", "abstract", "static", "final", "void")),
    pMetodoConModificador(Set.of("abstract", "static", "final")),
    pRMetodoConModficador(Set.of("void", "boolean", "char", "int")),
    pMetodoOAtributo(Set.of("idClass", "boolean", "char", "int", "void")),
    pR_MetodoOAtributo(Set.of("(", ";")),
    pRMetodo(Set.of("(")),
    pTipoMetodo(Set.of("void", "idClass", "boolean", "char", "int")),
    pMoficador(Set.of("abstract", "static", "final")),

    pConstructor(Set.of("public")),

    pTipo(Set.of("boolean", "char", "int", "idClass")),
    pArgsFormales(Set.of("(")),
    pListaArgsFormalesOpcional(Set.of( "boolean", "char", "int", "idClass")),
    pListaArgsFormales(Set.of("boolean", "char", "int", "idClass")),
    pRListaArgsFormales(Set.of(",")),
    pArgFormal(Set.of("boolean", "char", "int", "idClass")),
    pBloqueOpcional(Set.of( "{")),
    pBloque(Set.of("{")),

    pListaSentencias(Set.of( ";", "+", "++", "-", "–", "!", "true", "false", "intLiteral", "charLiteral",
            "null", "this", "stringLiteral", "idMetVar", "new", "idClass", "(", "var", "return", "if", "while", "{")),
    pSentencia(Set.of(";", "+", "++", "-", "–", "!", "true", "false", "intLiteral", "charLiteral", "null",
            "this", "stringLiteral", "idMetVar", "new", "idClass", "(", "var", "return", "if", "while", "{")),
    pAsignacionOLlamada(Set.of("+", "++", "-", "–", "!", "true", "false", "intLiteral", "charLiteral", "null",
            "this", "stringLiteral", "idMetVar", "new", "idClass", "(")),
    pVarLocal(Set.of("var")),
    pReturn(Set.of("return")),
    pExpresionOpcional(Set.of("+", "++", "-", "–", "!", "true", "false", "intLiteral", "charLiteral", "null",
            "this", "stringLiteral", "idMetVar", "new", "idClass", "(")),
    pIf(Set.of("if")),
    pIfConElse(Set.of("else")),
    pWhile(Set.of("while")),

    pExpresion(Set.of("+", "++", "-", "–", "!", "true", "false", "intLiteral", "charLiteral", "null",
            "this", "stringLiteral", "idMetVar", "new", "idClass", "(")),
    pExpresionConAsignacion(Set.of("=")),
    pOperadorAsignacion(Set.of("=")),
    pExpresionCompuesta(Set.of("+", "++", "-", "–", "!", "true", "false", "intLiteral", "charLiteral", "null",
            "this", "stringLiteral", "idMetVar", "new", "idClass", "(")),
    pRExpresionCompuesta(Set.of("||", "&&", "==", "!=", "<", ">", "<=", ">=", "+", "-", "*", "/", "%")),
    pOperadorBinario(Set.of("||", "&&", "==", "!=", "<", ">", "<=", ">=", "+", "-", "*", "/", "%")),
    pExpresionBasica(Set.of("+", "++", "-", "–", "!", "true", "false", "intLiteral", "charLiteral",
            "null", "this", "stringLiteral", "idMetVar", "new", "idClass", "(")),
    pOperadorUnario(Set.of("+", "++", "-", "--", "!")),
    pOperando(Set.of("true", "false", "intLiteral", "charLiteral", "null", "this", "stringLiteral",
            "idMetVar", "new", "idClass", "(")),
    pPrimitivo(Set.of("true", "false", "intLiteral", "charLiteral", "null")),
    pTipoPrimitivo(Set.of("boolean", "char", "int")),
    pReferencia(Set.of("this", "stringLiteral", "idMetVar", "new", "idClass", "(")),
    pRReferencia(Set.of(".")),
    pPrimario(Set.of("this", "stringLiteral", "idMetVar", "new", "idClass", "(")),
    pAccesoVarOLlamadaMetodo(Set.of("idMetVar")),
    pRAccesoVarOLlamadaMetodo(Set.of("(")),
    pLlamadaConstructor(Set.of("new")),
    pExpresionParentizada(Set.of("(")),
    pLlamadaMetodoEstatico(Set.of("idClass")),
    pArgsActuales(Set.of("(")),
    pListaExpsOpcional(Set.of("+", "++", "-", "–", "!", "true", "false", "intLiteral", "charLiteral", "null",
            "this", "stringLiteral", "idMetVar", "new", "idClass", "(", ",")),
    pListaExps(Set.of("+", "++", "-", "–", "!", "true", "false", "intLiteral", "charLiteral", "null",
            "this", "stringLiteral", "idMetVar", "new", "idClass", "(")),
    pRExpresionRListaExps(Set.of("+", "++", "-", "–", "!", "true", "false", "intLiteral", "charLiteral",
            "null", "this", "stringLiteral", "idMetVar", "new", "idClass", "(")),
    pRListaExps(Set.of(",")),
    pVarOMetodoEncadenado(Set.of(".")),
    pRVarOMetodoEncadenado(Set.of("("));

    private final Set<String> tokens;

    Primeros(Set<String> tokens) {
        this.tokens = new HashSet<>(tokens);
    }

    public boolean contiene(String token) {
        return tokens.contains(token);
    }

}
