package ast;

import sourcemanager.GeneratorManager;

public class BloquePrintC extends Bloque{


    public void generate(){
        GeneratorManager generator = GeneratorManager.getInstance();
        generator.gen("LOAD 3");
        generator.gen("CPRINT");
    }
}
