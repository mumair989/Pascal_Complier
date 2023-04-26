import java.util.ArrayList;
import java.util.List;

public class syntaxAnalyzer {
    
    lexicalAnalyzer lex = new lexicalAnalyzer();
    
    public void lexicalA(){
        lex.fillToken();
        lex.parse(lex);
    }

    
    int tokenNumber = 0;

    String currentTokenType;
    String currentLex;
    int currentLineNum;

    List <PascalVariable> variables = new ArrayList <PascalVariable>();
    List <PascalConstant> constants = new ArrayList <PascalConstant>();

    public String getCurrentTokenType(){
        return lex.tokens.get(tokenNumber).getType();
    }
    public String getCurrentLex(){
        return lex.tokens.get(tokenNumber).getLex();
    }
    public int getCurrentLineNum(){
        return lex.tokens.get(tokenNumber).getLineNum();
    }
    public void getNextToken(){
        tokenNumber++;
        currentTokenType = getCurrentTokenType();
        currentLex = getCurrentLex();
        currentLineNum = getCurrentLineNum();
    }

    public void analyzeSyntax() {
        program();
    }

    // Program -> Program Identifier ; declarations .
    public void program() {
        
        currentTokenType = getCurrentTokenType();
        currentLex = getCurrentLex();
        if (!currentTokenType.equals("program_reserved")) {
            System.out.println("Error: Expected 'PROGRAM' at the beginning of the program but found '" + currentLex + "'");
        }
        else{
            getNextToken();
            if (!currentTokenType.equals("identifier")) {
                System.out.println("Error: Expected an identifier after 'PROGRAM' but found '" + currentLex + "'");
            }
            else{
            getNextToken();
            if (!currentTokenType.equals("semicolon_SP")) {
                System.out.println("Error: Expected a ';' after the identifier but found '" + currentLex + "'");
            }
            else{
                getNextToken();
            }
        }
    }
        declarations(); 
        
    }

    // Declarations -> ConstDeclarations VarDeclarations SubprogramDeclarations
    public void declarations() {
        constDeclarations();
        varDeclarations();
        //subprogramDeclarations();
        //blockStatements();
    }
    // ConstDeclarations -> const constants; 
    public void constDeclarations() {
        if (currentTokenType.equals("const_reserved")) {
            getNextToken();
            do {
                constCheck();
            } while (currentTokenType.equals("identifier"));
        }
    }
    // constCheck -> identifier = 
    public void constCheck(){
        if (lex.reservedMap.containsKey(currentLex)){
            return;
        }
        if (!currentTokenType.equals("identifier")){
            System.out.println("Error: Expected an identifier after 'CONST' but found '" + currentLex + "'");
            getNextToken();

        }
        else{
            System.out.println(currentTokenType);
            int constLine = currentLineNum;
            String constantName = getCurrentLex();
            getNextToken();
            boolean flag = true;
            if (constLine != currentLineNum){ 
                System.out.println("Line: " + constLine + " Error: Expected an '=' sign after the identifier but None was found");
                System.out.println("Line: " + constLine + " Error: Expected a constant but None was found");
                System.out.println("Line: " + constLine + " Error: Expected a ';' at the end of the constant declaration but None was found");

                constCheck();
            }
            else{
                if (!currentTokenType.equals("equals_OP")){
                System.out.println("Line: " + constLine + " Error: Expected an '=' sign after the identifier but found '" + currentLex + "'");
                flag = false;
            }
            getNextToken();
            if (constLine != currentLineNum){
                System.out.println("Line: " + constLine + " Error: Expected a constant but None was found");
                System.out.println("Line: " + constLine + " Error: Expected a ';' at the end of the line but None was found");
                constCheck();
            }
            else{
                if (!(currentTokenType.equals("int_literal") || currentTokenType.equals("string_literal"))){
                    System.out.println("Line: " + constLine + " Error: Expected a constant value after the equal sign but found '" + currentLex + "'");
                    flag = false;
                }
                String constantValue = getCurrentLex();
                getNextToken();
                if (constLine != currentLineNum){
                    System.out.println("Line: " + constLine + " Error: Expected a ';' at the end of the constant declaration but None was found");
                    constCheck();
                }
                else{
                if (!currentTokenType.equals("semicolon_SP")){
                    System.out.println("Line: " + constLine + " Error: Expected a ';' after the number but found '" + currentLex + "'");
                    flag = false;
                }
                //add constant to the list | can be modified to only add if the constant is valid
                constants.add(new PascalConstant(constantName, constantValue));
                getNextToken();
            }
        }
    }
}
    }

    // VarDeclarations -> var variables;
    public void varDeclarations() {
        if (currentTokenType.equals("var_reserved")) {
            getNextToken();
            while (currentTokenType.equals("identifier")) {
                varCheck();
            }
        }
    }

