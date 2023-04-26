public class token {
    private String type;
    private String lex;
    private int lineNum;
    public token(String type, String lex, int lineNum){
        this.type = type;
        this.lex = lex;
        this.lineNum = lineNum + 1;
    }
    public String getType(){
        return type;
    }
    public String getLex(){
        return lex;
    }
    public int getLineNum(){
        return lineNum;
    }
}
