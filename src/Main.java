import exceptions.LexicalException;
import exceptions.SyntacticException;
import model.Token;
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
            LexicAnalyzer aLexico = new LexicAnalyzer(sourceManager);
            SyntacticAnalyzer sA = new SyntacticAnalyzer(aLexico);
        }catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (LexicalException e) {
            throw new RuntimeException(e);
        } catch (SyntacticException e) {
            throw new RuntimeException(e);
        }

    }


}