package ast;

import sourcemanager.GeneratorManager;

public class BloqueRead extends Bloque{

    public void generate(){
        GeneratorManager generator = GeneratorManager.getInstance();
        generator.gen("READ");
        generator.gen("PUSH 48");
        generator.gen("SUB");
        generator.gen("STORE 3");
        generator.gen("PRNLN");
    }
}
