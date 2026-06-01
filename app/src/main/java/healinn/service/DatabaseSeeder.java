package healinn.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


//data yg di set: data admin, data rooms (60 kamar), data ballroom
// Bertanggung jawab mengisi data awal ke database saat pertama kali aplikasi dijalankan.

public class DatabaseSeeder {
    private final Connection connection;

    //ambil dari database
    public DatabaseSeeder() {
        this.connection = Database.getInstance().getConnection();
    }

    public void seed(){
        seedAdmin();
        seedRooms();
        seedBallroom();
    }

    //data admin: username admin, password: admin123, email: admin@healinn.id
    private void seedAdmin(){
            if (rowExists("users", "username", "admin")) {
            System.out.println("DATASEED: Admin sudah ada, skip.");
            return;
        }

        String sql = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "admin");
            ps.setString(2, "admin123");
            ps.setString(3, "admin@healinn.id");
            ps.setString(4, "ADMIN");
            ps.executeUpdate();
            System.out.println("DATASEED: Admin berhasil dibuat.");
        } catch (SQLException e) {
            System.err.println("DATASEED: Gagal seed admin: " + e.getMessage());
        }
    }

    //data rooms
    private void seedRooms(){
        if(rowExists("rooms", "room_id", "STD-1")) {
            System.out.println("DATASEED: Rooms sudah ada, skip.");
            return;
        }

        String sql = "INSERT INTO rooms (room_id, room_number, room_type, bed_type, status) VALUES (?, ?, ?, ?, ?)";
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            // 1. Standard Twin Bed → No. 101–110
            for (int i = 1; i <= 10; i++) {
                ps.setString(1, "STD-TW-" + i);
                ps.setInt   (2, 100 + i);
                ps.setString(3, "STANDARD");
                ps.setString(4, "TWIN_BED");
                ps.setString(5, "AVAILABLE");
                ps.addBatch();
            }

            // 2. Standard Double Bed → No. 111–120
            for (int i = 1; i <= 10; i++) {
                ps.setString(1, "STD-DB-" + i);
                ps.setInt   (2, 110 + i);
                ps.setString(3, "STANDARD");
                ps.setString(4, "DOUBLE_BED");
                ps.setString(5, "AVAILABLE");
                ps.addBatch();
            }

            // 3. Deluxe Twin Bed → No. 201–210
            for (int i = 1; i <= 10; i++) {
                ps.setString(1, "DLX-TW-" + i);
                ps.setInt   (2, 200 + i);
                ps.setString(3, "DELUXE");
                ps.setString(4, "TWIN_BED");
                ps.setString(5, "AVAILABLE");
                ps.addBatch();
            }

            // 4. Deluxe Double Bed → No. 211–220
            for (int i = 1; i <= 10; i++) {
                ps.setString(1, "DLX-DB-" + i);
                ps.setInt   (2, 210 + i);
                ps.setString(3, "DELUXE");
                ps.setString(4, "DOUBLE_BED");
                ps.setString(5, "AVAILABLE");
                ps.addBatch();
            }

            // 5. Suite Twin Bed → No. 301–310
            for (int i = 1; i <= 10; i++) {
                ps.setString(1, "STE-TW-" + i);
                ps.setInt   (2, 300 + i);
                ps.setString(3, "SUITE");
                ps.setString(4, "TWIN_BED");
                ps.setString(5, "AVAILABLE");
                ps.addBatch();
            }

            // 6. Suite Double Bed → No. 311–320
            for (int i = 1; i <= 10; i++) {
                ps.setString(1, "STE-DB-" + i);
                ps.setInt   (2, 310 + i);
                ps.setString(3, "SUITE");
                ps.setString(4, "DOUBLE_BED");
                ps.setString(5, "AVAILABLE");
                ps.addBatch();
            }
            ps.executeBatch();
            System.out.println("DATASEED: 60 Rooms berhasil dibuat.");
        } catch (SQLException e) {
            System.err.println("DATASEED: Gagal seed rooms: " + e.getMessage());
        }
    }

    //data ballroom
    private void seedBallroom(){
        if(rowExists("ballroom", "ballroom_id", "BALLROOM-01")) {
            System.out.println("DATASEED: Ballroom sudah ada, skip.");
            return;
        }

        String sql = "INSERT INTO ballroom (ballroom_id, status) VALUES (?, ?)";
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "BALLROOM-01");
            ps.setString(2, "AVAILABLE");
            ps.addBatch();

            ps.executeBatch();
            System.out.println("DATASEED: Ballroom berhasil dibuat.");
        } catch (SQLException e) {
            System.err.println("DATASEED: Gagal seed ballroom: " + e.getMessage());
        }
    }

    //cek data sudah ada atau belum
    private boolean rowExists(String table, String column, String value) {
        String sql = "SELECT 1 FROM " + table + " WHERE " + column + " = ? LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("DATASEED: Gagal cek data: " + e.getMessage());
            return false;
        }
    }

} //akhir
