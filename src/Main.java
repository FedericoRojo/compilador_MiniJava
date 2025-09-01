import exceptions.LexicalException;
import sourcemanager.SourceManager;
import sourcemanager.SourceManagerImpl;

import java.io.IOException;


public class Main {

    public static void main(String[] args) {


        if(args.length == 0){
            System.out.println("Falto proveer un archivo fuente");
            System.exit(1);
        }



        try{
            String fileName = args[0];
            SourceManager sourceManager = new SourceManagerImpl();
            sourceManager.open(fileName);
            AnalizadorLexico aLexico = new AnalizadorLexico(sourceManager);
            boolean sinErrores = true;

            Token token = null;

                do{

                    try {

                        token = aLexico.getNextToken();
                        System.out.println(token);

                    }catch (IOException e) {

                        sinErrores = false;
                        System.out.println(e.getMessage());

                    } catch (LexicalException e) {

                        sinErrores = false;
                        printError(e);
                        token = new Token("error", "error", 0);

                    }

                }while( !token.id.equals("EOF"));



            if(sinErrores){
                success();
            }else{
                failed();
            }

        }catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void printError(LexicalException e){
        System.out.println(e.getMessage());
        if(e.message != ""){
            System.out.println("Detalle extra: "+e.message);
        }
        System.out.println("[Error:"+e.lexeme+"|"+e.lineNumber+"]");
    }

    public static void success(){
        System.out.println("[SinErrores]");
    }

    public static void failed(){
        System.out.println("[ConErrores]");
    }

}