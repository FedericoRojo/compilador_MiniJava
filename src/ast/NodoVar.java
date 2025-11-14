package ast;

import TablaSimbolo.TablaSimbolo;
import exceptions.SemanticException;
import model.*;
import sourcemanager.GeneratorManager;

public class NodoVar extends NodoPrimario{
    Clase inWichClassIsDeclared;
    Clase classOfMyLeftChain;
    NodoExpresion leftChain;
    GenericMethod method;
    Bloque bloque;
    VarGeneral data;

    public NodoVar(Token tk, Clase c, GenericMethod m, Bloque b){
        super(tk);
        this.inWichClassIsDeclared = c;
        this.method = m;
        this.bloque = b;
    }

    public VarGeneral getData(){ return this.data; }

    public void setClassOfMyLeftChain(Clase c){
        this.classOfMyLeftChain = c;
    }

    public void setLeftChain(NodoExpresion exp){
        this.leftChain = exp;
    }

    @Override
    public Type check() throws SemanticException {
        Type varLocalType = null;

        if(classOfMyLeftChain == null) {

            NodoVarLocal varLocal = bloque.buscarVariableEnAmbito(token);
            if (varLocal != null) {
                data = varLocal;
                varLocalType = varLocal.getType();
            }
            if (varLocalType == null) {
                Parameter p = method.searchVarInParameters(token);
                if(p != null) {
                    varLocalType = p.getType();
                    data = p;
                }
            }
            if (varLocalType == null) {
                Attribute attribute = inWichClassIsDeclared.existeVariable(token);
                if(attribute != null){
                    varLocalType = attribute.getType();
                    data = attribute;
                }
                if(varLocalType != null && method instanceof Method mMethod && mMethod.isStatic()){
                    throw new SemanticException(token, "No se puede acceder a una variable de instancia en un metodo estatico");
                }
            }

        }else{
            Attribute attribute = classOfMyLeftChain.existeVariable(token);
            if(attribute != null){
                varLocalType = attribute.getType();
                data = attribute;
            }
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
                encadenadoLlamadaMetodo.setLeftChain(this);
            }
            if(encadenado instanceof NodoVar encadenadoNodoVar){
                encadenadoNodoVar.setClassOfMyLeftChain(c);
                encadenadoNodoVar.setLeftChain(this);
            }
            varLocalType = encadenado.check();
        }

        return varLocalType;
    }

    public void generate(){
        GeneratorManager generator = GeneratorManager.getInstance();

        if(leftChain == null) {
            if (data instanceof NodoVarLocal dataVarLocal) {
                if (esAsignable) {
                    // x = expresion
                    String offset = dataVarLocal.getOffset() == 0 ? "0" : "-" + dataVarLocal.getOffset();
                    generator.gen("LOAD " + offset);
                    generator.gen("STORE " + offset);
                } else {
                    // x.algo
                    String offset = dataVarLocal.getOffset() == 0 ? "0" : "-" + dataVarLocal.getOffset();
                    generator.gen("LOAD " + offset);
                }

            } else if (data instanceof Attribute dataAttribute) {

                if (esAsignable) {
                    generator.gen("LOAD 3 ; cargo el THIS");
                    generator.gen("SWAP");
                    generator.gen("STOREREF " + dataAttribute.getOffset());
                } else {
                    generator.gen("LOAD 3 ; cargo el THIS");
                    generator.gen("LOADREF " + dataAttribute.getOffset());
                }

            } else if (data instanceof Parameter dataParameter) {
                int offsetRegistroDeActivacion = 3;
                if( (method instanceof Method methodM && methodM.isStatic()) ) {
                        offsetRegistroDeActivacion = 2;
                }

                if (esAsignable) {
                    int offsetParam = (dataParameter.getTotalParamsSize() + offsetRegistroDeActivacion - dataParameter.getOffset());
                    generator.gen("STORE " + offsetParam);
                } else {
                    int offsetParam = (dataParameter.getTotalParamsSize() + offsetRegistroDeActivacion - dataParameter.getOffset());
                    generator.gen("LOAD " + offsetParam);
                }

            }
        }else{
            if (data instanceof Attribute dataAttribute) {

                if (esAsignable) {
                    generator.gen("SWAP");
                    generator.gen("STOREREF " + dataAttribute.getOffset());
                } else {
                    generator.gen("LOADREF " + dataAttribute.getOffset());
                }

            }
        }
        if(encadenado != null){
            encadenado.generate();
        }
    }
}
