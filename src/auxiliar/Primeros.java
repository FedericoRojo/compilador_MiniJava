package auxiliar;

import java.util.Set;
import java.util.HashSet;

public enum Primeros {
    pInicial(Set.of("$", "class", "abstract", "static", "final")),
    pListaClases(Set.of("ε", "class", "abstract", "static", "final")),
    pClase(Set.of("class", "abstract", "static", "final")),
    pModificadorOpcional(Set.of("ε", "abstract", "static", "final")),
    pHerenciaOpcional(Set.of("extends", "ε")),
    pListaMiembros(Set.of("ε", "idClase", "boolean", "char", "int", "public", "abstract", "static", "final", "void")),

    pMiembro(Set.of("idClase", "boolean", "char", "int", "public", "abstract", "static", "final", "void")),
    pMetodoConModificador(Set.of("abstract", "static", "final")),
    pRMetodoConModficador(Set.of("void", "boolean", "char", "int")),
    pMetodoOAtributo(Set.of("idClase", "boolean", "char", "int", "void")),
    pR_MetodoOAtributo(Set.of("(", ";")),
    pRMetodo(Set.of("(")),
    pTipoMetodo(Set.of("void", "idClase", "boolean", "char", "int")),
    pMoficador(Set.of("abstract", "static", "final")),

    pConstructor(Set.of("public")),

    pTipo(Set.of("boolean", "char", "int", "idClase")),
    pArgsFormales(Set.of("(")),
    pListaArgsFormalesOpcional(Set.of("ε", "boolean", "char", "int", "idClase")),
    pListaArgsFormales(Set.of("boolean", "char", "int", "idClase")),
    pRArgFormalRListaArgsFormales(Set.of("boolean", "char", "int", "idClase")),
    pRListaArgsFormales(Set.of(",", "ε")),
    pArgFormal(Set.of("boolean", "char", "int", "idClase")),
    pBloqueOpcional(Set.of("ε", "{")),
    pBloque(Set.of("{")),

    pListaSentencias(Set.of("ε", ";", "+", "++", "-", "–", "!", "true", "false", "intLiteral", "charLiteral",
            "null", "this", "stringLiteral", "idMetVar", "new", "idClase", "(", "var", "return", "if", "while", "{")),
    pSentencia(Set.of(";", "+", "++", "-", "–", "!", "true", "false", "intLiteral", "charLiteral", "null",
            "this", "stringLiteral", "idMetVar", "new", "idClase", "(", "var", "return", "if", "while", "{")),
    pAsignacionOLlamada(Set.of("pExpresion")),
    pVarLocal(Set.of("var")),
    pReturn(Set.of("return")),
    pExpresionOpcional(Set.of("ε", "pExpresion")),
    pIf(Set.of("if")),
    pIfConElse(Set.of("else", "ε")),
    pWhile(Set.of("while")),

    pExpresion(Set.of("+", "++", "-", "–", "!", "true", "false", "intLiteral", "charLiteral", "null",
            "this", "stringLiteral", "idMetVar", "new", "idClase", "(")),
    pExpresionConAsignacion(Set.of("=", "+=", "-=", "ε")),
    pOperadorAsignacion(Set.of("=", "+=", "-=")),
    pExpresionCompuesta(Set.of("+", "++", "-", "–", "!", "true", "false", "intLiteral", "charLiteral", "null",
            "this", "stringLiteral", "idMetVar", "new", "idClase", "(")),
    pRExpresionBasicaConExpresionCompuesta(Set.of("+", "++", "-", "–", "!", "true", "false", "intLiteral",
            "charLiteral", "null", "this", "stringLiteral", "idMetVar", "new", "idClase", "(")),
    pRExpresionCompuesta(Set.of("||", "&&", "==", "!=", "<", ">", "<=", ">=", "+", "-", "*", "/", "%", "ε")),
    pOperadorBinario(Set.of("||", "&&", "==", "!=", "<", ">", "<=", ">=", "+", "-", "*", "/", "%")),
    pExpresionBasica(Set.of("+", "++", "-", "–", "!", "true", "false", "intLiteral", "charLiteral",
            "null", "this", "stringLiteral", "idMetVar", "new", "idClase", "(")),
    pOperadorUnario(Set.of("+", "++", "-", "–", "!")),
    pOperando(Set.of("true", "false", "intLiteral", "charLiteral", "null", "this", "stringLiteral",
            "idMetVar", "new", "idClase", "(")),
    pPrimitivo(Set.of("true", "false", "intLiteral", "charLiteral", "null")),
    pTipoPrimitivo(Set.of("boolean", "char", "int")),
    pReferencia(Set.of("this", "stringLiteral", "idMetVar", "new", "idClase", "(")),
    pRReferencia(Set.of(".", "ε")),
    pPrimario(Set.of("this", "stringLiteral", "idMetVar", "new", "idClase", "(")),
    pAccesoVarOLlamadaMetodo(Set.of("idMetVar")),
    pRAccesoVarOLlamadaMetodo(Set.of("(", "ε")),
    pLlamadaConstructor(Set.of("new")),
    pExpresionParentizada(Set.of("(")),
    pLlamadaMetodoEstatico(Set.of("idClase")),
    pArgsActuales(Set.of("(")),
    pListaExpsOpcional(Set.of("+", "++", "-", "–", "!", "true", "false", "intLiteral", "charLiteral", "null",
            "this", "stringLiteral", "idMetVar", "new", "idClase", "(", ",", "ε")),
    pListaExps(Set.of("+", "++", "-", "–", "!", "true", "false", "intLiteral", "charLiteral", "null",
            "this", "stringLiteral", "idMetVar", "new", "idClase", "(")),
    pRExpresionRListaExps(Set.of("+", "++", "-", "–", "!", "true", "false", "intLiteral", "charLiteral",
            "null", "this", "stringLiteral", "idMetVar", "new", "idClase", "(")),
    pRListaExps(Set.of(",", "ε")),
    pVarOMetodoEncadenado(Set.of(".")),
    pRVarOMetodoEncadenado(Set.of("(", "ε"));

    private final Set<String> tokens;

    Primeros(Set<String> tokens) {
        this.tokens = new HashSet<>(tokens);
    }

    public boolean contiene(String token) {
        return tokens.contains(token);
    }

}
