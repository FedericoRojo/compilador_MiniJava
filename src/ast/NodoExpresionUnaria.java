package ast;

import exceptions.SemanticException;
import model.BooleanType;
import model.IntType;
import model.Token;
import model.Type;

public class NodoExpresionUnaria extends NodoExpresion {
    NodoOperando operando;
    Token operador;

    public NodoExpresionUnaria(Token operador, NodoOperando operando){
        super(operador);
        this.operador = operador;
        this.operando = operando;
    }

    @Override
    public Type check() throws SemanticException {
        Type tipoOperando = operando.check();
        if( operadorCompatibleConInt(operador) ){
            tipoOperando.esCompatible(new IntType(), token);
        }
        if( operadorCompatibleConBoolean(operador) ){
            tipoOperando.esCompatible(new BooleanType(), token);
        }
        return tipoOperando;
    }

    public boolean operadorCompatibleConInt(Token t){
        return operador.getLexeme().equals("+") || operador.getLexeme().equals("++")
                || operador.getLexeme().equals("-") || operador.getLexeme().equals("--");
    }

    public boolean operadorCompatibleConBoolean(Token t){
        return operador.getLexeme().equals("!");
    }
}
