package ast;

import TablaSimbolo.TablaSimbolo;
import exceptions.SemanticException;
import model.ReferenceType;
import model.Token;
import model.Type;
import sourcemanager.GeneratorManager;

public class NodoString extends NodoPrimario{
    String label;
    private static int contador = 0;

    public NodoString(Token tk){
        super(tk);
        label = "string"+contador;
        contador++;
    }

    @Override
    public Type check() throws SemanticException {
        if(encadenado != null){
            throw new SemanticException(super.token, "Se quiere acceder a un atributo o metodo encadenado en la clase String");
        }
        ReferenceType refType = new ReferenceType( new Token("String", "String", super.token.getLineNumber()));
        refType.setAssociatedClass(TablaSimbolo.getInstance().getClassByString("String"));
        return refType;
    }

    public void generate(){
        GeneratorManager generator = GeneratorManager.getInstance();
        generator.gen(".DATA");
        generator.gen(label+": DW "+token.getLexeme()+", 0");
        generator.gen(".CODE");
        generator.gen("PUSH "+label);
    }
}
