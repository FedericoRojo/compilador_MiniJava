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

    public Type getOrCreateReferenceType(String typeName) {
        if (types.containsKey(typeName)) {
            return types.get(typeName);
        } else {
            Type newType = new ReferenceType(typeName);
            types.put(typeName, newType);
            return newType;
        }
    }

}

