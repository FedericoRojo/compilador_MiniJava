package ast;

import exceptions.SemanticException;
import model.BooleanType;
import model.Token;
import model.Type;
import sourcemanager.GeneratorManager;

public class NodoWhile extends NodoSentencia{
    private static int contador = 0;
    NodoExpresion condicion;
    NodoSentencia sentencias;
    String lblWhile;
    String lblFinWhile;

    public NodoWhile(Token t, NodoExpresion condicion, NodoSentencia cuerpo){
        this.setToken(t);
        this.condicion = condicion;
        this.sentencias = cuerpo;
        lblWhile = "lblWhile"+contador;
        lblFinWhile = "lblFinWhile"+contador;
    }


    @Override
    public void check() throws SemanticException {
        condicion.check().esCompatible(new BooleanType(), token);
        sentencias.check();
    }

    public void generate(){
        GeneratorManager generator = GeneratorManager.getInstance();
        generator.gen(lblWhile+": NOP");
        condicion.generate();
        generator.gen(lblFinWhile+": NOP");
        sentencias.generate();
        generator.gen(lblFinWhile+": NOP");
    }
}
