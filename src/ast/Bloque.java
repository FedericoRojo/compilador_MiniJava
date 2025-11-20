package ast;

import exceptions.SemanticException;
import model.Token;
import sourcemanager.GeneratorManager;

import java.util.ArrayList;
import java.util.List;

public class Bloque extends NodoSentencia {
    List<NodoSentencia> sentencias = new ArrayList<>();
    Bloque parent;
    List<NodoVarLocal> variables = new ArrayList<>();
    int contadorOffsetVariables = 0;

    public Bloque(){

    }

    public Bloque(Token t) {
        super.setToken(t);
    }

    public void check() throws SemanticException {
        for(NodoSentencia s: sentencias){
            s.check();
        }
    }

    public int getContadorOffsetVariables(){
        return contadorOffsetVariables;
    }

    public void setContadorOffsetVariables(int i){
        this.contadorOffsetVariables = i;
    }

    public void setParent(Bloque p){
        this.parent = p;
    }

    public Bloque getParent(){return this.parent;}

    public void addSentence(NodoSentencia s) throws SemanticException {
        if( s instanceof NodoVarLocal varLocal){

            if( existeVariableLocal( varLocal ) ){
                throw new SemanticException(varLocal.getToken(), "La variable "+s.getToken().getLexeme()+" ya fue declara en este bloque");
            }
            if( existeVariableEnAmbito(varLocal) ){
                throw new SemanticException(varLocal.getToken(), "La variable ya fue declarada en un bloque contenedor");
            }

            if(parent != null && contadorOffsetVariables == 0){
                this.contadorOffsetVariables = parent.getContadorOffsetVariables();
            }

            varLocal.setOffset(contadorOffsetVariables);
            this.contadorOffsetVariables++;
            variables.add(varLocal);
        }
        sentencias.add(s);
    }

    public boolean existeVariableLocal(NodoVarLocal nodoVar) {
        for (NodoVarLocal v : variables) {
            Token t = v.getToken();
            Token t1 = nodoVar.getToken();
            if (v.getToken().getLexeme().equals(nodoVar.getToken().getLexeme())) {
                return true;
            }
        }
        return false;
    }

    public boolean existeVariableEnAmbito(NodoVarLocal nodoVar) {
        if (existeVariableLocal(nodoVar)) return true;
        return parent != null && parent.existeVariableEnAmbito(nodoVar);
    }

    public NodoVarLocal buscarVarLocalDistintaDeToken(Token tk){
        for (NodoVarLocal v : variables) {
            if ( v.getToken().getLexeme().equals(tk.getLexeme()) &&
                !v.getToken().getLineNumber().equals(tk.getLineNumber()) &&
                v.getToken().getLineNumber() < tk.getLineNumber() ) {
                return v;
            }
        }
        return null;
    }

    public NodoVarLocal buscarVariableEnAmbitoDistintaDeMi(Token tk) {
        NodoVarLocal result = buscarVarLocalDistintaDeToken(tk);
        if (result != null){
            return result;
        }else if(parent != null){
            return parent.buscarVariableEnAmbitoDistintaDeMi(tk);
        }else{
            return null;
        }
    }

    public NodoVarLocal buscarVarLocal(Token tk){
        for (NodoVarLocal v : variables) {
            if (v.getToken().getLexeme().equals(tk.getLexeme())) {
                return v;
            }
        }
        return null;
    }

    public NodoVarLocal buscarVariableEnAmbito(Token tk) {
        NodoVarLocal result = buscarVarLocal(tk);
        if (result != null){
            return result;
        }else if(parent != null){
            return parent.buscarVariableEnAmbito(tk);
        }else{
            return null;
        }
    }

    public void generate(){

        GeneratorManager generator = GeneratorManager.getInstance();
        for(NodoSentencia sentence: sentencias){
            if(sentence instanceof NodoReturn){
                generator.gen("FMEM "+variables.size());
            }
            sentence.generate();
        }
        generator.gen("FMEM "+variables.size());

        /*
        if(parent != null){
            parent.setContadorOffsetVariables( parent.getContadorOffsetVariables() );
        }*/
    }

}
