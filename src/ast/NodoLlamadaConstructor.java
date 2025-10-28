package ast;

import TablaSimbolo.TablaSimbolo;
import exceptions.SemanticException;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class NodoLlamadaConstructor extends NodoPrimario{
    List<NodoExpresion> argumentos;

    public NodoLlamadaConstructor(Token tk, List<NodoExpresion> list){
        super(tk);
        this.argumentos = new ArrayList<>(list);
    }

    @Override
    public Type check() throws SemanticException {
        Clase claseAsociadaConstructor = TablaSimbolo.getInstance().getClassByString(super.token.getLexeme());

        if(claseAsociadaConstructor == null ){
            throw new SemanticException(token, "Se quiere llamar al constructor  "+token.getLexeme()+" pero no existe una clase con ese nombre");
        }

        checkParametersAndArguments(claseAsociadaConstructor);


        Type tipo = TablaSimbolo.getInstance().resolveType(claseAsociadaConstructor.getToken());
        if(tipo instanceof ReferenceType refType){
            refType.setAssociatedClass(claseAsociadaConstructor);
        }

        if(encadenado != null){
            if( claseAsociadaConstructor.existeVariable(encadenado.getToken()) == null &&
                claseAsociadaConstructor.existeMetodo(encadenado.getToken()) == null ){
                throw new SemanticException(encadenado.getToken(), "La llamada al constructor "+super.token.getLexeme()+" devuelve un tipo que no tiene ni metodo ni atributo con nombre "+encadenado.getToken().getLexeme());
            }

            if( encadenado instanceof NodoLlamadaMetodo encadenadoLlamadaMetodo){
                encadenadoLlamadaMetodo.setClassOfMyLeftChain(claseAsociadaConstructor);
            }
            if(encadenado instanceof NodoVar encadenadoNodoVar){
                encadenadoNodoVar.setClassOfMyLeftChain(claseAsociadaConstructor);
            }
            return encadenado.check();
        }
        return tipo;
    }

    private void checkParametersAndArguments(Clase claseAsociadaConstructor) throws SemanticException {
        Constructor constructor = claseAsociadaConstructor.getConstructor();
        List<Parameter> paramList = constructor.getParameters();
        if( paramList.size() != argumentos.size() ){
            throw new SemanticException(super.token, "Se quiere llamar al constructor con el numero incorrecto de parametros");
        }
        for(int i = 0; i < paramList.size(); i++){
            Type     tipoParametro =   paramList.get(i).getType();
            Type     tipoArgumento =   argumentos.get(i).check();

            tipoArgumento.esCompatible(tipoParametro, token);
        }
    }
}
