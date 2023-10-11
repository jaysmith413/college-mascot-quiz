package dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.cglib.core.Local;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NeonDbLeaderboard implements Leaderboard {

    private final String NEON_CONNECTION_STRING = "postgres://jaysmith413:RneKN7sD6Qfd@ep-dawn-moon-31749400.us-east-2.aws.neon.tech/neondb";

    private JdbcTemplate template;

    public NeonDbLeaderboard(){
        this.template = new JdbcTemplate(getDbSource(false));
    }

    @Override
    public List<Player> getLeaderboard(){
        List<Player> leaderboard = new ArrayList<>();

        String sql = "SELECT player_name, score, date " +
                     "FROM leaderboard " +
                     "ORDER BY score DESC, player_name " +
                     "LIMIT 10;";

        SqlRowSet results = this.template.queryForRowSet(sql);

        while(results.next()){
            String name = results.getString("player_name");
            int score = results.getInt("score");
            LocalDate date = results.getDate("date").toLocalDate();

            leaderboard.add(new Player(name, score, date));
        }

        return leaderboard;
    }

    @Override
    public void addLeaderboard(Player playerToAdd){

        String sql = "INSERT INTO leaderboard " +
                     "(player_name, score, date) " +
                     "VALUES " +
                     "(?, ?, ?)";

        try {
            this.template.update(sql, playerToAdd.getName(), playerToAdd.getScore(), LocalDate.now());
        } catch(DataAccessException e){
            e.printStackTrace();
        }
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
}
