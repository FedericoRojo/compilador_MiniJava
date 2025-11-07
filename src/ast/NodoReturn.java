package ast;

import exceptions.SemanticException;
import model.Method;
import model.NullType;
import model.Token;
import model.Type;

public class NodoReturn extends NodoSentencia{
    NodoExpresion retorno;
    Method metodo;

    public NodoReturn(Token t, NodoExpresion exp, Method m){
        super.setToken(t);
        this.retorno = exp;
        this.metodo = m;
    }

    public void check() throws SemanticException {
        Type tipoRetornoExpresion = retorno.check();
        Type tipoRetornoDeclarado = metodo.getReturnType();

        if( tipoRetornoDeclarado.getName().equals("void") ){
            tipoRetornoExpresion.esCompatible(new NullType(), token);
        }else{
            tipoRetornoExpresion.esCompatible(tipoRetornoDeclarado, token);
        }
    }

    public void generate(){
    }
}
