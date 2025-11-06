package ast;

import exceptions.SemanticException;
import model.BooleanType;
import model.Token;
import model.Type;

import java.util.List;

public class NodoIf extends NodoSentencia{
    NodoExpresion condicion;
    NodoSentencia  sentenciaThen;
    NodoSentencia  sentenciaElse;

    public NodoIf(Token token, NodoExpresion cond, NodoSentencia cuerpoThen, NodoSentencia cuerpoElse){
        super.setToken(token);
        this.condicion = cond;
        this.sentenciaThen = cuerpoThen;
        this.sentenciaElse = cuerpoElse;
    }


    public void check() throws SemanticException {
        condicion.check().esCompatible( new BooleanType(), token );
        sentenciaThen.check();
        if(!(sentenciaElse instanceof NodoSentenciaVacia)){
            sentenciaElse.check();
        }

    }


    public void generate(){

    }
}
