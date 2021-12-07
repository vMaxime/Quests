package fr.vmaxime.quests;

import fr.vmaxime.quests.quest.QuestType;

import java.sql.*;
import java.util.Locale;
import java.util.UUID;

public class QuestsDatabase {

    private Connection connection;
    private Statement statement;

    public QuestsDatabase(String host, int port, String database, String username, String password) {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
        } catch (Exception e){
            e.printStackTrace();
        }
        // create default tables for quests
        Quests.getInstance().getQuests().forEach(quest -> {
            try {
                statement.execute(
                        "CREATE TABLE IF NOT EXISTS " + quest.getType().name().toLowerCase(Locale.ROOT) +
                            "(" + "player VARCHAR(32) PRIMARY KEY," + "progression INTEGER" + ")"
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Gets the plugin database
     * @return Connection or null
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Gets the connection statement
     * @return Statement of the connection or null
     */
    public Statement getStatement() {
        return statement;
    }

    /**
     * Gets progression of a player in a quest from his unique id
     * @param uuid Unique id of the player
     * @param type Quest type we want the progression
     * @return Integer of the progression or 0
     */
    public int getProgression(UUID uuid, QuestType type) {
        try {
            ResultSet rs = statement.executeQuery("SELECT `progression` FROM `" + type.name().toLowerCase(Locale.ROOT) + "` WHERE player=`" + uuid.toString() + "`");
            if (rs.next())
                return rs.getInt("progression");
            else
                setProgression(uuid, type, 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Sets progression of a player in a quest from his unique id
     * @param uuid Unique id of the player
     * @param type Quest type we want the progression
     * @param progression Integer of the new progression of the player
     */
    public void setProgression(UUID uuid, QuestType type, int progression) {
        try {
            statement.executeUpdate("INSERT INTO `" + type.name().toLowerCase(Locale.ROOT) + "` (player, progression) VALUES(`" + uuid.toString() +  "`, " + progression + ") ON DUPLICATE KEY UPDATE progression=" + progression);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
