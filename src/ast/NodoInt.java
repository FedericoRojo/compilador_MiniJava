package ast;

import exceptions.SemanticException;
import model.IntType;
import model.Token;
import model.Type;
import sourcemanager.GeneratorManager;

public class NodoInt extends NodoPrimitivo{

    public NodoInt(Token tk){
        super(tk);
    }

    @Override
    public Type check() throws SemanticException {
        return new IntType();
    }


    public void generate(){
        GeneratorManager.getInstance().gen( "PUSH "+token.getLexeme() );
    }
}
