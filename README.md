# 🍽️ UTP PAM — Food Ordering App

Aplikasi Android sederhana untuk memilih makanan dan mengelola pesanan.

---

## 📱 Tampilan Aplikasi

| Menu List | Menu Detail |
|-----------|-------------|
| Daftar menu dengan kontrol pesanan langsung | Detail makanan dengan pengatur jumlah |

---

## ✨ Fitur

- **Menu List** — Menampilkan daftar makanan menggunakan `LazyColumn` lengkap dengan nama, harga, dan tombol `+` / `−` per item
- **Menu Detail** — Halaman detail tiap menu dengan deskripsi, kontrol jumlah, dan subtotal
- **Order Summary** — Panel ringkasan pesanan yang update secara realtime (total item & total harga)
- **Empty State** — Menampilkan pesan *"No orders yet"* saat belum ada pesanan
- **Navigasi** — Perpindahan halaman menggunakan Navigation Compose

---

## 🛠️ Tech Stack

| Komponen | Teknologi |
|----------|-----------|
| UI | Jetpack Compose |
| Navigasi | Navigation Compose |
| State Management | `remember`, `mutableStateListOf`, `rememberSaveable` |
| Arsitektur | State Hoisting + ViewModel |
| Data | In-memory (tanpa database) |
| Minimum SDK | 24 (Android 7.0) |
| Target SDK | 34 (Android 14) |

---

## 🗂️ Struktur Proyek

```
app/src/main/java/com/example/utppam/
│
├── MainActivity.kt               # Entry point, setup NavController & theme
│
├── model/
│   └── FoodItem.kt               # Data class menu makanan
│
├── viewmodel/
│   └── OrderViewModel.kt         # State management, logika pesanan
│
├── navigation/
│   └── Navigation.kt             # NavGraph: route "menu" & "detail/{itemId}"
│
├── screen/
│   ├── MenuListScreen.kt         # Halaman utama daftar menu
│   └── MenuDetailScreen.kt       # Halaman detail item
│
└── ui/theme/
    └── Theme.kt                  # Warna, MaterialTheme
```

---

## 🧭 Navigation Routes

| Route | Deskripsi |
|-------|-----------|
| `menu` | Halaman utama daftar menu |
| `detail/{itemId}` | Halaman detail item berdasarkan ID |

---

## 🔄 State Management

State pesanan dikelola secara terpusat di `OrderViewModel` menggunakan `mutableStateListOf` sehingga perubahan di satu screen (misalnya Detail) langsung tercermin di screen lain (Menu List & Order Summary) tanpa perlu reload.

```
OrderViewModel (mutableStateListOf)
       │
       ├──► MenuListScreen   (baca + ubah qty)
       └──► MenuDetailScreen (baca + ubah qty)
```

---

## 🚀 Cara Menjalankan

1. Clone repository ini
   ```bash
   git clone https://github.com/LeonVonDeusch/UTP-PAM.git
   ```

2. Buka folder `UTPPAM` di **Android Studio**

3. Tunggu Gradle sync selesai

4. Jalankan di emulator atau perangkat fisik (Android 7.0+)

---

## 📋 Ketentuan UTP

- [x] Menggunakan Jetpack Compose
- [x] Tanpa database / local storage (in-memory)
- [x] Menggunakan Navigation Compose
- [x] Minimal 2 halaman
- [ ] State management: `remember`, `mutableStateListOf`, `rememberSaveable`
- [x] State hoisting
- [x] UI interaktif dan reactive
- [x] Jumlah item tidak bisa negatif
- [x] Empty state "No orders yet"

---

## 👤 Identitas

| | |
|---|---|
| **Nama** | Dandy ZIkri Arifandi |
| **NIM** | 245150707111035 |
| **Mata Kuliah** | Pemrograman Aplikasi Mobile |
| **Tugas** | UTP |
