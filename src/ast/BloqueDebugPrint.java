package ast;

import sourcemanager.GeneratorManager;

public class BloqueDebugPrint extends Bloque{

    public void generate(){
        GeneratorManager generator = GeneratorManager.getInstance();
        generator.gen("LOAD 3");
        generator.gen("IPRINT");
    }
}
