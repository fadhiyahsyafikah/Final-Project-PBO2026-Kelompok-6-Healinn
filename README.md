## 🏨 HealInn Hotel & Convention Center

## 📝 Deskripsi Singkat Proyek
HealInn adalah aplikasi desktop berbasis JavaFX yang dirancang untuk membantu pengelolaan reservasi hotel secara digital. Aplikasi ini memungkinkan customer untuk memesan kamar dan ballroom secara mandiri, serta memberikan admin kemampuan memantau seluruh operasional hotel melalui dashboard yang informatif.
Aplikasi ini dibuat sebagai Proyek Akhir mata kuliah Pemrograman Berorientasi Objek, dengan fokus pada penerapan prinsip OOP secara menyeluruh.

## 👥 Tim Pengembang

- **[Karis Kabanga] (https://github.com/kariskabanga65)** ('H071251059')
- **[Zahra Intan Pratiwi] (https://github.com/zahrara06-alt)** ('H071251004)
- **[Fadhiyah Syafikah Firman] (https://github.com/fadhiyahsyafikah)** ('H071251019)

## 🎨 Tema

**Perhotelan**: Aplikasi untuk manajemen reservasi kamar, ballroom, dan pemantauan operasional hotel.

## ✨ Fitur Aplikasi
👤 CUSTOMER
- 🔐 Registrasi akun baru dengan username, email, dan password.
- 🔑 Login dengan validasi akun dari database.
- 🛏️ melihat dan memilih kamar yang tersedia (Standard, Deluxe, Suite x Twin Bed/ Double Bed).
- 📅 Reservasi kamar dengan pilihan tanggal check-in dan check-out.
- 💳 Input nominal pembayaran sebagai konfirmasi transaksi.
- 🏛️ Reservasi ballroom dengan 3 pilihan paket durasi.
- 🎯 Mengisi Tujuan penggunaan Ballroom saat reservasi.
- 📋 Melihat riwayat seluruh reservasi pribadi.
- ❌ Membatalkan reservasi yang masih aktif.

🔑 ADMIN
- 📊 Dashboard statistik: jumlah kamar terisi, tamu aktif, dan total pendapatan.
- 🏨 Grid visual status 60 kamar (hijau = tersedia, merah = terisi).
- 📋 Tabel seluruh transaksi reservasi dari semua customer dan opsi pembatalan reservasi.

## 🔄 Alur Kerja Aplikasi

```
Buka Aplikasi
└── Pilih Role
    ├── Customer
    │   ├── Login / Daftar Baru
    │   └── Dashboard
    │       ├── Pilih Kamar → Isi Tanggal → Input Pembayaran → Konfirmasi
    │       ├── Ballroom → Pilih Paket → Isi Tanggal & Tujuan → Konfirmasi
    │       └── Riwayat → Lihat / Batalkan Reservasi
    └── Admin
        └── Login
            └── Dashboard
                ├── Status Kamar (grid visual)
                ├── Semua Reservasi (tabel)
                └── Statistik (kamar terisi, tamu aktif, pendapatan)
```

---

## 🛠️ Teknologi dan Library
- **Bahasa**: Java
- **Framework UI**: JavaFX
- **Build Tool**: Gradle
- **Data Base**: SQLite
- **Penyimpanan**: File lokal healinn.db (otomatis dibuat saat pertama jalan)

## ⚙️ Cara Menjalankan Aplikasi

**Prasyarat:** Pastikan JDK 17 atau lebih baru sudah terpasang di sistem Anda.

**1. Clone repository:**
```bash
git clone https://github.com/fadhiyahsyafikah/Final-Project-PBO2026-Kelompok-6-Healinn.git
```

**2. Pindah ke direktori proyek:**
```bash
cd Final-Project-PBO2026-Kelompok-6-Healinn
```

**3. Jalankan aplikasi:**
```bash
# Windows
.\gradlew run

# macOS / Linux
./gradlew run
```

Gradle akan otomatis mengunduh semua dependensi yang dibutuhkan dan menjalankan aplikasi

---

## 🏛️ Struktur Kode

```
healinn-hotel/
├── build.gradle                    
├── settings.gradle
├── gradlew / gradlew.bat
│
└── src/main/java/healinn/
    ├── App.java
    ├── model/
    │   ├── User.java              
    │   ├── Admin.java             
    │   ├── Customer.java          
    │   ├── Bookable.java          
    │   ├── Room.java              
    │   ├── RoomType.java          
    │   ├── BedType.java           
    │   ├── Ballroom.java          
    │   ├── BallroomPackage.java   
    │   └── Reservation.java
    ├── service/
    │   ├── Database.java          
    │   ├── DatabaseSeeder.java    
    │   ├── AccService.java        
    │   ├── RoomService.java
    │   ├── BallroomService.java
    │   └── ReservationService.java
    ├── util/
    │   ├── SceneManager.java      
    │   ├── UIStyle.java           
    │   ├── UIComponent.java       
    │   └── UILayout.java          
    └── controller/
        ├── LoginSelectController.java
        ├── CustomerLoginController.java
        ├── AdminLoginController.java
        ├── CustomerDashboardController.java
        ├── CustomerBallroomController.java
        ├── CustomerRiwayatController.java
        ├── AdminDashboardController.java
        └── RoomBookingController.java
```

---

## ✍️ Penerapan 4 Pilar OOP

| Pilar OOP     | Penerapan                                                                       |
|---------------|---------------------------------------------------------------------------------|
| Encapsulation | Field bersifat `private`, akses melalui getter dan setter           |
| Inheritance   | `Admin` dan `Customer` mewarisi abstract class `User`                           |
| Abstraction   | `User` sebagai abstract class, `Bookable` sebagai interface                     |
| Polymorphism  | `Room` & `Ballroom` implements `Bookable`, override `getRole()` pada subclass   |

---

## 👥 Pembagian Tugas

| Nama                     | Tugas Utama                                                                              |
|--------------------------|------------------------------------------------------------------------------------------|
| Karis Kabanga           | Logika dan implementasi `model/` (User, Room, Reservation, dll.)                        |
| Fadhiyah Syafikah Firman | Logika bisnis, database service + controller (Database, AccService, ReservationService, dll.) |
| Zahra Intan Pratiwi      | Tampilan & alur `util/` + controller (SceneManager, UIComponent, semua halaman)         |

