package ast;

import exceptions.SemanticException;
import model.Token;
import model.Type;
import java.util.AbstractMap.SimpleEntry;

public class NodoAsignacionOLlamada extends NodoSentencia {
    NodoExpresion ladoIzquierdo;
    NodoExpresion ladoDerecho;

    public NodoAsignacionOLlamada(){

    }


    public void setLadoIzquierdo(NodoExpresion ladoIzquierdo) {
        this.ladoIzquierdo = ladoIzquierdo;
    }

    public void setLadoDerecho(NodoExpresion ladoDerecho){
        this.ladoDerecho = ladoDerecho;
    }

    @Override
    public void check() throws SemanticException {
        if(ladoDerecho == null){
            ladoIzquierdo.check();
            SimpleEntry<Boolean, Token> entry = esExpresionLlamada(ladoIzquierdo);
            if (entry.getKey() == false) {
                throw new SemanticException(entry.getValue(),
                        "Una expresión usada como sentencia debe ser una llamada o una asignación");
            }
        }else{
            if(!(ladoIzquierdo instanceof NodoVar) &&
                    !esEncadenamientoQueTerminaEnVariable(ladoIzquierdo) ){
                throw new SemanticException(token, "El lado izquierdo de la asignación no es asignable");
            }

            Type tipoIzq = ladoIzquierdo.check();
            Type tipoDer = ladoDerecho.check();

            tipoDer.esCompatible(tipoIzq, token);
        }
    }

    private SimpleEntry<Boolean, Token> esExpresionLlamada(NodoExpresion expr) {
        if (expr instanceof NodoPrimario) {
            NodoPrimario enc = ((NodoPrimario) expr).encadenado;
            if (enc != null) {
                return esExpresionLlamada(enc);
            } else if (expr instanceof NodoLlamadaMetodo
                    || expr instanceof NodoLlamadaMetodoEstatico
                    || expr instanceof NodoLlamadaConstructor) {
                return new SimpleEntry<>(true, expr.getToken());
            }
        }
        return new SimpleEntry<>(false, expr.getToken());
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
        if(ladoDerecho == null){
            ladoDerecho.generate();
        }else{
            //ACA hay que hacer algoo
        }
    }

}
