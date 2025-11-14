package ast;

import sourcemanager.GeneratorManager;

public class BloquePrintLn extends Bloque{

    public void generate(){
        GeneratorManager generator = GeneratorManager.getInstance();
        generator.gen("PRNLN");
    }
}
