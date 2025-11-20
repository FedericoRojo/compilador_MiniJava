package ast;

import exceptions.SemanticException;
import model.GenericMethod;
import model.Parameter;
import model.Token;
import model.Type;
import sourcemanager.GeneratorManager;

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
            if( !esEncadenamientoQueTerminaEnVariable(ladoIzquierdo) ){
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
                if (primario instanceof NodoVar primarioAsignable){
                    primarioAsignable.setEsAsignable();
                    return true;
                }else if(nodo instanceof NodoExpresionParentizada expresionParentizada){
                    return esEncadenamientoQueTerminaEnVariable(expresionParentizada.exp);
                }else {
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
            ladoIzquierdo.generate();

            limpiarPilaEnCasoDeQueLaLlamadaRetorneAlgoQueNoSeUsa();
        }else{
            ladoDerecho.generate();
            ladoIzquierdo.generate();
        }

    }

    private void limpiarPilaEnCasoDeQueLaLlamadaRetorneAlgoQueNoSeUsa(){
        if(ladoIzquierdo instanceof NodoPrimario leftNodoPrimario){
            NodoPrimario nodoPrimario = leftNodoPrimario.terminoEnLlamadaAMetodo();
            if(  esLlamadaMetodoYEsVoid(nodoPrimario) || esLlamadaMetodoEstaticoYEsVoid(nodoPrimario)){
                    GeneratorManager.getInstance().gen("POP");
            }
            esLlamadaAConstructorSinEncadenado(leftNodoPrimario);
        }
    }

    private void esLlamadaAConstructorSinEncadenado(NodoPrimario leftNodoPrimario){
        if(leftNodoPrimario instanceof NodoLlamadaConstructor nodoLlamadaConstructor){
            if(nodoLlamadaConstructor.encadenado == null) {
                GeneratorManager.getInstance().gen("POP");
            }
        }
    }


    private boolean esLlamadaMetodoYEsVoid(NodoPrimario nodoPrimario){
        return (nodoPrimario instanceof NodoLlamadaMetodo nodoLlamadaMetodo && !nodoLlamadaMetodo.getAssociatedMethod().isVoid());
    }

    private boolean esLlamadaMetodoEstaticoYEsVoid(NodoPrimario nodoPrimario){
        return (nodoPrimario instanceof NodoLlamadaMetodoEstatico nodoLlamadaMetodoEstatico && !nodoLlamadaMetodoEstatico.getAssociatedMethod().isVoid());
    }


}
