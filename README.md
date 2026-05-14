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
- [x] State management: `remember`, `mutableStateListOf`, `rememberSaveable`
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

# 📖 Dokumentasi Kode — UTP PAM Food Ordering App

Dokumen ini menjelaskan **setiap class dan setiap baris** kode dalam project ini.

---

## 📁 Daftar File

1. [FoodItem.kt](#1-fooditemkt--model)
2. [OrderViewModel.kt](#2-orderviewmodelkt--viewmodel)
3. [Navigation.kt](#3-navigationkt--navigasi)
4. [MainActivity.kt](#4-mainactivitykt--entry-point)
5. [Theme.kt](#5-themekt--tampilan)
6. [MenuListScreen.kt](#6-menulistscreenkt--halaman-utama)
7. [MenuDetailScreen.kt](#7-menudetailscreenkt--halaman-detail)

---

## 1. `FoodItem.kt` — Model

> **Tugasnya:** Menjadi "cetakan" atau blueprint untuk setiap item makanan di aplikasi.

```kotlin
package com.example.utppam.model
// → Menandakan file ini berada di folder/package "model"
//   Package digunakan untuk mengorganisir file agar tidak berantakan

data class FoodItem(
// → "data class" adalah class khusus Kotlin untuk menyimpan data
//   Otomatis punya fitur: copy(), equals(), toString() tanpa kita tulis manual
//   Berbeda dengan class biasa yang hanya punya behavior/fungsi

    val id: Int,
    // → Nomor unik tiap makanan (1, 2, 3, ...)
    //   "val" = nilainya tidak bisa diubah setelah dibuat (immutable)
    //   "Int" = tipe data bilangan bulat

    val name: String,
    // → Nama makanan, misal "Nasi Goreng Spesial"
    //   "String" = tipe data teks

    val price: Double,
    // → Harga makanan, misal 25000.0
    //   "Double" = tipe data angka desimal (bisa ada koma)

    val description: String,
    // → Deskripsi panjang makanan yang tampil di halaman detail

    val emoji: String,
    // → Emoji makanan, misal "🍳", dipakai sebagai gambar pengganti foto

    val category: String,
    // → Kategori makanan: "Makanan Utama", "Minuman", "Dessert", dll

    var quantity: Int = 0
    // → Jumlah pesanan item ini
    //   "var" = nilainya BISA diubah (mutable), berbeda dengan "val"
    //   "= 0" = nilai default saat pertama dibuat adalah 0 (belum dipesan)
)
```

---

## 2. `OrderViewModel.kt` — ViewModel

> **Tugasnya:** Menyimpan semua data pesanan dan menyediakan fungsi untuk mengubahnya. Ini adalah "otak" dari aplikasi.

```kotlin
package com.example.utppam.viewmodel
// → File ini berada di package "viewmodel"

import androidx.compose.runtime.mutableStateListOf
// → Import fungsi mutableStateListOf dari Jetpack Compose
//   Digunakan untuk membuat list yang bisa dipantau perubahannya oleh UI

import androidx.lifecycle.ViewModel
// → Import class ViewModel dari Android Jetpack
//   ViewModel bertahan saat layar diputar (rotasi), berbeda dengan Activity

import com.example.utppam.model.FoodItem
// → Import data class FoodItem yang kita buat di file model

class OrderViewModel : ViewModel() {
// → Membuat class OrderViewModel yang mewarisi (extends) ViewModel
//   Artinya OrderViewModel punya semua kemampuan ViewModel bawaan Android

    val menuItems = mutableStateListOf(
    // → "menuItems" adalah list semua makanan yang tersedia
    // → "mutableStateListOf" = list yang:
    //   1. Bisa diubah (tambah/hapus/edit item)
    //   2. Otomatis memberitahu UI saat ada perubahan → UI langsung update
    // → "val" karena variabel menuItems-nya tidak diganti,
    //   tapi ISI list-nya boleh berubah

        FoodItem(id = 1, name = "Nasi Goreng Spesial", price = 25000.0,
            description = "...", emoji = "🍳", category = "Makanan Utama"),
        // → Membuat objek FoodItem pertama dengan data lengkap
        // → id=1 adalah identitas unik item ini

        FoodItem(id = 2, name = "Mie Ayam Bakso", ...),
        FoodItem(id = 3, name = "Ayam Bakar Madu", ...),
        // → Dan seterusnya untuk 10 item makanan
        // → Semua quantity tidak disebutkan → otomatis = 0 (default)
    )

    val totalItems: Int
        get() = menuItems.sumOf { it.quantity }
    // → "totalItems" adalah properti yang menghitung total semua pesanan
    // → "get()" = computed property, nilainya dihitung ulang setiap kali dipanggil
    // → "sumOf { it.quantity }" = jumlahkan semua quantity dari setiap item
    //   Contoh: nasi goreng=2, es teh=1 → totalItems = 3

    val totalPrice: Double
        get() = menuItems.sumOf { it.price * it.quantity }
    // → Menghitung total harga semua pesanan
    // → "it.price * it.quantity" = harga × jumlah per item, lalu semua dijumlahkan
    //   Contoh: (25000×2) + (8000×1) = 58000.0

    fun increaseQuantity(itemId: Int) {
    // → Fungsi untuk menambah jumlah pesanan satu item
    // → Menerima parameter "itemId" untuk tahu item mana yang ditambah

        val index = menuItems.indexOfFirst { it.id == itemId }
        // → Cari posisi (index) item di dalam list berdasarkan id-nya
        // → "indexOfFirst" = cari index pertama yang memenuhi kondisi
        // → Jika ditemukan → hasilnya 0,1,2,... | Jika tidak → hasilnya -1

        if (index != -1) {
        // → Hanya lanjut jika item ditemukan (index bukan -1)

            menuItems[index] = menuItems[index].copy(quantity = menuItems[index].quantity + 1)
            // → Update item di posisi "index"
            // → ".copy()" = buat salinan objek FoodItem dengan satu nilai yang diubah
            //   (hanya quantity yang berubah, id/name/price/dll tetap sama)
            // → quantity + 1 = tambahkan 1 ke jumlah pesanan sekarang
            // → Kenapa pakai .copy()? Karena mutableStateListOf hanya mendeteksi
            //   perubahan jika objeknya diganti, bukan hanya propertinya
        }
    }

    fun decreaseQuantity(itemId: Int) {
    // → Fungsi untuk mengurangi jumlah pesanan satu item

        val index = menuItems.indexOfFirst { it.id == itemId }
        // → Sama seperti increaseQuantity, cari posisi item dulu

        if (index != -1 && menuItems[index].quantity > 0) {
        // → Dua syarat harus terpenuhi:
        //   1. Item ditemukan (index != -1)
        //   2. Quantity masih > 0, agar tidak jadi negatif

            menuItems[index] = menuItems[index].copy(quantity = menuItems[index].quantity - 1)
            // → Sama seperti increase, tapi kurangi 1
        }
    }

    fun getItemById(itemId: Int): FoodItem? {
    // → Fungsi untuk mengambil satu item berdasarkan id-nya
    // → "FoodItem?" = hasilnya bisa null jika item tidak ditemukan
    //   (tanda "?" berarti nullable di Kotlin)

        return menuItems.find { it.id == itemId }
        // → "find" = cari item pertama yang id-nya cocok
        // → Jika tidak ditemukan, otomatis return null
    }

    fun formatPrice(price: Double): String {
    // → Fungsi untuk mengubah angka harga menjadi format rupiah yang rapi
    // → Menerima "price" bertipe Double, mengembalikan String

        return "Rp ${String.format("%,.0f", price).replace(',', '.')}"
        // → String.format("%,.0f", price) = format angka dengan pemisah ribuan
        //   Contoh: 25000.0 → "25,000"
        // → .replace(',', '.') = ganti koma dengan titik (format Indonesia)
        //   Contoh: "25,000" → "25.000"
        // → Hasil akhir: "Rp 25.000"
    }
}
```

---

## 3. `Navigation.kt` — Navigasi

> **Tugasnya:** Mengatur perpindahan antar halaman (screen) di aplikasi.

```kotlin
package com.example.utppam.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
// → Controller untuk mengontrol navigasi (pindah halaman, kembali, dll)

import androidx.navigation.NavType
// → Tipe data yang bisa dikirim antar halaman (Int, String, Boolean, dll)

import androidx.navigation.compose.NavHost
// → Composable yang menjadi "wadah" semua halaman/route

import androidx.navigation.compose.composable
// → Fungsi untuk mendaftarkan satu halaman ke dalam NavHost

import androidx.navigation.navArgument
// → Digunakan untuk mendefinisikan argumen/parameter yang dikirim ke route

sealed class Screen(val route: String) {
// → "sealed class" = class tertutup, hanya bisa punya subclass yang didefinisikan di sini
//   Berguna untuk mendaftarkan semua route agar tidak typo
// → Setiap Screen punya "route" yaitu string alamat halaman

    object Menu : Screen("menu")
    // → Route untuk halaman Menu List
    // → "object" = singleton, hanya ada satu instance
    // → Route-nya adalah string "menu"

    object Detail : Screen("detail/{itemId}") {
    // → Route untuk halaman Detail
    // → "{itemId}" = placeholder, akan diisi dengan id item yang dipilih
    //   Contoh: "detail/3" artinya buka detail item dengan id=3

        fun createRoute(itemId: Int) = "detail/$itemId"
        // → Fungsi helper untuk membuat route yang sudah diisi id-nya
        //   Contoh: createRoute(3) → "detail/3"
    }
}

@Composable
fun AppNavGraph(
// → Fungsi Composable utama yang mendaftarkan semua halaman

    navController: NavHostController,
    // → Controller navigasi yang diterima dari MainActivity

    viewModel: OrderViewModel
    // → ViewModel yang dibagikan ke semua halaman (state hoisting)
) {
    NavHost(
        navController = navController,
        // → Hubungkan NavHost dengan controller navigasi

        startDestination = Screen.Menu.route
        // → Halaman pertama yang tampil saat app dibuka adalah "menu"
    ) {

        composable(Screen.Menu.route) {
        // → Daftarkan route "menu" dan tentukan composable yang tampil

            MenuListScreen(
                viewModel = viewModel,
                // → Teruskan ViewModel ke MenuListScreen

                onItemClick = { itemId ->
                    navController.navigate(Screen.Detail.createRoute(itemId))
                    // → Saat item diklik, navigate ke route "detail/{id}"
                    //   Contoh: navigate("detail/3")
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            // → Daftarkan route "detail/{itemId}"

            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
            // → Definisikan bahwa "itemId" adalah argumen bertipe Int
            //   Navigation Compose akan otomatis parsing string "3" menjadi Int 3
        ) { backStackEntry ->
        // → "backStackEntry" berisi informasi halaman saat ini, termasuk argumennya

            val itemId = backStackEntry.arguments?.getInt("itemId") ?: return@composable
            // → Ambil nilai itemId dari argumen navigasi
            // → "?." = safe call, jika arguments null tidak crash
            // → "?: return@composable" = jika itemId null, keluar dari composable ini

            MenuDetailScreen(
                viewModel = viewModel,
                itemId = itemId,
                // → Kirim itemId ke MenuDetailScreen untuk mencari data item

                onBack = { navController.popBackStack() }
                // → Saat tombol back diklik, kembali ke halaman sebelumnya
                //   "popBackStack" = hapus halaman ini dari tumpukan navigasi
            )
        }
    }
}
```

---

## 4. `MainActivity.kt` — Entry Point

> **Tugasnya:** Pintu masuk aplikasi. Pertama kali dijalankan Android saat app dibuka.

```kotlin
package com.example.utppam

import android.os.Bundle
// → Bundle = objek untuk menyimpan state Activity (dipakai Android secara internal)

import androidx.activity.ComponentActivity
// → Base class untuk Activity yang menggunakan Jetpack Compose

import androidx.activity.compose.setContent
// → Fungsi untuk menentukan tampilan Compose yang akan ditampilkan Activity

import androidx.activity.viewModels
// → Delegate property untuk membuat ViewModel yang terikat ke Activity lifecycle

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
// → Fungsi untuk membuat NavController yang diingat selama recomposisi

class MainActivity : ComponentActivity() {
// → MainActivity mewarisi ComponentActivity
//   ComponentActivity adalah versi modern Activity untuk Compose

    private val viewModel: OrderViewModel by viewModels()
    // → Membuat instance OrderViewModel menggunakan delegate "by viewModels()"
    // → "private val" = hanya bisa diakses dari dalam class ini
    // → "by viewModels()" = Android yang urus pembuatan dan penyimpanan ViewModel
    //   ViewModel ini akan tetap hidup selama app tidak ditutup (survive rotasi layar)

    override fun onCreate(savedInstanceState: Bundle?) {
    // → "onCreate" dipanggil pertama kali saat Activity dibuat
    // → "override" = menimpa fungsi dari parent class (ComponentActivity)
    // → "savedInstanceState" = data yang tersimpan jika Activity dibuat ulang

        super.onCreate(savedInstanceState)
        // → Panggil onCreate milik parent class dulu (wajib)

        setContent {
        // → Mulai mendefinisikan UI menggunakan Compose

            FoodOrderingAppTheme {
            // → Bungkus semua UI dengan tema app (warna, font, dll dari Theme.kt)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    // → Surface mengisi seluruh layar

                    color = CreamBackground
                    // → Warna background seluruh app (dari Theme.kt)
                ) {
                    val navController = rememberNavController()
                    // → Buat NavController dan ingat selama recomposisi
                    // → "remember" di dalam rememberNavController menjaga
                    //   controller tidak dibuat ulang saat UI digambar ulang

                    AppNavGraph(
                        navController = navController,
                        viewModel = viewModel
                        // → Kirim navController dan viewModel ke sistem navigasi
                        //   Ini adalah penerapan "state hoisting":
                        //   state (viewModel) dibuat di atas dan diturunkan ke bawah
                    )
                }
            }
        }
    }
}
```

---

## 5. `Theme.kt` — Tampilan

> **Tugasnya:** Mendefinisikan semua warna dan tema Material3 yang dipakai seluruh aplikasi.

```kotlin
package com.example.utppam.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
// → lightColorScheme = skema warna untuk mode terang (light mode)

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
// → Kelas Color dari Compose untuk mendefinisikan warna

// ── DEFINISI WARNA ──────────────────────────────────────────

val OrangePrimary = Color(0xFFFF6B35)
// → Warna oranye utama app
// → "0xFF" = prefix warna hex di Kotlin (setara # di web)
// → "FF6B35" = kode warna hex (R=FF, G=6B, B=35)

val OrangeLight = Color(0xFFFF8C61)
// → Versi lebih terang dari OrangePrimary, untuk gradient

val OrangeDark = Color(0xFFE84A00)
// → Versi lebih gelap, dipakai untuk harga agar lebih kontras

val CreamBackground = Color(0xFFFFF8F5)
// → Warna background seluruh app (putih kekuningan/krem)

val SurfaceColor = Color(0xFFFFFFFF)
// → Putih bersih untuk permukaan card

val CardColor = Color(0xFFFFF3EE)
// → Warna card yang aktif (ada pesanan), sedikit oranye muda

val TextPrimary = Color(0xFF1A1A1A)
// → Warna teks utama (hampir hitam)

val TextSecondary = Color(0xFF6B6B6B)
// → Warna teks sekunder (abu-abu sedang), untuk deskripsi

val TextHint = Color(0xFFAAAAAA)
// → Warna teks hint (abu-abu terang), untuk placeholder/empty state

val GreenSuccess = Color(0xFF4CAF50)
// → Hijau untuk indikator sukses (disiapkan tapi belum dipakai di UI)

val RedError = Color(0xFFF44336)
// → Merah untuk error (disiapkan untuk validasi)

// ── COLOR SCHEME ────────────────────────────────────────────

private val LightColorScheme = lightColorScheme(
// → Mendefinisikan skema warna Material3
// → "private" = hanya dipakai di dalam file ini

    primary = OrangePrimary,
    // → Warna utama: tombol, icon aktif, dll

    onPrimary = Color.White,
    // → Warna teks/icon DI ATAS primary (harus kontras)

    primaryContainer = Color(0xFFFFE0D0),
    // → Warna container yang berkaitan dengan primary

    onPrimaryContainer = OrangeDark,
    // → Warna teks di atas primaryContainer

    secondary = Color(0xFF8D5524),
    // → Warna sekunder (coklat)

    onSecondary = Color.White,

    background = CreamBackground,
    // → Warna background default seluruh app

    surface = SurfaceColor,
    // → Warna permukaan (card, sheet, dll)

    onBackground = TextPrimary,
    // → Warna teks di atas background

    onSurface = TextPrimary,
    // → Warna teks di atas surface

    error = RedError,
    onError = Color.White
)

// ── THEME COMPOSABLE ────────────────────────────────────────

@Composable
fun FoodOrderingAppTheme(content: @Composable () -> Unit) {
// → Fungsi Composable yang membungkus seluruh UI dengan tema
// → "content: @Composable () -> Unit" = parameter berupa composable
//   (apapun yang ada di dalam {} saat memanggil FoodOrderingAppTheme)

    MaterialTheme(
        colorScheme = LightColorScheme,
        // → Terapkan skema warna yang sudah kita definisikan

        content = content
        // → Tampilkan konten yang dibungkus
    )
}
```

---

## 6. `MenuListScreen.kt` — Halaman Utama

> **Tugasnya:** Menampilkan daftar semua menu makanan beserta kontrol pesanan.

### Fungsi-fungsi di dalamnya:

#### `MenuListScreen` — Composable utama halaman

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
// → Izin untuk menggunakan API Material3 yang masih experimental (TopAppBar)

@Composable
fun MenuListScreen(
    viewModel: OrderViewModel,
    // → ViewModel yang berisi data dan fungsi pesanan

    onItemClick: (Int) -> Unit
    // → Callback: fungsi yang dipanggil saat item diklik
    // → "(Int) -> Unit" = menerima Int (itemId), tidak mengembalikan nilai
    // → Ini adalah state hoisting: aksi dikembalikan ke atas (ke Navigation)
) {
    var showOrderSummary by rememberSaveable { mutableStateOf(false) }
    // → State lokal untuk toggle panel Order Summary
    // → "rememberSaveable" = state ini BERTAHAN saat rotasi layar
    // → "mutableStateOf(false)" = nilai awal false (panel tersembunyi)
    // → "by" = delegate, agar bisa langsung pakai showOrderSummary
    //   tanpa perlu .value setiap saat

    Scaffold(
    // → Layout utama Material3 yang menyediakan slot TopAppBar, BottomBar, FAB, dll

        topBar = { ... },
        // → Slot untuk AppBar di bagian atas

        containerColor = CreamBackground
        // → Warna background Scaffold
    ) { paddingValues ->
    // → "paddingValues" = padding otomatis dari Scaffold agar konten
    //   tidak tertimpa TopAppBar atau BottomBar

        Column(...) {

            AnimatedVisibility(
                visible = showOrderSummary,
                // → Tampilkan/sembunyikan berdasarkan state showOrderSummary

                enter = expandVertically() + fadeIn(),
                // → Animasi masuk: membesar dari atas + muncul perlahan

                exit = shrinkVertically() + fadeOut()
                // → Animasi keluar: mengecil ke atas + menghilang perlahan
            ) {
                OrderSummaryPanel(viewModel = viewModel)
                // → Tampilkan panel ringkasan pesanan jika showOrderSummary = true
            }

            if (viewModel.menuItems.isEmpty()) {
                EmptyMenuState()
                // → Jika list kosong, tampilkan empty state
            } else {
                LazyColumn(
                // → List yang hanya merender item yang terlihat di layar
                // → Efisien untuk list panjang (tidak render semua sekaligus)

                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    // → Padding di dalam LazyColumn (kiri/kanan 16dp, atas/bawah 12dp)

                    verticalArrangement = Arrangement.spacedBy(12.dp)
                    // → Jarak 12dp antar item
                ) {
                    items(viewModel.menuItems, key = { it.id }) { item ->
                    // → Render setiap item dari menuItems
                    // → "key = { it.id }" = identitas unik tiap item
                    //   Membuat animasi dan update lebih efisien

                        MenuItemCard(
                            item = item,
                            onItemClick = { onItemClick(item.id) },
                            onIncrease = { viewModel.increaseQuantity(item.id) },
                            onDecrease = { viewModel.decreaseQuantity(item.id) },
                            formatPrice = viewModel::formatPrice
                            // → "viewModel::formatPrice" = referensi ke fungsi formatPrice
                            //   Dikirim sebagai parameter agar MenuItemCard bisa format harga
                        )
                    }

                    item {
                        if (viewModel.totalItems == 0) {
                            EmptyOrderState()
                            // → Tampilkan hint "Tap + untuk menambahkan" jika belum pesan
                        }
                    }
                }
            }
        }
    }
}
```

#### `OrderSummaryPanel` — Panel ringkasan pesanan

```kotlin
@Composable
fun OrderSummaryPanel(viewModel: OrderViewModel) {
// → Menampilkan ringkasan semua item yang sudah dipesan

    // Jika belum ada pesanan → tampilkan "No orders yet"
    if (viewModel.totalItems == 0) {
        Text(text = "No orders yet", ...)
    } else {
        // Filter hanya item yang quantity > 0, lalu tampilkan satu per satu
        viewModel.menuItems
            .filter { it.quantity > 0 }
            // → Saring: hanya item yang sudah dipesan

            .forEach { item ->
            // → Untuk setiap item yang sudah dipesan, tampilkan baris:
            //   [nama] [x jumlah] [subtotal]
            }

        // Total semua pesanan
        Text(text = "Total (${viewModel.totalItems} item)")
        Text(text = viewModel.formatPrice(viewModel.totalPrice))
    }
}
```

#### `MenuItemCard` — Card satu item makanan

```kotlin
@Composable
fun MenuItemCard(
    item: FoodItem,
    // → Data item yang akan ditampilkan

    onItemClick: () -> Unit,
    // → Callback saat card diklik (buka detail)

    onIncrease: () -> Unit,
    // → Callback saat tombol + diklik

    onDecrease: () -> Unit,
    // → Callback saat tombol - diklik

    formatPrice: (Double) -> String
    // → Fungsi untuk format harga
    // → Semua callback dikirim dari luar (state hoisting) agar
    //   MenuItemCard tidak perlu tahu soal ViewModel
) {
    Card(
        modifier = Modifier
            .shadow(if (item.quantity > 0) 6.dp else 2.dp, ...)
            // → Shadow lebih besar jika item sudah dipesan (visual feedback)

            .clickable { onItemClick() }
            // → Seluruh card bisa diklik untuk buka detail

        colors = CardDefaults.cardColors(
            containerColor = if (item.quantity > 0) Color(0xFFFFF3EE) else Color.White
            // → Warna card berubah saat ada pesanan (oranye muda vs putih)
        )
    ) {
        // Isi card: [emoji] [nama+harga] [tombol qty]

        // Tombol quantity: tampilan berbeda tergantung kondisi
        if (item.quantity > 0) {
            // → Tampilkan: [−] [angka] [+] dalam satu baris oranye
        } else {
            // → Tampilkan: hanya tombol [+] bulat
        }

        // Garis oranye di bawah card jika item sudah dipesan
        if (item.quantity > 0) {
            Box(modifier = Modifier.height(3.dp).background(gradient))
        }
    }
}
```

#### `EmptyOrderState` & `EmptyMenuState`

```kotlin
@Composable
fun EmptyOrderState() {
// → Tampil di bawah list saat belum ada item yang dipesan sama sekali
// → Isi: emoji 👆 dan teks "Tap + untuk menambahkan pesanan"
}

@Composable
fun EmptyMenuState() {
// → Tampil menggantikan seluruh list jika menuItems kosong
// → Isi: emoji 🍽️ dan teks "Menu tidak tersedia"
}
```

---

## 7. `MenuDetailScreen.kt` — Halaman Detail

> **Tugasnya:** Menampilkan detail satu item makanan dan memungkinkan pengguna mengubah jumlah pesanan.

```kotlin
@Composable
fun MenuDetailScreen(
    viewModel: OrderViewModel,
    itemId: Int,
    // → ID item yang ingin ditampilkan detailnya

    onBack: () -> Unit
    // → Callback untuk kembali ke halaman sebelumnya
) {
    val item = viewModel.menuItems.find { it.id == itemId }
    // → Cari item dari list berdasarkan itemId
    // → Karena menuItems adalah mutableStateListOf, perubahan quantity
    //   di sini otomatis update tampilan (reactive)

    if (item == null) { return }
    // → Jika item tidak ditemukan (misalnya id salah), keluar dari composable

    val scrollState = rememberScrollState()
    // → State untuk scroll halaman ini
    // → "rememberScrollState()" menggunakan "remember" di dalamnya
    //   agar posisi scroll tidak hilang saat recomposisi

    val buttonScale by animateFloatAsState(
        targetValue = if (item.quantity > 0) 1f else 0.95f,
        // → Animasi skala tombol: 100% jika sudah ada pesanan, 95% jika belum
        label = "buttonScale"
    )
    // → "animateFloatAsState" = animasi angka float secara otomatis
    // → "by" delegate → bisa langsung pakai buttonScale tanpa .value

    Scaffold(...) { paddingValues ->

        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                // → Aktifkan scroll vertikal menggunakan scrollState di atas
        ) {

            // ── HERO SECTION ──
            Box(modifier = Modifier.height(240.dp).background(gradient)) {
                Text(text = item.emoji, fontSize = 100.sp)
                // → Tampilkan emoji besar sebagai "foto" makanan

                Box(...) { Text(text = item.category) }
                // → Badge kategori di pojok kanan atas hero
            }

            // ── DETAIL CARD ──
            Card(modifier = Modifier.offset(y = (-20).dp)) {
            // → "offset(y = -20.dp)" = geser card 20dp ke atas
            //   Efek card menumpuk di atas hero section

                Text(text = item.name)        // Nama makanan
                Text(text = formatPrice(...)) // Harga
                Divider()                     // Garis pemisah
                Text(text = item.description) // Deskripsi panjang

                // ── QUANTITY CONTROL ──
                Card(colors = CardColor) {
                    Row {
                        IconButton(
                            onClick = { viewModel.decreaseQuantity(item.id) },
                            enabled = item.quantity > 0
                            // → Tombol - dinonaktifkan jika quantity sudah 0
                            // → Mencegah quantity jadi negatif
                        )

                        Text(text = item.quantity.toString(), fontSize = 40.sp)
                        // → Angka besar di tengah menunjukkan jumlah pesanan

                        IconButton(onClick = { viewModel.increaseQuantity(item.id) })
                        // → Tombol +
                    }

                    if (item.quantity > 0) {
                        Text("Subtotal: ${formatPrice(item.price * item.quantity)}")
                        // → Hanya tampil jika sudah ada pesanan
                    }
                }

                // ── TOTAL SEMUA PESANAN ──
                if (viewModel.totalItems > 0) {
                    // → Tampilkan total seluruh pesanan (bukan hanya item ini)
                    Text("${viewModel.totalItems} item")
                    Text(viewModel.formatPrice(viewModel.totalPrice))
                } else {
                    Text("No orders yet")
                    // → Empty state jika belum ada pesanan apapun
                }

                // ── TOMBOL AKSI ──
                Button(
                    onClick = { viewModel.increaseQuantity(item.id) },
                    modifier = Modifier.scale(buttonScale)
                    // → Terapkan animasi skala pada tombol
                ) {
                    Text(if (item.quantity == 0) "Tambah ke Pesanan" else "Tambah Lagi")
                    // → Teks berubah tergantung apakah sudah dipesan atau belum
                }

                OutlinedButton(onClick = onBack) {
                    Text("Kembali ke Menu")
                    // → Tombol outline untuk kembali ke halaman list
                }
            }
        }
    }
}
```

---

## 🔄 Ringkasan Alur Data

```
MainActivity
│  Membuat OrderViewModel (bertahan selama app hidup)
│  Membuat NavController
│
└─► AppNavGraph (Navigation.kt)
    │  Mengatur route dan perpindahan halaman
    │
    ├─► MenuListScreen
    │   │  State lokal: showOrderSummary (rememberSaveable)
    │   │  Baca: viewModel.menuItems, viewModel.totalItems
    │   │  Aksi: viewModel.increaseQuantity(), viewModel.decreaseQuantity()
    │   │
    │   └─► [klik item] → navigate ke detail/{itemId}
    │
    └─► MenuDetailScreen
        │  State scroll: rememberScrollState() (remember)
        │  Baca: viewModel.menuItems.find(itemId), viewModel.totalItems, viewModel.totalPrice
        │  Aksi: viewModel.increaseQuantity(), viewModel.decreaseQuantity()
        │
        └─► [klik back] → popBackStack() → kembali ke MenuListScreen

OrderViewModel
│  Data: menuItems (mutableStateListOf) ← sumber kebenaran tunggal
│  Computed: totalItems, totalPrice (dihitung otomatis dari menuItems)
│  Fungsi: increaseQuantity, decreaseQuantity, getItemById, formatPrice
```
