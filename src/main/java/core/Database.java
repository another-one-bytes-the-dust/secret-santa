package core;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.commons.dbcp2.BasicDataSource;

public class Database {

    private static BasicDataSource connectionPool;
    private static final int POOL_SIZE = 4;
    static {
        createConnectionPool();
    }

    public static void createConnectionPool() {
        try {
            URI uri = new URI(System.getenv("DATABASE_URL"));
            String dbUrl = "jdbc:postgresql://" + uri.getHost() + ':' + uri.getPort() + uri.getPath();
            connectionPool = new BasicDataSource();

            if (uri.getUserInfo() != null) {
                connectionPool.setUsername(uri.getUserInfo().split(":")[0]);
                connectionPool.setPassword(uri.getUserInfo().split(":")[1]);
            }

            connectionPool.setDriverClassName("org.postgresql.Driver");
            connectionPool.setUrl(dbUrl);
            connectionPool.setInitialSize(POOL_SIZE);
        } catch (URISyntaxException e) {
            System.out.println("Could not retrieve database metadata " + e.getMessage());
        }
    }

    private Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    public void changeParticipantName(String name, String chatId) {
        String sql = "UPDATE participants " +
                "Set name = ? WHERE chat_id = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, chatId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeParticipantDepartment(String department, String chatId) {
        String sql = "UPDATE participants " +
                "Set department = ? WHERE chat_id = ?";
        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, department);
            ps.setString(2, chatId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addParticipant(String name, String department, String chatId) {
        String sql = "INSERT INTO participants " +
                "(name, department, chat_id) " +
                "VALUES(?, ?, ?)";

        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, department);
            ps.setString(3, chatId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void applyParticipant(String chatId) {
        String sql = "UPDATE participants " +
                "Set ready = ? WHERE chat_id = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, true);
            ps.setString(2, chatId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Participant getParticipantById(int id)
    {
        String sql = "SELECT id, name, department, chat_id FROM participants WHERE id = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String department = rs.getString("department");
                    String chatId = rs.getString("chat_id");

                    return new Participant(id, name, department, chatId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Participant getReceiverBySantaId(int id)
    {
        String sql = "SELECT receiver_id FROM gift_relations WHERE santa_id = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int receiverId = rs.getInt("receiver_id");

                    return getParticipantById(receiverId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Participant getParticipantByChatId(String chatId)
    {
        String sql = "SELECT id, name, department, chat_id FROM participants WHERE chat_id = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, chatId);

             try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String department = rs.getString("department");

                    return new Participant(id, name, department, chatId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Participant> getAllParticipants()
    {
        String sql = "SELECT id, name, department, chat_id, ready FROM participants ORDER BY id ASC";
        List<Participant> participants = new ArrayList<>();

        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String department = rs.getString("department");
                    String chatId = rs.getString("chat_id");
                    boolean ready = rs.getBoolean("ready");
                    participants.add(new Participant(id, name, department, chatId, ready));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return participants;
    }

    public void deleteParticipant(int id) {
        String sql = "DELETE FROM participants WHERE id = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean hasSantas() {
        String sql = "SELECT * FROM gift_relations";
        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void assignSantas(Map<Participant, Participant> relations) {
        String sql = "INSERT INTO gift_relations (santa_id, receiver_id) VALUES ";

        StringJoiner joiner = new StringJoiner(", ");

        for (Participant santa : relations.keySet()) {
            int santaId =  santa.getId();
            int receiverId = relations.get(santa).getId();
            joiner.add(String.format("(%d, %d)", santaId, receiverId));
        }

        sql += joiner.toString();

        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}