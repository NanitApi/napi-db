package napi.db;

public enum JdbcDriver {

    MYSQL("mysql"),
    POSTGRESQL("postgresql"),
    MONGO("mongo"),
    SQLITE("sqlite"),
    H2("h2");

    private final String name;

    JdbcDriver(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

}
