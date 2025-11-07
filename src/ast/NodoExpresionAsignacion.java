package ast;

import exceptions.SemanticException;
import model.Token;
import model.Type;

public class NodoExpresionAsignacion extends NodoExpresion{
    NodoExpresion ladoIzquierdo;
    NodoExpresion ladoDerecho;

    public NodoExpresionAsignacion(Token tk, NodoExpresion ladoIzq, NodoExpresion ladoDer){
        super(tk);
        this.ladoIzquierdo = ladoIzq;
        this.ladoDerecho = ladoDer;
    }

    @Override
    public Type check() throws SemanticException {
        if(!esEncadenamientoQueTerminaEnVariable(ladoIzquierdo) ){
                throw new SemanticException(token, "Se esta intentando asignar una expresion a algo que no es una variable");
        }

        Type tipoIzquierdo = ladoIzquierdo.check();
        Type tipoDerecho = ladoDerecho.check();

        tipoIzquierdo.esCompatible(tipoDerecho, token);

        return tipoIzquierdo;
    }


    private boolean esEncadenamientoQueTerminaEnVariable(NodoExpresion nodo) {
        if (nodo instanceof NodoPrimario) {
            NodoPrimario primario = (NodoPrimario) nodo;
            if (primario.encadenado == null){
                if (primario instanceof NodoVar){
                    return true;
                } else {
                    return false;
                }
            }else {
                return esEncadenamientoQueTerminaEnVariable(primario.encadenado);
            }
        }
        return false;
    }

    public void generate(){

    }
}
