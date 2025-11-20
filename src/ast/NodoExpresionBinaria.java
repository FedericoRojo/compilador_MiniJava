package ast;

import exceptions.SemanticException;
import model.BooleanType;
import model.IntType;
import model.Token;
import model.Type;
import sourcemanager.GeneratorManager;

public class NodoExpresionBinaria extends NodoExpresion {
    NodoExpresion ladoIzquierdo;
    NodoExpresion ladoDerecho;
    Token operador;

    public NodoExpresionBinaria(NodoExpresion ladoIzq, Token operador, NodoExpresion ladoDer){
        super(operador);
        this.operador = operador;
        this.ladoIzquierdo= ladoIzq;
        this.ladoDerecho = ladoDer;
    }

    @Override
    public Type check() throws SemanticException {
        Type toR = null;
        Type ladoIzq = ladoIzquierdo.check();
        Type ladoDer = ladoDerecho.check();
        if(operadorCompatibleConIntYDevuelvenInt()){
            ladoIzq.esCompatible(new IntType(), token);
            ladoDer.esCompatible(new IntType(), token);
            toR = new IntType();
        }else if(operadorCompatibleConBoolean()){
            ladoIzq.esCompatible(new BooleanType(), token);
            ladoDer.esCompatible(new BooleanType(), token);
            toR = new BooleanType();
        }else if(operadorCompatibleConIntYDevuelvenBoolean()){
            ladoIzq.esCompatible(new IntType(), token);
            ladoDer.esCompatible(new IntType(), token);
            toR = new BooleanType();
        }else if(operadoresCompatiblesConTodo()){
            toR = manejoOperadoresComptabilesConTodo(ladoIzq, ladoDer);
        }
        return toR;
    }

    public boolean operadorCompatibleConIntYDevuelvenInt(){
        return operador.getLexeme().equals("+") || operador.getLexeme().equals("-") ||
                operador.getLexeme().equals("*") || operador.getLexeme().equals("/") ||
                operador.getLexeme().equals("%");
    }

    public boolean operadorCompatibleConIntYDevuelvenBoolean(){
        return operador.getLexeme().equals("<") || operador.getLexeme().equals("<=") ||
                operador.getLexeme().equals(">") || operador.getLexeme().equals(">=");
    }

    public boolean operadorCompatibleConBoolean(){
        return operador.getLexeme().equals("&&") || operador.getLexeme().equals("||");
    }

    public boolean operadoresCompatiblesConTodo(){
        return operador.getLexeme().equals("==") || operador.getLexeme().equals("!=");
    }

    public Type manejoOperadoresComptabilesConTodo(Type ladoIzq, Type ladoDer) throws SemanticException {
        boolean compatibles = false;

        try {
            ladoIzq.esCompatible(ladoDer, token);
            compatibles = true;
        } catch (SemanticException e) {}

        if (!compatibles) {
            try {
                ladoDer.esCompatible(ladoIzq, token);
                compatibles = true;
            } catch (SemanticException e) {}
        }

        if (!compatibles) {
            throw new SemanticException(operador, "Tipos incompatibles en comparación con " + operador.getLexeme());
        }

        return new BooleanType();
    }


    public void generate(){
        GeneratorManager generator = GeneratorManager.getInstance();
        ladoIzquierdo.generate();
        ladoDerecho.generate();
        switch (operador.getLexeme()){
            case "+":
                generator.gen("ADD");
                break;
            case "-":
                generator.gen("SUB");
                break;
            case "*":
                generator.gen("MUL");
                break;
            case "/":
                generator.gen("DIV");
                break;
            case "%":
                generator.gen("MOD");
                break;
            case "<":
                generator.gen("LT");
                break;
            case "<=":
                generator.gen("LE");
                break;
            case ">":
                generator.gen("GT");
                break;
            case ">=":
                generator.gen("GE");
                break;
            case "&&":
                generator.gen("AND");
                break;
            case "||":
                generator.gen("OR");
                break;
            case "==":
                generator.gen("EQ");
                break;
            case "!=":
                generator.gen("NE");
                break;
        }

    }
}
