package ast;

import exceptions.SemanticException;
import model.Method;
import model.NullType;
import model.Token;
import model.Type;
import sourcemanager.GeneratorManager;

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
        GeneratorManager generator = GeneratorManager.getInstance();
        if(!metodo.getReturnType().getName().equals("void")){
            retorno.generate();
            if(metodo.isStatic()){
                int offsetMetodoStatico = 2;
                generator.gen("STORE "+(metodo.getParameters().size()+offsetMetodoStatico+1));
            }else{
                int offsetMetodoDinamico = 3;
                generator.gen("STORE "+(metodo.getParameters().size()+offsetMetodoDinamico+1));
            }

            generator.gen("STOREFP ; Almacena el tope de la pila en el registro");

            int eliminarThis = 0;
            if( !metodo.isStatic() ){
                eliminarThis = 1;
            }

            generator.gen("RET "+(metodo.getParameters().size()+eliminarThis));
        }
    }
}
