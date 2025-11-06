package ast;

import TablaSimbolo.TablaSimbolo;
import exceptions.SemanticException;
import model.ReferenceType;
import model.Token;
import model.Type;

public class NodoString extends NodoPrimario{
    public NodoString(Token tk){
        super(tk);
    }

    @Override
    public Type check() throws SemanticException {
        if(encadenado != null){
            throw new SemanticException(super.token, "Se quiere acceder a un atributo o metodo encadenado en la clase String");
        }
        ReferenceType refType = new ReferenceType( new Token("String", "String", super.token.getLineNumber()));
        refType.setAssociatedClass(TablaSimbolo.getInstance().getClassByString("String"));
        return refType;
    }

    public void generate(){
    }
}
