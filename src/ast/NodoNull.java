package ast;

import exceptions.SemanticException;
import model.NullType;
import model.Token;
import model.Type;
import sourcemanager.GeneratorManager;

public class NodoNull extends NodoPrimitivo{

    public NodoNull(Token tk){
        super(tk);
    }


    @Override
    public Type check() throws SemanticException {
        return new NullType();
    }

    public void generate(){
        GeneratorManager.getInstance().gen("PUSH 0");
    }
}
