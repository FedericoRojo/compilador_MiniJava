package analizers;

import exceptions.LexicalException;
import model.Token;
import sourcemanager.SourceManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class LexicAnalyzer {

    private static final Map<String, String> KEYWORDS = new HashMap<>();
    String lexeme;
    char currentChar;
    SourceManager sourceManager;

    public LexicAnalyzer(SourceManager sm) throws IOException{
        this.sourceManager = sm;
        updateCurrentChar();
    }

    static {
        KEYWORDS.put("class", "class");
        KEYWORDS.put("extends", "extends");
        KEYWORDS.put("public", "public");
        KEYWORDS.put("static", "static");
        KEYWORDS.put("void", "void");
        KEYWORDS.put("boolean", "boolean");
        KEYWORDS.put("char", "char");
        KEYWORDS.put("int", "int");
        KEYWORDS.put("abstract", "abstract");
        KEYWORDS.put("final", "final");
        KEYWORDS.put("if", "if");
        KEYWORDS.put("else", "else");
        KEYWORDS.put("while", "while");
        KEYWORDS.put("return", "return");
        KEYWORDS.put("var", "var");
        KEYWORDS.put("this", "this");
        KEYWORDS.put("new", "new");
        KEYWORDS.put("null", "null");
        KEYWORDS.put("true", "true");
        KEYWORDS.put("false", "false");
    }

    public Token getNextToken() throws LexicalException, IOException{
        lexeme = "";
        return start();
    }

    private Token start() throws LexicalException, IOException {

        if(Character.isLowerCase(currentChar)){
            updateLexeme();
            updateCurrentChar();
            return idMethodVariable();
        }else if(Character.isUpperCase(currentChar)){
            updateLexeme();
            updateCurrentChar();
            return idClass();
        }else if(Character.isDigit(currentChar)){
            updateLexeme();
            updateCurrentChar();
            return intLiteral(1);
        }else if( currentChar ==  '\'' ){
            updateLexeme();
            updateCurrentChar();
            return charLiteral();
        }else if( currentChar == '\"' ){
            updateLexeme();
            updateCurrentChar();
            return stringLiteral();
        }else if( currentChar == '/'){
            updateLexeme();
            updateCurrentChar();
            return comment();
        }else if(currentChar == sourceManager.END_OF_FILE ){
            return end();
        }else if(Character.isWhitespace(currentChar)){
            updateCurrentChar();
            return start();
        }else if(currentChar == '('){
            return singleCharToken("(");
        }else if(currentChar == ')'){
            return singleCharToken(")");
        }else if(currentChar == '{'){
            return singleCharToken("{");
        }else if(currentChar == '}'){
            return singleCharToken("}");
        }else if(currentChar == ';'){
            return singleCharToken(";");
        }else if(currentChar == ','){
            return singleCharToken(",");
        }else if(currentChar == '.'){
            return singleCharToken(".");
        }else if(currentChar == ':'){
            return singleCharToken(":");
        }else if(currentChar == '?'){
            return singleCharToken("?");
        }if(currentChar == '+'){
            updateLexeme();
            updateCurrentChar();
            return possibleDoubleAdd();
        }else if(currentChar == '-'){
            updateLexeme();
            updateCurrentChar();
            return possibleDoubleSub();
        }else if(currentChar == '*'){
            return singleCharToken("*");
        }else if(currentChar == '%'){
            return singleCharToken("%");
        }else if(currentChar == '='){
            updateLexeme();
            updateCurrentChar();
            return possibleDoubleEquals();
        }else if(currentChar == '!'){
            updateLexeme();
            updateCurrentChar();
            return notOrDifferent();
        }else if(currentChar == '<'){
            updateLexeme();
            updateCurrentChar();
            return possibleMinorOrEquals();
        }else if(currentChar == '>'){
            updateLexeme();
            updateCurrentChar();
            return possibleGreaterOrEquals();
        }else if(currentChar == '&'){
            updateLexeme();
            updateCurrentChar();
            return and();
        }else if(currentChar == '|'){
            updateLexeme();
            updateCurrentChar();
            return or();
        }else{
            updateLexeme();
            int lineNumber = sourceManager.getLineNumber();
            int colIndex = sourceManager.getColumnIndex();
            String currentLine = sourceManager.getCurrentLine();
            updateCurrentChar();
            throw new LexicalException(lexeme, lineNumber, "",
                                        colIndex, currentLine);
        }
    }

    private Token and() throws LexicalException, IOException{
        if(currentChar == '&'){
            updateLexeme();
            updateCurrentChar();
            return doubleAnd();
        }else{
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), "Solo un &, deberia usar dos &&",
                                        sourceManager.getColumnIndex(), sourceManager.getCurrentLine());
        }
    }

    private Token doubleAnd() throws IOException{
        return new Token("&&", lexeme, sourceManager.getLineNumber());
    }

    private Token or() throws LexicalException, IOException{
        if(currentChar == '|'){
            updateLexeme();
            updateCurrentChar();
            return doubleOr();
        }else{
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), "Solo un |, deberia usar 2 ||",
                    sourceManager.getColumnIndex(), sourceManager.getCurrentLine());
        }
    }

    private Token doubleOr() throws IOException{
        return new Token("||", lexeme, sourceManager.getLineNumber());
    }

    private Token possibleGreaterOrEquals() throws IOException{
        if(currentChar == '='){
            updateLexeme();
            updateCurrentChar();
            return greaterOrEquals();
        }else{
            return new Token(">", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token greaterOrEquals() throws IOException{
        return new Token(">=", lexeme, sourceManager.getLineNumber());
    }

    private Token possibleMinorOrEquals() throws IOException{
        if(currentChar == '='){
            updateLexeme();
            updateCurrentChar();
            return minorOrEquals();
        }else{
            return new Token("<", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token minorOrEquals() throws IOException{
        return new Token("<=", lexeme, sourceManager.getLineNumber());
    }


    private Token possibleDoubleSub() throws IOException{
        if(currentChar == '-'){
            updateLexeme();
            updateCurrentChar();
            return doubleSub();
        }else{
            return new Token("-", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token doubleSub() throws IOException{
        return new Token("--", lexeme, sourceManager.getLineNumber());
    }

    private Token notOrDifferent() throws IOException{
        if(currentChar == '='){
            updateLexeme();
            updateCurrentChar();
            return different();
        }else{
            return new Token("!", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token different() throws IOException{
        return new Token("!=", lexeme, sourceManager.getLineNumber());
    }

    private Token possibleDoubleEquals() throws IOException{
        if(currentChar == '='){
            updateLexeme();
            updateCurrentChar();
            return doubleEquals();
        }else{
            return new Token("=", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token doubleEquals() throws IOException{
        return new Token("==", lexeme, sourceManager.getLineNumber());
    }


    private Token possibleDoubleAdd() throws IOException{
        if(currentChar == '+'){
            updateLexeme();
            updateCurrentChar();
            return doubleAdd();
        }else{
            return new Token("+", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token doubleAdd() throws IOException{
        return new Token("++", lexeme, sourceManager.getLineNumber());
    }

    private Token singleCharToken(String type) throws IOException {
        updateLexeme();
        updateCurrentChar();
        return new Token(type, lexeme, sourceManager.getLineNumber());
    }

    private Token end() {
        return new Token("$", "", sourceManager.getLineNumber());
    }


    private Token idMethodVariable() throws IOException{
        if(Character.isDigit(currentChar) || Character.isLetter(currentChar) || currentChar == '_'){
            updateLexeme();
            updateCurrentChar();
            return idMethodVariable();
        }else{
            String type = KEYWORDS.getOrDefault(lexeme, "idMetVar");
            return new Token(type, lexeme, sourceManager.getLineNumber());
        }
    }

    private Token idClass() throws IOException{
        if(Character.isDigit(currentChar) || Character.isLetter(currentChar) || currentChar == '_'){
            updateLexeme();
            updateCurrentChar();
            return idClass();
        }else{
            return new Token("idClass", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token intLiteral(int nro) throws LexicalException, IOException{
        if(Character.isDigit(currentChar)){
            updateLexeme();
            if(nro >= 9){
                throw new LexicalException(lexeme, sourceManager.getLineNumber(), "El numero supera el limite permitido",
                        sourceManager.getColumnIndex(), sourceManager.getCurrentLine());
            }
            updateCurrentChar();
            return intLiteral(nro + 1);
        }else{
            return new Token("intLiteral", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token charLiteral() throws LexicalException, IOException{
        if( currentChar == '\\' ){
            updateLexeme();
            updateCurrentChar();
            return charLiteral2();
        }else if(currentChar == sourceManager.END_OF_FILE ){
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), "Caracter no cerrado, end of file encontrado",
                    sourceManager.getColumnIndex(), sourceManager.getCurrentLine());
        }else if( (currentChar == '\n') || (currentChar == '\r')){
            updateLexeme();
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), "Salto de linea no es un caracter válido",
                    sourceManager.getColumnIndex(), sourceManager.getCurrentLine());
        }else{
            updateLexeme();
            updateCurrentChar();
            return charLiteral3();
        }
    }

    private Token charLiteral2() throws LexicalException, IOException{
        if( (currentChar == '\n') || (currentChar == '\r')){
            updateLexeme();
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), "Salto de linea no es un caracter válido",
                    sourceManager.getColumnIndex(), sourceManager.getCurrentLine());
        }else if(currentChar == sourceManager.END_OF_FILE ){
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), "Caracter no cerrado, end of file encontrado",
                    sourceManager.getColumnIndex(), sourceManager.getCurrentLine());
        }if( currentChar == 'u'){
            updateLexeme();
            updateCurrentChar();
            return charLiteralPossibleUnicode(1);
        }else {
            updateLexeme();
            updateCurrentChar();
            return charLiteral3();
        }
    }

    private Token charLiteralPossibleUnicode(int nro) throws IOException, LexicalException{
        if(currentChar == '\'' && nro == 1){
            updateLexeme();
            updateCurrentChar();
            return charLiteralEnd();
        }else if(currentChar == sourceManager.END_OF_FILE ){
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), "Caracter unicode no cerrado, end of file encontrado",
                    sourceManager.getColumnIndex(), sourceManager.getCurrentLine());
        }else if( (currentChar == '\n') || (currentChar == '\r')){
            updateLexeme();
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), "Salto de linea no es un caracter válido",
                    sourceManager.getColumnIndex(), sourceManager.getCurrentLine());
        }else if( Character.isDigit(currentChar) ||
                  (currentChar >= 'a' && currentChar <= 'f') ||
                  (currentChar >= 'A' && currentChar <= 'F') ){

            updateLexeme();
            if(nro > 4){
                updateCurrentChar();
                throw new LexicalException(lexeme, sourceManager.getLineNumber(), "Unicode invalido, es muy largo",
                        sourceManager.getColumnIndex(), sourceManager.getCurrentLine());
            }
            updateCurrentChar();
            return charLiteralPossibleUnicode(nro+1);
        }else if(currentChar == '\'' && nro == 5){
            updateLexeme();
            updateCurrentChar();
            return charLiteralEnd();
        }else{
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), "Unicode invalido",
                    sourceManager.getColumnIndex()-1, sourceManager.getCurrentLine());
        }
    }

    private Token charLiteral3() throws LexicalException, IOException{
        if(  currentChar ==  '\''  ){
            updateLexeme();
            updateCurrentChar();
            return charLiteralEnd();
        }else if(currentChar == sourceManager.END_OF_FILE ){
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), "Caracter no cerrado, end of file encontrado",
                    sourceManager.getColumnIndex(), sourceManager.getCurrentLine());
        }else if( (currentChar == '\n') || (currentChar == '\r')){
            updateLexeme();
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), "Salto de linea no es un caracter válido",
                    sourceManager.getColumnIndex(), sourceManager.getCurrentLine());
        }else{
            updateLexeme();
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), "", sourceManager.getColumnIndex(),
                    sourceManager.getCurrentLine());
        }
    }

    private Token charLiteralEnd(){
        return new Token("charLiteral", lexeme, sourceManager.getLineNumber());
    }

    private Token stringLiteral() throws LexicalException, IOException{
       if(currentChar == '\\'){
            updateLexeme();
            updateCurrentChar();
            return stringLiteral1();
        }else if(currentChar == '\n'){
           throw new LexicalException(lexeme, sourceManager.getLineNumber(),
                   "String incorrecto, no se permite salto de linea",
                   sourceManager.getColumnIndex()+1, sourceManager.getCurrentLine());
        } if(currentChar == '"'){
            updateLexeme();
            updateCurrentChar();
            return stringLiteralEnd();
        }else if(currentChar == sourceManager.END_OF_FILE){
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), "String no cerrado, end of file encontrado",
                    sourceManager.getColumnIndex()+1, sourceManager.getCurrentLine());
        }else {
           updateLexeme();
           updateCurrentChar();
           return stringLiteral();
        }
    }

    private Token stringLiteral1() throws LexicalException, IOException{
        if(currentChar == sourceManager.END_OF_FILE){
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), "String no cerrado, end of file encontrado",
                    sourceManager.getColumnIndex()+1, sourceManager.getCurrentLine());
        }else if(currentChar == '\n'){
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), "String incorrecto, no se permite salto de linea",
                    sourceManager.getColumnIndex()+1, sourceManager.getCurrentLine());
        }else{
            updateLexeme();
            updateCurrentChar();
            return stringLiteral();
        }
    }

    private Token stringLiteralEnd(){
        return new Token("stringLiteral", lexeme, sourceManager.getLineNumber());
    }

    private Token comment() throws LexicalException, IOException{
        if(currentChar == '/'){
            updateCurrentChar();
            return commentInline();
        }if(currentChar == '*'){
            updateLexeme();
            updateCurrentChar();
            return commentMultiLine();
        }else{
            return new Token("/", lexeme, sourceManager.getLineNumber());
        }
    }

    private Token commentInline() throws LexicalException, IOException {
        if(currentChar == '\n'){
            lexeme = "";
            updateCurrentChar();
            return start();
        }else{
            updateCurrentChar();
            return commentInline();
        }
    }


    private Token commentMultiLine() throws LexicalException, IOException{
        if(currentChar == '*'){
            updateLexeme();
            updateCurrentChar();
            return commentMultiLine1();
        }else if(currentChar == sourceManager.END_OF_FILE){
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), "Comentario multilinea no cerrado, end of file encontrado",
                    sourceManager.getColumnIndex()+1, sourceManager.getCurrentLine());
        }else{
            updateLexeme();
            updateCurrentChar();
            return commentMultiLine();
        }
    }

    private Token commentMultiLine1() throws LexicalException, IOException{
        if( currentChar == '/'){
            updateCurrentChar();
            lexeme = "";
            return start();
        }else if(currentChar == sourceManager.END_OF_FILE){
            throw new LexicalException(lexeme, sourceManager.getLineNumber(), "Comentario multilinea no cerrado, end of file encontrado",
                    sourceManager.getColumnIndex()+1, sourceManager.getCurrentLine());
        }else{
            return  commentMultiLine();
        }
    }

    private void updateLexeme(){
        lexeme = lexeme + currentChar;
    }

    private void updateCurrentChar() throws IOException {
        currentChar = sourceManager.getNextChar();
    }

}
