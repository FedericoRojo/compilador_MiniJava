package ast;

import exceptions.SemanticException;
import model.CharType;
import model.Token;
import model.Type;
import sourcemanager.GeneratorManager;

public class NodoChar extends NodoPrimitivo{


    public NodoChar(Token tk){
        super(tk);
    }

    @Override
    public Type check() throws SemanticException {
        return new CharType();
    }

    public void generate(){
        char character = token.getLexeme().substring(1, token.getLexeme().length() - 1).charAt(0);
        int unicode = character;
        GeneratorManager.getInstance().gen( "PUSH "+unicode );
    }
}
