import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class Database {
    private static final String USER_NAME = "java";
    private static final String PASSWORD = "password";
    private static final String MySQL_URL = "jdbc:mysql://localhost:3306/javabase";
    private Connection connection = null;

    /**
     * Connect to Database;
     */
    public void connectToDatabase() {
        System.out.println("Connecting database...");
        try {
            connection = DriverManager.getConnection(MySQL_URL, USER_NAME, PASSWORD);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Database connected!");
    }


    public void closeDatabase() {
        close(connection);
        System.out.println("Database disconnected!");
    }

    /**
     * Close connection to MYSQL database.
     *
     * @param connection Connection variable
     */
    private static void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close the PreparedStatement ps.
     *
     * @param ps PreparedStatement needed to close
     */
    private static void close(PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close the ResultSet rs.
     *
     * @param rs ResultSet needed to be close
     */
    private static void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get all Words from database to Dictionary.
     */
    public void getAllWord() {
        final String SQL_QUERY = "SELECT * FROM ev_txt";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
            try {
                ResultSet rs = ps.executeQuery();
                try {
                    while (rs.next()) {
                        Dictionary.addWord(new Word(rs.getString("Words")
                                , rs.getString("Pronounce")
                                , rs.getString("Description")));
                    }
                } finally {
                    close(rs);
                }
            } finally {
                close(ps);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * insert new word to dictionary
     * @param word insert word
     * @param pronounce insert pronounse
     * @param description insert description
     */
    public void insertWord(final String word, final String pronounce, final String description) {
        final String SQL_QUERY = "INSERT INTO ev_txt (Words, Pronounce, Description) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
            ps.setString(1, word);
            ps.setString(2, pronounce);
            ps.setString(3, description);
            try {
                ps.executeUpdate();
            } catch (SQLIntegrityConstraintViolationException e) {
                // `word` is already in database
                System.out.println(
                        "Cannot insert `"
                                + word
                                + "` to dictionary as `"
                                + word
                                + "` is already in the database!");
                return;
            } finally {
                close(ps);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete the word `word` from the database.
     *
     * <p>Nothing happens if `word` is not in the database for deletion.
     *
     * @param word delete word
     */
    public void deleteWord(final String word) {
        final String SQL_QUERY = "DELETE FROM ev_txt WHERE Words = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
            ps.setString(1, word);
            try {
                ps.executeUpdate();
            } finally {
                close(ps);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
