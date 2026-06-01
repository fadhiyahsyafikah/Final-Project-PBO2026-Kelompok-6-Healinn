package healinn.service;

import healinn.model.Ballroom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BallroomService {
    private static final String BALLROOM_ID = "BALLROOM-01";
    private final Connection connection;

    public BallroomService() {
        this.connection = Database.getInstance().getConnection();
    }

    //ambil data ballroom
    public Ballroom getBallroom() {
        String sql = "SELECT * FROM ballroom WHERE ballroom_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, BALLROOM_ID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Ballroom ballroom = new Ballroom();
                ballroom.setStatus(Ballroom.Status.valueOf(rs.getString("status")));
                return ballroom;
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil data ballroom: " + e.getMessage());
        }
        return new Ballroom();
    }

    //update status ballroom
    public void updateStatus(Ballroom.Status newStatus) {
        String sql = "UPDATE ballroom SET status = ? WHERE ballroom_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newStatus.name());
            ps.setString(2, BALLROOM_ID);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal update status ballroom: " + e.getMessage());
        }
    }
}
