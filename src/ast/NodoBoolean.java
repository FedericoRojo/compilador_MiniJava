package ast;

import model.BooleanType;
import model.Token;
import model.Type;
import sourcemanager.GeneratorManager;

public class NodoBoolean extends NodoPrimitivo{

    public NodoBoolean(Token t){
        super(t);
    }

    @Override
    public Type check() {
        return new BooleanType();
    }

    public void generate(){
        int lexema = token.getLexeme().equals("true") ? 1 : 0;
        GeneratorManager.getInstance().gen( "PUSH "+lexema );
    }
}
