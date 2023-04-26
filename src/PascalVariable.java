public class PascalVariable{
    private String name;
    private String type;
    private String value;
    public PascalVariable(String name, String type){
        this.name = name;
        this.type = type;
    }
    //getter and setter methods
    public String getName(){
        return name;
    }
    public String getType(){
        return type;
    }
    public String getValue(){
        return value;
    }
    public void setValue(String value){
        this.value = value;
    }

}