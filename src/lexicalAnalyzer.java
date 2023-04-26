/**
 * Project 1: Lexical Analyzer
 * This program will read in a file containing pascal syntax and output the tokens
 * @author Aviraj Kar (10:45 class), Muhammad Umair (9:15 class)
 */


import java.io.*;
import java.util.*;
//lexical analyzer for the pascal language
class lexicalAnalyzer{

    //private String stream = "";
    private String prevLetter = "";
    private String prevDigit = "";
    private char specialCharCase;
    private boolean flagSpecialCharCase = false;
    private List<String> lines = new ArrayList<String>();
    private int currentLine = 0;

    ArrayList<token> tokens = new ArrayList<>();
    public lexicalAnalyzer(){
        try {
            BufferedReader br = new BufferedReader(new FileReader("ParsePascalCode.txt"));

            String line = "";
            while((line = br.readLine()) != null){
            // add the line to the list of lines
                //stream += line;
                line = line.toLowerCase();
                lines.add(line);
            }
            //stream = stream.toLowerCase();
            br.close();
            } 
            catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
    }
    private static final Map<String, String> reserved = new HashMap<String, String>();

    // list of reserved words
    private static final String[] reservedWords = {"and", "array", "begin", "case", "const", "div", "do", "downto", "else", "end", "file", "for", "function", "goto", "if", "in", "label", "mod", "nil", "not", "of", "or", "packed", "procedure", "program", "record", "repeat", "set", "then", "to", "type", "until", "var", "while", "with"};
    
    //put the reservedWords into reserved
    public HashMap<String, String> reservedMap = new HashMap<>();
    //put
    private static final HashMap<String, String> operatorMap = new HashMap<>();

    private static final HashMap<Character, String> spCharMap = new HashMap<>();

    private static final HashMap<String, String> dataTypeMap = new HashMap<>();

    //fill hashmaps with token names and lexemes
    void fillToken(){
        for(String word : reservedWords) reservedMap.put(word, word + "_reserved");
        for(String word : dataTypes) dataTypeMap.put(word, word + "_type");

        //fill operators with names
        operatorMap.put("+", "plus_OP");
        operatorMap.put("-", "minus_OP");
        operatorMap.put("*", "multi_OP");
        operatorMap.put("/", "div_OP");
        operatorMap.put("=", "equals_OP");
        operatorMap.put("%", "modulo_OP");
        operatorMap.put("<", "less_than_OP");
        operatorMap.put(">", "greater_than_OP");
        operatorMap.put("<=", "less_than_EQ_OP");
        operatorMap.put(">=", "greater_than_EQ_OP");
        operatorMap.put("<>", "not_EQ_OP");
        operatorMap.put(":=", "assign_OP");
        operatorMap.put("and", "and_OP");
        operatorMap.put("or", "or_OP");
        operatorMap.put("not", "not_OP");

        spCharMap.put(':', "colon_SP");
        spCharMap.put(';', "semicolon_SP");
        spCharMap.put('(', "left_paren");
        spCharMap.put(')', "right_paren");
        spCharMap.put(',', "comma_SP");
        spCharMap.put('.', "dot_SP");
        spCharMap.put('\'', "single_quote_SP");
        spCharMap.put('\"', "double_quote_SP");


    }

    // list of special symbols
    private static final char[] specialSymbols = {' ', '+', '-', '*', '/', '=', '<', '>', ':', ';', '%', '(', ')', ',', '.', '\'', '\"'};

    //list of operators
    private static final String[] operators = {"+", "-", "*", "%", "/", "=", "<", ">", "<=", ">=", "<>", ":=", "and", "or", "not"};


    //list of data types
    private static final String[] dataTypes = {"integer", "real", "boolean", "char", "string"};

