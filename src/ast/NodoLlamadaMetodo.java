package ast;

import TablaSimbolo.TablaSimbolo;
import exceptions.SemanticException;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NodoLlamadaMetodo extends NodoPrimario{
    List<NodoExpresion> argumentos;
    Clase inWichClassIsDeclared;
    Clase classOfMyLeftChain;

    public NodoLlamadaMetodo(Token tk, List<NodoExpresion> argumentos, Clase c){
        super(tk);
        this.argumentos = argumentos;
        this.inWichClassIsDeclared = c;
    }

    public void setClassOfMyLeftChain(Clase c){
        this.classOfMyLeftChain = c;
    }

    @Override
    public Type check() throws SemanticException {
        Method method = resolveGetMethod();


        checkParameters(method);

        if(encadenado != null){
            return resolveChain(method);
        }
        return method.getReturnType();
    }

    private void checkParameters(Method method) throws SemanticException {
        List<Parameter> paramList = method.getParameters();
        if( paramList.size() != argumentos.size() ){
            throw new SemanticException(token, "Se quiere llamar al metodo con el numero incorrecto de parametros");
        }
        for(int i = 0; i < paramList.size(); i++){
            Type     tipoParametro =   paramList.get(i).getType();
            Type     tipoArgumento =   argumentos.get(i).check();

            tipoArgumento.esCompatible(tipoParametro, token);
        }
    }

    private Method resolveGetMethod() throws SemanticException {
        Method method;

        if(classOfMyLeftChain == null) {
            method = inWichClassIsDeclared.getMethod(token);
        }else{
            method = classOfMyLeftChain.getMethod(token);
        }

        if(method == null ){
            throw new SemanticException(token, "Se quiere llamar al metodo  "+token.getLexeme()+" pero no fue declarado");
        }

        if(method.isStatic()){
            throw new SemanticException(token, "Se quiere llamar a un metodo estatico en un contexto dinamico");
        }

        return method;
    }

    private Type resolveChain(Method method) throws SemanticException {
        if(method.getReturnType() instanceof PrimitiveType){
            throw new SemanticException(encadenado.getToken(), "No se puede acceder a miembros de un tipo primitivo");
        }
        Clase c = TablaSimbolo.getInstance().getClassByString( method.getReturnType().getName() );
        if(c == null){
            throw new SemanticException(method.getReturnType().getToken(), "El tipo de retorno del metodo "+method.getName()+" no fue declarado");
        }

        if( c.existeVariable(encadenado.getToken()) == null &&
                c.existeMetodo(encadenado.getToken()) == null ){
            throw new SemanticException(encadenado.getToken(), "La llamada a metodo "+getToken().getLexeme()+" retorna un tipo que no tiene ni metodo ni atributo con nombre "+encadenado.getToken().getLexeme());
        }

        if( encadenado instanceof NodoLlamadaMetodo encadenadoLlamadaMetodo){
            encadenadoLlamadaMetodo.setClassOfMyLeftChain(c);
        }
        if(encadenado instanceof NodoVar encadenadoNodoVar){
            encadenadoNodoVar.setClassOfMyLeftChain(c);
        }
        return encadenado.check();
    }


}
