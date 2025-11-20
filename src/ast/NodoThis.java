package ast;

import TablaSimbolo.TablaSimbolo;
import exceptions.SemanticException;
import model.*;
import sourcemanager.GeneratorManager;

public class NodoThis extends NodoPrimario{
    Type type;
    GenericMethod method;

    public NodoThis(Token tk, Type t, GenericMethod m){
        super(tk);
        this.type = t;
        this.method = m;
    }

    @Override
    public Type check() throws SemanticException {
        if(method instanceof Method){
            if(((Method) method).isStatic()) {
                throw new SemanticException(super.token, "No se puede usar this en un metodo estatico");
            }
        }

        if(encadenado != null){
            if(type instanceof PrimitiveType){
                throw new SemanticException(encadenado.getToken(), "No se puede acceder a miembros de un tipo primitivo");
            }

            Clase c = TablaSimbolo.getInstance().getClassByString(type.getName());
            if( c.existeVariable(encadenado.getToken()) == null &&
                    c.existeMetodo(encadenado.getToken()) == null){
                throw new SemanticException(encadenado.getToken(), "La variable "+getToken().getLexeme()+" es de un tipo que no tiene ni metodo ni atributo con nombre "+encadenado.getToken().getLexeme());
            }

            if( encadenado instanceof NodoLlamadaMetodo encadenadoLlamadaMetodo){
                encadenadoLlamadaMetodo.setClassOfMyLeftChain(c);
                encadenadoLlamadaMetodo.setLeftChain(this);
            }
            if(encadenado instanceof NodoVar encadenadoNodoVar){
                encadenadoNodoVar.setLeftChain(this);
                encadenadoNodoVar.setClassOfMyLeftChain(c);
            }

            return encadenado.check();
        }
        return type;
    }

    public void generate(){
        GeneratorManager generator = GeneratorManager.getInstance();
        generator.gen("LOAD 3");
        if(encadenado != null){
            encadenado.generate();
        }
    }
}