    // is the character a letter
    private static boolean isLetter(char c){
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private int lexCounter = 0;

    //is the character a digit
    private static boolean isDigit(char c){
        return (c >= '0' && c <= '9');
    }

    // is the character a special symbol
    private static boolean isSpecialSymbol(char c){
        for(int i = 0; i < specialSymbols.length; i++){
            if(c == specialSymbols[i]){
                return true;
            }
        }
        return false;
    }

    // is the string a reserved word
    private static boolean isReservedWord(String s){
        for(int i = 0; i < reservedWords.length; i++){
            if(s.equals(reservedWords[i])){
                return true;
            }
        }
        return false;
    }

    // is data type
    private static boolean isDataType(String s){
        for(int i = 0; i < dataTypes.length; i++){
            if(s.equals(dataTypes[i])){
                return true;
            }
        }
        return false;
    }


    // is the string an operator
    private static boolean isOperator(String s){
        String temp = s.replaceAll(" ","");
        for(int i = 0; i < operators.length; i++){
            if(temp.equals(operators[i])){
                return true;
            }
        }
        return false;
    }

    // is the string a comment
    private static boolean isStartComment(String s){
        String temp = s.replaceAll("\\s+","");
        return temp.equals("(*");
    }

    // is whitespace
    private static boolean isWhiteSpace(char c){
        return c == ' ';
    }

    //get next character
    private char nextChar(){
        // if (lexCounter < stream.length()) return stream.charAt(lexCounter++);
        // else return ' ';

        if (lexCounter < lines.get(currentLine).length()) return lines.get(currentLine).charAt(lexCounter++);
        else {
            currentLine++;
            if (currentLine == lines.size()) return ' ';
            else{
                lexCounter = 0;
                return lines.get(currentLine).charAt(lexCounter++);
            }
        }
    }
    //get next token
    public void nextToken(){
        //if (lexCounter < stream.length()){
          if (currentLine < lines.size()){
        //get the next character
                int lineNumber = currentLine;
                char c = nextChar();
                //if the character is a letter
                if(isLetter(c)){
                    String s = "";
                    if (prevLetter != "") s = prevLetter;
                    prevLetter = "";
                    //while the character is a letter or a digit
                    while(isLetter(c) || isDigit(c)){
                        //add the character to the string
                        s += c;
                        //get the next character
                        c = nextChar();

                        //if the string is a reserved word
                        if(isReservedWord(s) && (isSpecialSymbol(c) || isWhiteSpace(c))){
                        //print the reserved word
                        System.out.println("Next token: " +reservedMap.get(s) + " Next lexeme: "+ s);
                        token t = new token(reservedMap.get(s), s, lineNumber);
                        tokens.add(t);
                        s = "";
                        if (isSpecialSymbol(c) && !isWhiteSpace(c)) {
                            System.out.println("Next Token:  " + spCharMap.get(c) + " Next lexeme: " + c);
                            token t1 = new token(spCharMap.get(c), c+"", lineNumber);
                            tokens.add(t1);
                        }
                        }
                        else if (isDataType(s) && (isSpecialSymbol(c) || isWhiteSpace(c))){
                            System.out.println("Next Token: " + dataTypeMap.get(s) + " Next lexeme: " + s);
                            token t = new token(dataTypeMap.get(s), s, lineNumber);
                            tokens.add(t);
                            s = "";
                            if (isSpecialSymbol(c) && !isWhiteSpace(c)) {
                                System.out.println("Next Token:  " + spCharMap.get(c) + " Next lexeme: " + c);
                                token t1 = new token(spCharMap.get(c), c+"", lineNumber);
                                tokens.add(t1);
                            }
                        }
                        else if (isOperator(s) && (isSpecialSymbol(c) || isWhiteSpace(c))){
                            System.out.println("Next Token: " + operatorMap.get(s) + " Next lexeme: " + s);
                            token t = new token(operatorMap.get(s), s, lineNumber);
                            tokens.add(t);
                            s = "";
                        }
                        else if (isStartComment(s) && (isSpecialSymbol(c) || isWhiteSpace(c))){
                            System.out.println("comment: " + s);
                            s = "";
                        }
                        else if (isSpecialSymbol(c) && !isWhiteSpace(c)){
                            //System.out.println("Special Character: " + c);
                            specialCharCase = c;
                            flagSpecialCharCase = true;

                        }
                    }
                    //if the string is not a reserved word
                    //print the identifier
                    if (s != "") {
                        System.out.println("Next Token: identifier Next lexeme: " + s);
                        token t = new token("identifier", s, lineNumber);
                        tokens.add(t);
                        if (isSpecialSymbol(c) && !isWhiteSpace(c)){
                            System.out.println("Next Token: " + spCharMap.get(c) + " Next lexeme: " + c);
                            token t1 = new token(spCharMap.get(c), c+"", lineNumber);
                            tokens.add(t1);
                    }
                }
                }
                //if the character is a digit
                else if(isDigit(c) || prevDigit != ""){
                    String s = "";
                    if (prevDigit != "") s = prevDigit;
                    prevDigit = "";
                    //while the character is a digit
                    while(isDigit(c)){
                        //add the character to the string
                        s += c;
                        //get the next character
                        c = nextChar();
                    }
                    //print the number
                    System.out.println("Next Token: int_literal Next lexeme: " + s);
                    token t = new token("int_literal", s, lineNumber);
                    tokens.add(t);
                    if (isSpecialSymbol(c) && !isWhiteSpace(c) && c != '\''){
                        System.out.println("Next Token: " + spCharMap.get(c) + " Next lexeme: " + c);
                        token t1 = new token(spCharMap.get(c), c+"", lineNumber);
                        tokens.add(t1);
                }
                }
                //go to the next character if whitespace
                else if (isWhiteSpace(c)){
                    System.out.print("");
                }
                //if the character is a special symbol
                else if(isSpecialSymbol(c)){
                    String s = "";

                        //add the character to the string
                        s += c;

                        if (s.equals(")")){
                            System.out.println("here");
                            token t = new token("right_paren", s, lineNumber);
                            tokens.add(t);
                        }
                        
                        else{

                        //get the next character
                        c = nextChar();
                        boolean flag = true;

                        if (isLetter(c)){
                            prevLetter += c;
                            flag = false;
                        }
                        if (isDigit(c)){
                            prevDigit += c;
                            flag = false;
                        }
                        if (flag){
                            s += c;
                        }
                    //if string literal
                    if (s.equals("('") || s.equals("(\"") || s.equals("'") || s.equals("\"")){
                        
                        //adding ( or "  or ' to the token list for string literal
                        if (s.length()== 1){
                            token ts = new token(spCharMap.get(s.charAt(0)), s.charAt(0)+"", lineNumber);
                            tokens.add(ts);
                        }
                        else{
                            for (int i = 0; i < s.length(); i++){
                                token ts = new token(spCharMap.get(s.charAt(i)), s.charAt(i)+"", lineNumber);
                                tokens.add(ts);
                            }
                        }
                        
                        

                        s = "";
                        String literal = "";
                        literal += prevLetter;
                        prevLetter = "";
                        token ends;

                        while (true){
                            c = nextChar();
                            if (c == '\''){
                                //c = nextChar();
                                ends = new token(spCharMap.get(c), c+"", lineNumber);
                                break;
                                // if (c == ')'){
                                //     break;
                                // }
                            }
                            else if (c == '"'){
                                //c = nextChar();
                                ends = new token("double_quote_SP", c+"", lineNumber);
                                break;
                                // if (c == ')'){
                                //     break;
                                // }
                            }
                            // else if (c == ')'){
                            //     break;
                            // }
                            //add the character to the string
                            s += c;
                            literal += c;
                        }
                        System.out.println("Next Token: string_literal Next lexeme: " + literal + " next lex: " + c);
                        token t = new token("string_literal", literal, lineNumber);
                        tokens.add(t);
                        tokens.add(ends);
                    }
                    //if the string is a comment
                    else if(isStartComment(s)){

                        s = "";
                        if (prevLetter != "") s += prevLetter;
                        prevLetter = "";
                        boolean CommentFlag = false;
                        //while(lexCounter < stream.length()){
                        while(lexCounter < lines.get(currentLine).length()){
                            c = nextChar();
                            if (c == '*'){
                                c = nextChar();
                                if (c == ')'){
                                    CommentFlag = true;
                                    break;
                                }
                            }
                            // else if (c == ')'){
                            //     break;
                            // }

                            s += c;
                        }
                        if (CommentFlag)System.out.println("comment: " + s);
                        else{
                            System.out.println("Error: Incorrect comment syntax on '" + s + "'");
                        }
                    }
                    
                    //if the string is an operator
                    else if(isOperator(s)){
                        s = s.trim();
                        System.out.println("Next token: " + operatorMap.get(s) + " Next lexeme: " + s);
                        token t = new token(operatorMap.get(s), s, lineNumber);
                        tokens.add(t);
                    }
                    //if the string is not an operator
                    else{
                        s = s.replaceAll(" ","");

                        if (s.length() >1){ System.out.println("Unknown character: " + s);
                        token t = new token("unknown", s, lineNumber);
                        tokens.add(t);
                    }
                        else if (s != ""){
                            System.out.println("Next token: " + spCharMap.get(s.charAt(0)) + " Next lexeme: "+ s);
                            token t = new token(spCharMap.get(s.charAt(0)), s, lineNumber);
                            tokens.add(t);
                    }
                }
                }
            }
                else if (!isWhiteSpace(c)){
                    System.out.println("Unknown character: " + c);
                }
        
            }
    }

    // parse
    public void parse(lexicalAnalyzer lex){
        //if (lexCounter < stream.length()){
        if (currentLine < lines.size()){
            lex.nextToken();
            // Call the parser
            parse(lex);
        }
    }
    
    //main method
    public static void main(String[] args){

        // Create a new lexical analyzer
        lexicalAnalyzer lex = new lexicalAnalyzer();
        lex.fillToken();

        lex.parse(lex);
        // for (int i = 0; i < lex.lines.size(); i++) {
        //     System.out.println(lex.lines.get(i) + " line number: " + i);
            
        // }
        for (int i = 0; i < lex.tokens.size(); i++) {
            System.out.println(lex.tokens.get(i).getType() + " " + lex.tokens.get(i).getLex() + " " + lex.tokens.get(i).getLineNum());
        }
    }

    // test footer
    
}