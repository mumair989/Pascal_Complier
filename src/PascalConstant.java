public class PascalConstant{
    private String name;
    private String type;
    private String value;
    public PascalConstant(String name, String value){
        this.name = name;
        this.value = value;
        if (value.matches("[0-9]+")){
            type = "integer";
        }
        else if (value.matches("[0-9]+.[0-9]+")){
            type = "real";
        }
        else if (value.matches("true|false")){
            type = "boolean";
        }
        else if (value.matches("'.'")){
            type = "char";
        }
        else if (value.matches("\".*\"")){
            type = "string";
        }
    }
    public String getName(){
        return name;
    }
    public String getType(){
        return type;
    }
    public String getValue(){
        return value;
    }
}