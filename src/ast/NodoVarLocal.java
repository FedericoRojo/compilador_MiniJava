package ast;

import exceptions.SemanticException;
import model.*;

import java.util.List;

public class NodoVarLocal extends NodoSentencia implements VarGeneral {
    NodoExpresion contenido;
    Type tipo;
    GenericMethod metodo;
    Bloque bloque;

    public NodoVarLocal( Token tk, NodoExpresion contenido, GenericMethod m, Bloque b) {
        this.contenido = contenido;
        super.setToken(tk);
        this.metodo = m;
        this.bloque = b;
    }

    public Type getType() throws SemanticException {
        if (tipo == null) {
            tipo = contenido.check();
        }
        return tipo;
    }

    public void check() throws SemanticException {
        checkIfExistsInParams();

        NodoVarLocal nodoVarLocal = bloque.buscarVariableEnAmbitoDistintaDeMi(token);

        if( nodoVarLocal != null ){
            throw new SemanticException(token, "Se intenta declarar una variable que ya ha sido declarada");
        }

        Type t = contenido.check();
        if( t instanceof NullType ){
            throw new SemanticException(contenido.getToken(), "El contenido de la variable no puede ser nulo");
        }

        if(tipo == null){
            tipo = t;
        }
    }


    private void checkIfExistsInParams() throws SemanticException {
        List<Parameter> list = metodo.getParameters();
        for(Parameter p: list){
            if(p.getName().equals(token.getLexeme())){
                throw new SemanticException(token, "La variable tiene el mismo nombre que el parametro del metodo "+metodo.getName());
            }
        }
    }
}
