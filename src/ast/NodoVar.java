package ast;

import TablaSimbolo.TablaSimbolo;
import exceptions.SemanticException;
import model.*;

public class NodoVar extends NodoPrimario{
    Clase inWichClassIsDeclared;
    Clase classOfMyLeftChain;
    GenericMethod method;
    Bloque bloque;


    public NodoVar(Token tk, Clase c, GenericMethod m, Bloque b){
        super(tk);
        this.inWichClassIsDeclared = c;
        this.method = m;
        this.bloque = b;
    }

    public void setClassOfMyLeftChain(Clase c){
        this.classOfMyLeftChain = c;
    }

    @Override
    public Type check() throws SemanticException {
        Type varLocalType = null;

        if(classOfMyLeftChain == null) {
            NodoVarLocal varLocal = bloque.buscarVariableEnAmbito(token);
            if (varLocal != null) {
                varLocalType = varLocal.getType();
            }
            if (varLocalType == null) {
                varLocalType = method.searchVarInParameters(token);
            }
            if (varLocalType == null) {
                varLocalType = inWichClassIsDeclared.existeVariable(token);
                if(varLocalType != null && method instanceof Method mMethod && mMethod.isStatic()){
                    throw new SemanticException(token, "No se puede acceder a una variable de instancia en un metodo estatico");
                }
            }
        }else{
            varLocalType = classOfMyLeftChain.existeVariable(token);
        }


        if (varLocalType == null) {
            throw new SemanticException(token, "Se quiere utilizar la variable " + token.getLexeme() + " pero no fue declarada");
        }

        if(encadenado != null){
            if(varLocalType instanceof PrimitiveType){
                throw new SemanticException(encadenado.getToken(), "No se puede acceder a miembros de un tipo primitivo");
            }
            ReferenceType varLocalRefType = (ReferenceType) varLocalType;
            Clase c = varLocalRefType.associatedClass;
            if(c == null){
                throw new SemanticException(token, "El tipo de la variable "+token.getLexeme()+" no fue declarado");
            }
            if( c.existeVariable(encadenado.getToken()) == null &&
                c.existeMetodo(encadenado.getToken()) == null){
                throw new SemanticException(encadenado.getToken(), "La variable "+getToken().getLexeme()+" es de un tipo que no tiene ni metodo ni atributo con nombre "+encadenado.getToken().getLexeme());
            }
            if( encadenado instanceof NodoLlamadaMetodo encadenadoLlamadaMetodo){
                encadenadoLlamadaMetodo.setClassOfMyLeftChain(c);
            }
            if(encadenado instanceof NodoVar encadenadoNodoVar){
                encadenadoNodoVar.setClassOfMyLeftChain(c);
            }
            varLocalType = encadenado.check();
        }
        return varLocalType;
    }
}
