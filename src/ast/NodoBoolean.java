package ast;

import model.BooleanType;
import model.Token;
import model.Type;

public class NodoBoolean extends NodoPrimitivo{

    public NodoBoolean(Token t){
        super(t);
    }

    @Override
    public Type check() {
        return new BooleanType();
    }

    public void generate(){
        
    }
}
