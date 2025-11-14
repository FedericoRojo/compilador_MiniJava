package ast;

import exceptions.SemanticException;
import model.BooleanType;
import model.IntType;
import model.Token;
import model.Type;
import sourcemanager.GeneratorManager;

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


    public void generate(){
        GeneratorManager generator = GeneratorManager.getInstance();
        operando.generate();
        switch (operador.getLexeme()){
            case "+":
                generator.gen("; + unario no hace nada");
                break;
            case "-":
                generator.gen("NEG");
                break;
            case "++":
                generator.gen("PUSH 1");
                generator.gen("ADD");
                break;
            case "--":
                generator.gen("PUSH 1");
                generator.gen("SUB");
                break;
            case "!":
                generator.gen("NOT");
                break;
        }
    }
}
