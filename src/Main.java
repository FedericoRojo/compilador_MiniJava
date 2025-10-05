import TablaSimbolo.TablaSimbolo;
import analizers.LexicAnalyzer;
import analizers.SyntacticAnalyzer;
import exceptions.LexicalException;
import exceptions.SemanticException;
import exceptions.SyntacticException;
import sourcemanager.SourceManager;
import sourcemanager.SourceManagerImpl;

import java.io.IOException;


public class Main {

    public static void main(String[] args) {
/*
        if(args.length == 0){
            System.out.println("Falto proveer un archivo fuente");
            System.exit(1);
        }*/

        try{
            //String fileName = args[0];
            String fileName = "src/test/ejemplo.txt";

            SourceManager sourceManager = new SourceManagerImpl();
            sourceManager.open(fileName);
            LexicAnalyzer aLexico = new LexicAnalyzer(sourceManager);
            SyntacticAnalyzer sA = new SyntacticAnalyzer(aLexico);

            sA.start();

            TablaSimbolo.getInstance().checkWellDefined();
            success();

        }catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (LexicalException e) {
            printLexError(e);
        } catch (SyntacticException e) {
            printSyntacticError(e);
        }catch (SemanticException e){
            printSemanticException(e);
        }

    }

    public static void printLexError(LexicalException e){
        System.out.println(e.getMessage());
        if(!e.extraMessage.equals("")){
            System.out.println("Detalle extra: "+e.extraMessage);
        }
        System.out.println(e.message);
    }

    public static void printSemanticException(SemanticException e){
        System.out.println(e.getMessage());
    }

    public static void printSyntacticError(SyntacticException e){
        System.out.println(e.message);
        System.out.println(e.getMessage());
    }

    public static void success(){
        System.out.println("Compilación exitosa");
        System.out.println("[SinErrores]");
    }


}