    // varCheck -> identifier : type;
    public void varCheck(){
        if (lex.reservedMap.containsKey(currentLex)){
            return;
        }
        if (!currentTokenType.equals("identifier")){

            System.out.println("Error: Expected an identifier after 'VAR' but found '" + currentLex + "'");
        }
        else{
            System.out.println(currentTokenType);
            int varLine = currentLineNum;
            String variableName = getCurrentLex();
            getNextToken();

            if (currentTokenType.equals("comma_SP"))varCheck();

            boolean flag = true;
            if (varLine != currentLineNum){ 
                System.out.println("Line: " + varLine + " Error: Expected an ':' sign after the identifier but None was found");
                System.out.println("Line: " + varLine + " Error: Expected a type but None was found");
                System.out.println("Line: " + varLine + " Error: Expected a ';' at the end of the variable declaration but None was found");

                varCheck();
            }
            else{
                if (!currentTokenType.equals("colon_SP")){
                    System.out.println("Line: " + varLine + " Error: Expected a ':' after '" +  variableName +  "' but found '" + currentLex + "'");
                    flag = false;
            }
            getNextToken();
            if (varLine != currentLineNum){
                System.out.println("Line: " + varLine + " Error: Expected a type but None was found");
                System.out.println("Line: " + varLine + " Error: Expected a ';' at the end of the line but None was found");
                varCheck();
            }
            else{
                if (!(currentTokenType.equals("integer_type") || currentTokenType.equals("real_type") || currentTokenType.equals("boolean_type") || currentTokenType.equals("char_type") || currentTokenType.equals("string_type"))){
                    System.out.println("Line: " + varLine + " Error: Expected a type after ':' but found " + currentLex);
                    flag = false;
                }
                String varType = getCurrentLex();
                getNextToken();
                if (varLine != currentLineNum){
                    System.out.println("Line: " + varLine + " Error: Expected a ';' at the end of the variable declaration but None was found");
                    
                    //adding variable to the list when the semicolon is missing
                    variables.add(new PascalVariable(variableName, varType));
                    varCheck();
                }
                else{
                if (!currentTokenType.equals("semicolon_SP")){
                    System.out.println("Line: " + varLine + " Error: Expected a ';' after the number but found '" + currentLex + "'");
                    flag = false;
                }
                // no matter what variables are added to the list, since they are used in the following instructions
                variables.add(new PascalVariable(variableName, varType));
                getNextToken();
            }
        }
    }
}




        // else{
        //     String variableName = getCurrentLex();
        //     getNextToken();
        //     if (!currentTokenType.equals("colon_SP")){
        //         System.out.println("Error: Expected a ':' after '" +  variableName +  "' but found '" + currentLex + "'");
        //     }
        //     else{
        //         getNextToken();
        //         if (!(currentTokenType.equals("integer_type") || currentTokenType.equals("real_type") || currentTokenType.equals("boolean_type") || currentTokenType.equals("char_type") || currentTokenType.equals("string_type"))){
        //             System.out.println("Error: Expected a type after ':' but found " + currentLex);
        //         }
        //         else{
        //             String variableType = getCurrentLex();
        //             getNextToken();
        //             if (!currentTokenType.equals("semicolon_SP")){
        //                 System.out.println("Error: Expected a ';' after the type but found '" + currentLex + "'");
        //             }
        //             else{
        //                 variables.add(new PascalVariable(variableName, variableType));
        //                 getNextToken();
        //             }
        //         }
        //     }
        // }
    }

/*     public void blockStatements(){
        if(currentTokenType.equals("begin_reserved")){
            System.out.println(currentTokenType);
            statements();
        }
    }*/

/*     private void statements() {
        if (currentTokenType.equals("end_reserved")){System.out.println("here");
        analyzeSyntax();
    } 
        else if (tokenNumber == lex.tokens.size()) System.out.println("Error: Expected 'END' at the end of the program");;
        statement();
    }*/

/*     public void statement(){
        if (currentTokenType.equals("identifier")){
            assignmentStatement();
        }
        else if (currentTokenType.equals("if_reserved")){
            ifStatement();
        }
        else if (currentTokenType.equals("while_reserved")){
            whileStatement();
        }
        else if (currentTokenType.equals("for_reserved")){
            forStatement();
        }
        else if (currentTokenType.equals("begin_reserved")){
            blockStatements();
        }
        else if (currentTokenType.equals("end_reserved")){
            getNextToken();
        }
        else{
            System.out.println("Error: Expected a statement but found '" + currentLex + "'");
        }
    } */

    public static void main(String args[]){
        
        syntaxAnalyzer syn = new syntaxAnalyzer();
        
        syn.lexicalA();
        // lex.tokens.forEach((token) -> {
        //     System.out.println(token.getType() + " " + token.getLex());
        // });
        syn.analyzeSyntax();
        syn.constants.forEach((constant) -> {
            System.out.println(constant.getName() + " " + constant.getValue());
        });
        syn.variables.forEach((variable) -> {
            System.out.println(variable.getName() + " " + variable.getType());
        });
    }
}
