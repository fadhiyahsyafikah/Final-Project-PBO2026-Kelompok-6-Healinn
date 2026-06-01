package healinn.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    // lokasi database(sqlite)
    private static final String URL = "jdbc:sqlite:healinn.db";

    // variabel untuk singleton instance dan koneksi database, menympan slot kosong yg bakala dipakai
    private static Database instance;
    private Connection connection;

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private Database() {
        connect();
        createTables();
    }

    //koneksi ke database (sqlite)
    private void connect() {
        try {
            connection = DriverManager.getConnection(URL);
            System.out.println("Database terkoneksi.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Gagal terhubung ke database.");
        }
    }

    //membuat tabel user, booking, dan ballroom jika belum ada
    private void createTables() {
        try (Statement stmt = connection.createStatement()) {
            
            //tabel user
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    username TEXT PRIMARY KEY,
                    password TEXT NOT NULL,
                    email    TEXT NOT NULL,
                    role     TEXT NOT NULL   -- 'ADMIN' atau 'CUSTOMER'
                )
            """);

            //tabel rooms(30 kamar, standar, deluxe, suite)
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS rooms (
                    room_id     TEXT PRIMARY KEY,
                    room_number INTEGER NOT NULL,
                    room_type   TEXT NOT NULL,   -- 'STANDARD', 'DELUXE', 'SUITE'
                    bed_type    TEXT NOT NULL,   -- 'TWIN_BED', 'DOUBLE_BED'
                    status      TEXT NOT NULL    -- 'AVAILABLE', 'OCCUPIED', 'MAINTENANCE'
                )
            """);

            //tabel ballroom
             stmt.execute("""
                CREATE TABLE IF NOT EXISTS ballroom (
                    ballroom_id TEXT PRIMARY KEY,
                    status      TEXT NOT NULL    -- 'AVAILABLE', 'BOOKED'
                )
            """);

            //tabel reservasi
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS reservations (
                    reservation_id    TEXT PRIMARY KEY,
                    customer_username TEXT NOT NULL,
                    type              TEXT NOT NULL,   -- 'ROOM' atau 'BALLROOM'
                    bookable_id       TEXT NOT NULL,
                    bookable_name     TEXT NOT NULL,
                    check_in          TEXT NOT NULL,   -- format: yyyy-MM-dd
                    check_out         TEXT NOT NULL,   -- format: yyyy-MM-dd
                    total_price       INTEGER NOT NULL,
                    ballroom_pkg      TEXT,            -- null jika tipe ROOM
                    purpose           TEXT,            -- tujuan penggunaan ballroom, null jika tipe ROOM
                    guest_count       INTEGER NOT NULL DEFAULT 1,
                    status            TEXT NOT NULL    -- 'ACTIVE', 'COMPLETED', 'CANCELLED'
                )
            """);

            System.out.println("Tabel berhasil dibuat.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Gagal membuat tabel.");
        }

    }

} //akhir
