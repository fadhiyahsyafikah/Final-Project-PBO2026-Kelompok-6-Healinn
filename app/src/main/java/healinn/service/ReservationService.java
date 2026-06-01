package healinn.service;

import healinn.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


//semua reservasi terjadi
public class ReservationService {
    private final Connection connection;
    private final RoomService roomService;
    private final BallroomService ballroomService;

    public ReservationService() {
        this.connection = Database.getInstance().getConnection();
        this.roomService = new RoomService();
        this.ballroomService = new BallroomService();
    }

    //booking kamar
    public Reservation bookRoom(String custUsername, String roomId, LocalDate checkIn, LocalDate checkOut, int guestCount) {
        if (!checkOut.isAfter(checkIn)) return null;

        Room room = roomService.findById(roomId);
        if (room == null || !room.isAvailable()) return null;

        long price = room.calculatePrice(checkIn, checkOut);
        String resId = generateReservationId();

        boolean saved = insertReservation(
            resId, custUsername,
            "ROOM", roomId, room.getName(),
            checkIn, checkOut, price, null, null, guestCount);   //purpose null krn kamar     
        if (!saved) return null;

        roomService.updateStatus(roomId, Room.Status.OCCUPIED);

        return buildObject(resId, custUsername, "ROOM",
            roomId, room.getName(), checkIn, checkOut, price, null, null, guestCount);
    }

    //booking ballroom
    public Reservation bookBallroom(String custUsername, BallroomPackage pkg, LocalDate eventDate, int days, String purpose, int guestCount) {
        Ballroom ballroom = ballroomService.getBallroom();
        if (!ballroom.isAvailable()) return null;

        long totalPrice = pkg.calculateTotal(days);
        LocalDate endDate = eventDate.plusDays(pkg.isPerDay() ? days : 1);
        String resId = generateReservationId();

        boolean saved = insertReservation(
            resId, custUsername,
            "BALLROOM", "BALLROOM-01", "Grand Ballroom Healinn",
            eventDate, endDate, totalPrice, pkg.name(), purpose, guestCount);

        if (!saved) return null;
        ballroomService.updateStatus(Ballroom.Status.BOOKED);
        return buildObject(resId, custUsername, "BALLROOM",
        "BALLROOM-01", "Grand Ballroom Healinn",
        eventDate, endDate, totalPrice, pkg.name(), purpose, guestCount);
    }

    //cancel
    public boolean cancelReservation(String reservationId) {
        Reservation res = findById(reservationId);
        if (res == null || res.getStatus() != Reservation.Status.ACTIVE) return false;

        String sql = "UPDATE reservations SET status = 'CANCELLED' WHERE reservation_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, reservationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Cancel error: " + e.getMessage());
            return false;
        }

        if (res.getType() == Reservation.Type.ROOM) {
            roomService.updateStatus(res.getBookableId(), Room.Status.AVAILABLE);
        } else {
            ballroomService.updateStatus(Ballroom.Status.AVAILABLE);
        }
        return true;
    }

    //query, data transaksi
    public List<Reservation> getByCustomer(String username) {
        return query(
            "SELECT * FROM reservations WHERE customer_username = ? ORDER BY reservation_id DESC", username); //customer
    }

    public List<Reservation> getAll() {
        return query("SELECT * FROM reservations ORDER BY reservation_id DESC"); //admin
    }

    //statistik, jumlah cust yg ada dan pendapatan kotor, ini untuk admin
    public int countActiveGuests() {
        String sql = "SELECT COALESCE(SUM(guest_count), 0) FROM reservations " + "WHERE type='ROOM' AND status='ACTIVE'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) { return 0; }
    }

    public long getTotalRevenue() {
        String sql = "SELECT SUM(total_price) FROM reservations WHERE status != 'CANCELLED'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getLong(1) : 0;
        } catch (SQLException e) { return 0; }
    }

    // total harga yg muncul untuk cust
    public long estimateRoomPrice(RoomType type, BedType bedType, LocalDate checkIn, LocalDate checkOut) {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (nights <= 0) nights = 1;
        return nights * bedType.getPriceFor(type);
    }

// method tambahan untuk insert data ke database
    private boolean insertReservation(String resId, String customer, String type, 
                                      String bookableId, String bookableName, 
                                      LocalDate checkIn, LocalDate checkOut,
                                      long price, String ballroompkg, String purpose, int guestCount){
        String sql = """
            INSERT INTO reservations
            (reservation_id, customer_username, type, bookable_id, bookable_name,
             check_in, check_out, total_price, ballroom_pkg, purpose, guest_count, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'ACTIVE')
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, resId);
            ps.setString(2, customer);
            ps.setString(3, type);
            ps.setString(4, bookableId);
            ps.setString(5, bookableName);
            ps.setString(6, checkIn.toString());
            ps.setString(7, checkOut.toString());
            ps.setLong(8, price);
            ps.setString(9, ballroompkg);
            ps.setString(10, purpose);    
            ps.setInt(11, guestCount);    
            
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Insert error: " + e.getMessage());
            return false;
        }
    }

    private Reservation findById(String resId){
        List<Reservation> resList = query("SELECT * FROM reservations WHERE reservation_id = ? LIMIT 1", resId);
        return resList.isEmpty() ? null : resList.get(0);
    }    

    private List<Reservation> query (String sql, String... params){
        List<Reservation> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            for (int i = 0; i < params.length; i++) { 
                ps.setString(i + 1, params[i]);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Reservation r = buildObject(
                    rs.getString("reservation_id"),
                    rs.getString("customer_username"),
                    rs.getString("type"),
                    rs.getString("bookable_id"),
                    rs.getString("bookable_name"),
                    LocalDate.parse(rs.getString("check_in")),
                    LocalDate.parse(rs.getString("check_out")),
                    rs.getLong("total_price"),
                    rs.getString("ballroom_pkg"),
                    rs.getString("purpose"),
                    rs.getInt("guest_count")
                );
                r.setStatus(Reservation.Status.valueOf(rs.getString("status")));
                list.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Query error: " + e.getMessage());
        } 
        return list;
    }

    private Reservation buildObject(String resId, String customer, String type,
                                    String bookableId, String bookableName,
                                    LocalDate checkIn, LocalDate checkOut,
                                    long price, String pkgName, String purpose, int guestCount) {
        Reservation.Type resType = Reservation.Type.valueOf(type);
        BallroomPackage pkg = pkgName != null ? BallroomPackage.valueOf(pkgName) : null;
        return new Reservation(resId, customer, resType,
            bookableId, bookableName, checkIn, checkOut, price, pkg, purpose, guestCount);
    }

    private String generateReservationId() {
        String sql = "SELECT COUNT(*) FROM reservations";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            int count = rs.next() ? rs.getInt(1) : 0;
            return String.format("RES-%04d", count + 1);
        } catch (SQLException e) {
            return "RES-" + System.currentTimeMillis();
        }
    }

} //akhir