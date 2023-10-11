package connection;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

public class DbConnection {


    private final String NEON_CONNECTION_STRING = "postgres://jaysmith413:RneKN7sD6Qfd@ep-dawn-moon-31749400.us-east-2.aws.neon.tech/neondb";
    private DataSource dataSource;

    public DbConnection(){
        this.dataSource = getDbSource(false);
    }
    private DataSource getDbSource(boolean useLocalhost) {
        BasicDataSource dbSource = new BasicDataSource();

        try {

            int startUsernameIndex = NEON_CONNECTION_STRING.indexOf("//") + "//".length();
            int endUsernameIndex = NEON_CONNECTION_STRING.indexOf(":", startUsernameIndex);
            final String username = NEON_CONNECTION_STRING.substring(startUsernameIndex, endUsernameIndex);

            int endPasswordIndex = NEON_CONNECTION_STRING.indexOf("@");
            final String password = NEON_CONNECTION_STRING.substring(endUsernameIndex + 1, endPasswordIndex);

            final String address = NEON_CONNECTION_STRING.substring(endPasswordIndex + 1);

            final String connectionStr = "jdbc:postgresql://" + address;
            dbSource.setUrl(connectionStr);
            dbSource.setUsername(username);
            dbSource.setPassword(password);

        } catch(IndexOutOfBoundsException e){
            String errorMessage = "Error parsing connection string (NEON_CONNECTION_STRING).\n" +
                        "Check that it is formatted correctly at the top of this file";
            System.out.println(errorMessage);
            e.printStackTrace();
            System.exit(1);
            }

        return dbSource;
    }
    public DataSource getConnection(){
        return this.dataSource;
    }
}
