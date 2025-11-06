package model;

import ast.NodoSentencia;
import ast.NodoVarLocal;
import exceptions.SemanticException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GenericMethod {
    String name;
    Token token;
    List<Parameter> parameters;
    HashMap<Token, NodoVarLocal> variables;
    List<NodoSentencia> codeBlock;
    Clase owner;
    boolean hasBlock;
    int offset;


    public GenericMethod(String name, Token token, Clase owner){
        this.name = name;
        this.token = token;
        this.owner = owner;
        this.parameters = new ArrayList<>();
        this.hasBlock = false;
        this.codeBlock = new ArrayList<>();
    }

    public void addSentenceNodeToBlock(NodoSentencia sentence) throws SemanticException {
        if(sentence instanceof NodoVarLocal){
            if( variables.get(sentence.getToken()) != null){
                throw new SemanticException(sentence.getToken(), "Ya existe una variable con ese mismo nombre en este metodo");
            }
            variables.put(sentence.getToken(), (NodoVarLocal) sentence);
        }
        codeBlock.add(sentence);
    }

    public void checkSentences() throws SemanticException{
        //Aca falta ver que no recorra metodos que no son mios
        if(codeBlock != null) {
            for (NodoSentencia n : codeBlock) {
                n.check();
            }
        }
    }

    public void setHasBlock(boolean b){this.hasBlock = b;}

    public String getName(){ return this.name;}

    public Token getToken(){ return this.token; }

    public void checkWellDefined() throws SemanticException{
        for(Parameter p: parameters){
            p.checkWellDefined();
        }
    }

    public boolean hasSameParameters(List<Parameter> params){
        boolean toReturn = true;
        if( parameters.size() != params.size() ){
            toReturn = false;
        }

        for(int i = 0; i < parameters.size() && toReturn; i++){
            Parameter p1 = parameters.get(i);
            Parameter p2 = params.get(i);

            String name1 = (p1 != null && p1.getType()!= null) ? p1.getType().getName() : null;
            String name2 = (p2 != null && p2.getType()!= null) ? p2.getType().getName() : null;

            if(!Objects.equals(name1,name2)){
                toReturn=false;
            }
        }

        return toReturn;
    }

    public List<Parameter> getParameters(){ return this.parameters; }

    public Parameter searchVarInParameters(Token tk){
        for (Parameter p: parameters){
            if(tk.getLexeme().equals(p.getName())){
                return p;
            }
        }
        return null;
    }

    public void addParameter(Parameter p) throws SemanticException {
        for (Parameter existing : parameters) {
            if (existing.getName().equals(p.getName())) {
                throw new SemanticException(p.getToken(), "Error: este método ya tiene un parámetro con el mismo nombre");
            }
        }
        parameters.add(p);
    }
}
