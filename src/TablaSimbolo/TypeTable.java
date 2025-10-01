package TablaSimbolo;

import model.PrimitiveType;
import model.ReferenceType;
import model.Type;

import java.util.HashMap;
import java.util.Map;

public class TypeTable {
    private final Map<String, Type> types = new HashMap<>();

    public TypeTable() {
        types.put("int", new PrimitiveType(PrimitiveType.Primitive.INT));
        types.put("char", new PrimitiveType(PrimitiveType.Primitive.CHAR));
        types.put("boolean", new PrimitiveType(PrimitiveType.Primitive.BOOLEAN));
        types.put("void", new PrimitiveType(PrimitiveType.Primitive.VOID));
    }

    public Type getOrCreateReferenceType(String className) {
        if (types.containsKey(className)) {
            return types.get(className);
        } else {
            Type newType = new ReferenceType(className);
            types.put(className, newType);
            return newType;
        }
    }

}

