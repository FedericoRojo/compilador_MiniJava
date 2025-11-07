package ast;

import TablaSimbolo.TablaSimbolo;
import exceptions.SemanticException;
import model.*;
import java.util.ArrayList;
import java.util.List;
import sourcemanager.GeneratorManager;

public class NodoLlamadaMetodoEstatico extends NodoPrimario{
    List<NodoExpresion> argumentos;
    Clase clase;
    Token className;
    String label;

    public NodoLlamadaMetodoEstatico(Token methodName, List<NodoExpresion> list, Token className) throws SemanticException {
        super(methodName);
        this.label = "lblMet"+methodName.getLexeme()+"@"+className.getLexeme();
        this.argumentos = new ArrayList<>(list);
        this.className = className;
    }

    @Override
    public Type check() throws SemanticException {
        this.clase = TablaSimbolo.getInstance().getClassByString(className.getLexeme());
        if(this.clase == null){
            throw new SemanticException(className, "Se intenta acceder a una clase que no existe");
        }

        Method method = clase.getMethod(token);

        if(method == null ){
            throw new SemanticException(token, "Se quiere llamar al metodo estatico "+token.getLexeme()+" pero no fue declarado");
        }
        if(!method.isStatic()){
            throw new SemanticException(token, "Se quiere llamar al metodo estatico "+token.getLexeme()+" pero no es estatico");
        }

        checkParametersAndArguments(method);

        if(encadenado != null){
            if(method.getReturnType() instanceof PrimitiveType){
                throw new SemanticException(encadenado.getToken(), "No se puede acceder a miembros de un tipo primitivo");
            }
            Clase c = TablaSimbolo.getInstance().getClassByString( method.getReturnType().getName() );
            if(c == null){
                throw new SemanticException(token, "El tipo de la variable "+token.getLexeme()+" no fue declarado");
            }
            if( c.existeVariable(encadenado.getToken()) == null &&
                c.existeMetodo(encadenado.getToken()) == null ){
                throw new SemanticException(encadenado.getToken(), "La variable "+getToken().getLexeme()+" es de un tipo que no tiene ni metodo ni atributo con nombre "+encadenado.getToken().getLexeme());
            }

            if( encadenado instanceof NodoLlamadaMetodo encadenadoLlamadaMetodo){
                encadenadoLlamadaMetodo.setClassOfMyLeftChain(c);
            }
            if(encadenado instanceof NodoVar encadenadoNodoVar){
                encadenadoNodoVar.setClassOfMyLeftChain(c);
            }

            return encadenado.check();
        }
        return method.getReturnType();
    }

    private void checkParametersAndArguments(Method method) throws SemanticException {
        List<Parameter> paramList = method.getParameters();
        if( paramList.size() != argumentos.size() ){
            throw new SemanticException(token, "Se quiere llamar al metodo estico "+getToken().getLexeme()+" con el numero incorrecto de parametros");
        }
        for(int i = 0; i < paramList.size(); i++){
            Type     tipoParametro =   paramList.get(i).getType();
            Type     tipoArgumento =   argumentos.get(i).check();

            tipoArgumento.esCompatible(tipoParametro, token);
        }

    }

    public void generate(){
        GeneratorManager generator = GeneratorManager.getInstance();
        for(NodoExpresion e: argumentos){
            e.generate();
        }
        generator.gen("PUSH "+label+"; apila el metodo");
        generator.gen("CALL ; Llama al metodo en el tope de la pila");
    }
}
