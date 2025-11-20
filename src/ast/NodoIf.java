package ast;

import exceptions.SemanticException;
import model.BooleanType;
import model.Token;
import model.Type;
import sourcemanager.GeneratorManager;

import java.util.List;

public class NodoIf extends NodoSentencia{
    private static int contador = 0;

    NodoExpresion condicion;
    NodoSentencia  sentenciaThen;
    NodoSentencia  sentenciaElse;
    String lblInicioElse;
    String lblFinElse;

    public NodoIf(Token token, NodoExpresion cond, NodoSentencia cuerpoThen, NodoSentencia cuerpoElse){
        super.setToken(token);
        this.condicion = cond;
        this.sentenciaThen = cuerpoThen;
        this.sentenciaElse = cuerpoElse;
        this.lblFinElse = "lblFinElse"+contador;
        this.lblInicioElse = "lblInicioElse"+contador;
        contador++;
    }


    public void check() throws SemanticException {
        condicion.check().esCompatible( new BooleanType(), token );
        sentenciaThen.check();
        if(!(sentenciaElse instanceof NodoSentenciaVacia)){
            sentenciaElse.check();
        }
    }


    public void generate(){
        GeneratorManager generator = GeneratorManager.getInstance();
        if(!(sentenciaElse instanceof NodoSentenciaVacia)){
            condicion.generate();
            generator.gen("BF "+lblInicioElse);
            sentenciaThen.generate();
            generator.gen("JUMP "+lblFinElse);
            generator.gen(lblInicioElse+": NOP");
            sentenciaElse.generate();
            generator.gen(lblFinElse+": NOP");
        }else{
            condicion.generate();
            generator.gen("BF "+lblFinElse);
            sentenciaThen.generate();
            generator.gen("JUMP "+lblFinElse);
            generator.gen(lblFinElse+": NOP");
        }
    }
}
