package sample.obj;

public class ProductLine {
    private int id;
    private String name;
    private String ip;
    private String dataBaseName;
    private String user;
    private String password;

    public ProductLine (int id, String name, String ip, String dataBaseName, String user, String password){
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.dataBaseName = dataBaseName;
        this.user = user;
        this.password = password;
    }

    public ProductLine(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDataBaseName() {
        return dataBaseName;
    }

    public void setDataBaseName(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
