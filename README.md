### Fitur
- **Product Service**: Menambahkan dan melihat daftar produk.
- **Stock Change Service**: Mencatat penjualan dan restock produk.
- **Notification Service**: Mengirimkan notifikasi saat stok produk di bawah ambang batas minimum.
- **Analytics Service**: Menghasilkan laporan produk terlaris berdasarkan penjualan.
- **Event-Driven**: Menggunakan Kafka untuk komunikasi antar layanan.
- **Penyimpanan Data**: Menggunakan PostgreSQL untuk data persistence, Redis untuk caching dan ElasticSearch untuk searching.

---

## Arsitektur
1. **Product Service** (`port: 8082`):
   - Mengelola data produk (tambah, lihat).
   - Mengirimkan event ke Kafka saat produk ditambahkan atau diperbarui.
2. **Stock Change Service** (`port: 8083`):
   - Mencatat perubahan stok (SALE, RESTOCK).
   - Mengirimkan event ke Kafka saat stok berubah.
3. **Notification Service** (`port: 8084`):
   - Mengonsumsi event dari Kafka.
   - Mengirimkan notifikasi saat stok produk di bawah ambang batas minimum.
4. **Analytics Service** (`port: 8085`):
   - Mengonsumsi event dari Kafka.
   - Menghasilkan laporan analitik seperti produk terlaris.

### Layanan Pendukung
- **PostgreSQL**:
  - `postgres-product` (`port: 5433`): Menyimpan data produk.
  - `postgres-stock` (`port: 5434`): Menyimpan data perubahan stok.
  - `postgres-analytics` (`port: 5435`): Menyimpan data analitik.
- **Redis** (`port: 6379`): Digunakan untuk caching.
- **Kafka** (`port: 9092`): Digunakan untuk komunikasi antar layanan.
- **Zookeeper** (`port: 2181`): Digunakan oleh Kafka untuk koordinasi.
- **Elasticsearch** (`port: 9200`): Digunakan untuk menyimpan data product untuk kebutuhan searching

### Diagram
```
[Product Service] <-- [Kafka] <-- [Stock Change Service] --> [Kafka] --> [Analytics Service]
       |                                    |                                      |
       |                                    |                                      |
       v                                    v                                      v
[PostgreSQL]                             [Kafka]                               [PostgreSQL]
   |    |                                   |
   |    |                                   |
   v    v                                   v
[Redis] [Elasticsearch]              [Notification Service]

Stock Change Service akan melakukan update stock melalui SALE/RESTOCK kemudian akan mengirimkan sebuah event melalui producer kafka yang akan diterima oleh Product Service, Analytics Service Consumer dan Notification Service consumer, Product Service akan mengupdate stock product pada database dan memperbarui cache, Analytics Service akan mencatat semua perubahan stock, Notification Service akan mengirimkan email jika stock product sudah mencapai batas minimum                                 
                                           
                                    
```

---


## Cara Menjalankan

- docker-compose build
- docker-compose up -d

---

## Teknologi yang Digunakan

- **Spring Boot**: Framework untuk membangun layanan mikro.
- **Apache Kafka**: Untuk komunikasi berbasis event antar layanan.
- **PostgreSQL**: Database relasional untuk penyimpanan data.
- **Redis**: Untuk caching.
- **Docker & Docker Compose**: Untuk containerization dan orkestrasi layanan.
- **Maven**: Untuk manajemen dependensi dan build.
- **ElasticSearch**: Database untuk penyimpanan data untuk searching.
---

## Command Tambahan
- docker volume rm maven-repo : untuk menghapus volume cache maven

## Docker
- **DockerFile** : file yang berisi serangkaian instruksi untuk membangun image Docker. Image ini adalah template yang digunakan untuk membuat container.
   - Fungsi : 
        - Mendefinisikan lingkungan aplikasi, seperti sistem operasi, dependensi, dan konfigurasi.
        - Menentukan langkah-langkah untuk menginstal perangkat lunak, menyalin kode, dan menjalankan aplikasi.
        - Menghasilkan image.
- **Docker Compose** : alat untuk mendefinisikan dan mengelola aplikasi multi-container. memungkinkan untuk menjalankan beberapa container (misal: aplikasi/service, database, cache) secara bersamaan
   - Fungsi :
        - Mengatur hubungan antar container (misal: jaringan, volume).
        - Menyederhanakan perintah untuk menjalankan, menghentikan, atau menskalakan aplikasi.
