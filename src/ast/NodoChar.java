package ast;

import exceptions.SemanticException;
import model.CharType;
import model.Token;
import model.Type;

public class NodoChar extends NodoPrimitivo{


    public NodoChar(Token tk){
        super(tk);
    }

    @Override
    public Type check() throws SemanticException {
        return new CharType();
    }
}
