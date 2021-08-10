package napi.db;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * JDBC url representation. Can be used to build JDBC connection URL
 */
public final class JdbcUrl {

    private final String url;

    private JdbcUrl(String url){
        this.url = url;
    }

    @Override
    public String toString() {
        return url;
    }

    /**
     * Get URL builder for remote databases
     * @return Builder for remote databases
     */
    public static BuilderRemote remote(){
        return new BuilderRemote();
    }

    /**
     * Get URL builder for local (file) databases
     * @return Builder for local databases
     */
    public static BuilderLocal local(){
        return new BuilderLocal();
    }

    public static class BuilderRemote {

        private String driver;
        private String host;
        private String protocol;
        private int port;
        private String database;

        public BuilderRemote driver(String driver){
            this.driver = driver;
            return this;
        }

        public BuilderRemote driver(JdbcDriver driver){
            this.driver = driver.getName();
            return this;
        }

        public BuilderRemote host(String host){
            this.host = host;
            return this;
        }

        public BuilderRemote protocol(String host){
            this.protocol = protocol;
            return this;
        }

        public BuilderRemote port(int port){
            this.port = port;
            return this;
        }

        public BuilderRemote database(String database){
            this.database = database;
            return this;
        }

        public JdbcUrl build() {
            StringBuilder builder = new StringBuilder();

            builder.append("jdbc:");
            builder.append(driver);

            if (protocol != null){
                builder.append(":");
                builder.append(protocol);
            }

            builder.append("://");
            builder.append(host);

            if (port > 0){
                builder.append(":");
                builder.append(port);
            }

            builder.append("/");
            builder.append(database);

            return new JdbcUrl(builder.toString());
        }

    }

    public static class BuilderLocal {

        private String driver;
        private Path path;

        public BuilderLocal driver(String driver){
            this.driver = driver;
            return this;
        }

        public BuilderLocal driver(JdbcDriver driver){
            this.driver = driver.getName();
            return this;
        }

        public BuilderLocal path(Path path){
            this.path = path;
            return this;
        }

        public BuilderLocal path(String path){
            this.path = Paths.get(path);
            return this;
        }

        public BuilderLocal path(URI uri){
            this.path = Paths.get(uri);
            return this;
        }

        public JdbcUrl build() {
            StringBuilder builder = new StringBuilder();
            builder.append("jdbc:");
            builder.append(driver);
            builder.append(":");
            builder.append(path.toAbsolutePath());
            return new JdbcUrl(builder.toString());
        }

    }

}
