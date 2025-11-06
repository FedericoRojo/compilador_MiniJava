package sourcemanager;

import TablaSimbolo.TablaSimbolo;
import exceptions.SemanticException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GeneratorManager {
    private static GeneratorManager instance;

    private BufferedWriter writer;

    private GeneratorManager(String s){
        createFile(s);
    }

    public static GeneratorManager getInstance(String fileName){
        if(instance == null){
            instance = new GeneratorManager(fileName);
        }
        return instance;
    }


    public static GeneratorManager getInstance(){
        if(instance == null){
            throw new IllegalStateException("GeneratorManager no fue inicializado");
        }
        return instance;
    }

    public void createFile(String name){
        try{
            writer = new BufferedWriter(new FileWriter(name));
        }catch(IOException e){
            System.out.println("Error al crear archvio: "+e.getMessage());
        }
    }

    public void gen(String line){
        if( writer == null ){
            System.out.println("Error: no se ha creado ningun archivo");
            return;
        }
        try{
            writer.write(line);
            writer.newLine();
        }catch(IOException e){
            System.out.println("Error al escribir en el archivo: "+e.getMessage());
        }
    }

    public void close(){
        if(writer != null){
            try{
                writer.close();
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
    }


    public static void removeInstance(){
        instance = null;
    }

}
