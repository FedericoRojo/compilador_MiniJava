package TablaSimbolo;

import model.*;

import java.util.HashMap;
import java.util.Map;

public class TypeTable {
    private final Map<String, Type> types = new HashMap<>();

    public TypeTable() {
        types.put("int", new IntType());
        types.put("char", new CharType());
        types.put("boolean", new BooleanType());
        types.put("void", new VoidType());
    }

    public Type getOrCreateReferenceType(Token token) {
        if( token.getLexeme().equals("int") || token.getLexeme().equals("char") || token.getLexeme().equals("boolean") || token.getLexeme().equals("void")){
            return types.get(token.getLexeme());
        } else {
            Type newType = new ReferenceType(token);
            types.put(token.getLexeme(), newType);
            return newType;
        }
    }

}

