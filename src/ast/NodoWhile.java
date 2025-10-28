package ast;

import exceptions.SemanticException;
import model.BooleanType;
import model.Token;
import model.Type;

public class NodoWhile extends NodoSentencia{
    NodoExpresion condicion;
    NodoSentencia sentencias;

    public NodoWhile(Token t, NodoExpresion condicion, NodoSentencia cuerpo){
        this.setToken(t);
        this.condicion = condicion;
        this.sentencias = cuerpo;
    }


    @Override
    public void check() throws SemanticException {
        condicion.check().esCompatible(new BooleanType(), token);
        sentencias.check();
    }
}
