package ast;

import exceptions.SemanticException;
import model.NullType;
import model.Token;
import model.Type;

public class NodoNull extends NodoPrimitivo{

    public NodoNull(Token tk){
        super(tk);
    }


    @Override
    public Type check() throws SemanticException {
        return new NullType();
    }
}
