package healinn.service;

import healinn.model.BedType;
import healinn.model.Room;
import healinn.model.RoomType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//roomservice: ambil (semua atau tipe), update (available atu tidak), cari (id), hitung rooms yg terisi

public class RoomService {
    private final Connection connection;

    public RoomService() {
        this.connection = Database.getInstance().getConnection();
    }

    //ambil data kamar 
    public List<Room> getAllRooms() {
        return queryRooms("SELECT * FROM rooms ORDER BY room_number");
    }

    //ambil kamar berdasarkan semua tipe
    public List<Room> getRoomsByType(RoomType type) {
      return queryRooms("SELECT * FROM rooms WHERE room_type = ? ORDER BY room_number", type.name());
    }

    //ambil kamar berdasarkan tipe dan bed
    public List<Room> getRoomsByTypeAndBed(RoomType type, BedType bedType) {
        return queryRooms( "SELECT * FROM rooms WHERE room_type = ? AND bed_type = ? ORDER BY room_number", type.name(), bedType.name());
    }

    //cari satu kamar berdasarkan id
    public Room findById(String roomId){
        List<Room> rooms = queryRooms("SELECT * FROM rooms WHERE room_id = ? LIMIT 1", roomId);
        return rooms.isEmpty() ? null : rooms.get(0);
    }

    //hitung yg sedang occupied
    public int countOccupied(){
        String sql = "SELECT COUNT(*) AS count FROM rooms WHERE status = 'OCCUPIED'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            System.err.println("Gagal menghitung occupied rooms: " + e.getMessage());
        }
        return 0;
    }

    //update status kamar
    public void updateStatus(String roomId, Room.Status newStatus) {
        String sql = "UPDATE rooms SET status = ? WHERE room_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newStatus.name());
            ps.setString(2, roomId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal update status kamar: " + e.getMessage());
        }
    }

   // method tambahan untuk query rooms
    private List<Room> queryRooms(String sql, String... params) {
        List<Room> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setString(i + 1, params[i]);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String id = rs.getString("room_id");
                int number = rs.getInt   ("room_number");
                RoomType type = RoomType.valueOf(rs.getString("room_type"));
                BedType bedType = BedType.valueOf(rs.getString("bed_type"));
                Room.Status status = Room.Status.valueOf(rs.getString("status"));

                Room room = new Room(id, number, type, bedType);
                room.setStatus(status);
                list.add(room);
            }
        } catch (SQLException e) {
            System.err.println("[ROOM] queryRooms error: " + e.getMessage());
        }
        return list;
    }

} //akhir
