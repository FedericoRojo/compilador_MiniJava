package ast;

import TablaSimbolo.TablaSimbolo;
import exceptions.SemanticException;
import model.*;

public class NodoExpresionParentizada extends NodoPrimario {
    NodoExpresion exp;

    public NodoExpresionParentizada(Token tk, NodoExpresion nodo){
        super(tk);
        this.exp = nodo;
    }

    @Override
    public Type check() throws SemanticException {
        Type type = exp.check();

        if(encadenado !=null ){
            if(type instanceof PrimitiveType){
                throw new SemanticException(encadenado.getToken(), "No se puede acceder a miembros de un tipo primitivo");
            }
            Clase c = TablaSimbolo.getInstance().getClassByString( type.getName() );
            if(c == null){
                throw new SemanticException(type.getToken(), "El tipo de la expresion "+token+" no fue declarado");
            }

            if( c.existeVariable(encadenado.getToken()) == null &&
                c.existeMetodo(encadenado.getToken()) == null ){
                throw new SemanticException(encadenado.getToken(), "La expresión parentizada es de un tipo que no tiene ni metodo ni atributo con nombre "+encadenado.getToken().getLexeme());
            }

            if( encadenado instanceof NodoLlamadaMetodo encadenadoLlamadaMetodo){
                encadenadoLlamadaMetodo.setLeftChain(this);
                encadenadoLlamadaMetodo.setClassOfMyLeftChain(c);
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
        exp.generate();
        if(encadenado != null){
            encadenado.generate();
        }
    }
}
