# OnlyBurger Android

Native Android client (Kotlin + Jetpack Compose) for the OnlyBurger backend in
`../OnlyBurger.Api`. It reuses the same products, prices (RSD), names and images as the
web app.

Customers can browse the menu, manage a cart, place orders, pay (simulated), and cancel
pending orders.

## Requirements

- Android Studio (recent stable; this project was built with the 2026.1 toolchain).
- Android SDK with API 36 (Android Studio installs it on first sync).
- JDK 17+ (Android Studio's bundled JDK is used automatically).

## Run it

1. Start the backend first (it must be reachable from the emulator):

   ```bash
   cd ../OnlyBurger.Api
   dotnet run
   ```

   It listens on `http://localhost:5169`.

2. Open the `OnlyBurgerAndroid` folder in Android Studio and let Gradle sync.
   `local.properties` already points at the SDK on this machine; if you move the project
   to another computer, update `sdk.dir` there (or delete it and let Studio recreate it).

3. Run the app on an emulator (Run > Run 'app'). The emulator reaches your PC's localhost
   via `10.0.2.2`, which is the default base URL.

   To run on a physical device instead, edit `BASE_URL` in
   [ApiConfig.kt](app/src/main/java/com/onlyburger/app/data/remote/ApiConfig.kt) to your
   PC's LAN IP (for example `http://192.168.1.20:5169/`) and add that IP to
   [network_security_config.xml](app/src/main/res/xml/network_security_config.xml).

### Building from the command line

```bash
./gradlew assembleDebug
```

The APK is written to `app/build/outputs/apk/debug/app-debug.apk`.

## Logging in

Use the seeded admin account, or register a new customer in the app:

- Username: `admin`
- Password: `Admin123!`

## Features

- Register / log in (JWT stored on the device; you stay signed in across launches).
- Browse the menu with product images, descriptions and RSD prices.
- Add to cart with quantity, adjust quantities, remove items; a live badge shows the count.
- Checkout with a delivery location; the order total is computed by the backend.
- View your orders with status and payment badges, pay (simulated), and cancel pending orders.

## Architecture

MVVM with a small manual dependency-injection container (no Hilt, to keep the build simple).

```
UI (Jetpack Compose screens)
  -> ViewModel (StateFlow UI state, coroutines)
    -> Repository
      -> Retrofit ApiService  ->  OnlyBurger backend
```

### Project structure

```
app/src/main/java/com/onlyburger/app/
├── OnlyBurgerApp.kt          Application; creates the DI container
├── MainActivity.kt           Top-level navigation (login / register / main)
├── di/AppContainer.kt        Single instances of Retrofit + repositories
├── data/
│   ├── TokenStore.kt         JWT + profile in SharedPreferences
│   ├── remote/
│   │   ├── ApiConfig.kt      Base URL
│   │   ├── ApiService.kt     Retrofit endpoints
│   │   ├── AuthInterceptor.kt  Adds the Bearer token
│   │   ├── NetworkModule.kt  Builds Retrofit/OkHttp
│   │   └── dto/Dtos.kt       Request/response models
│   └── repository/           Auth, Product, Cart, Order repositories
├── ui/
│   ├── MainScaffold.kt       Bottom-nav (Menu/Cart/Orders), top bar, cart badge
│   ├── navigation/           (navigation lives in MainActivity + MainScaffold)
│   ├── screens/              Login, Register, Menu, Cart, Orders
│   ├── components/           ProductImage, StatusBadge
│   ├── theme/                Material 3 brand theme
│   └── viewmodel/            Auth, Menu, Cart, Orders + factory
└── util/                     Money (RSD), ProductImages (id -> drawable), NetworkError
```

Product images live in `app/src/main/res/drawable/product_<id>.jpg` and are mapped by id
in [ProductImages.kt](app/src/main/java/com/onlyburger/app/util/ProductImages.kt), mirroring
the web app's convention.

## Note on the build configuration

AGP 9 enables "built-in Kotlin" and a new DSL by default. This project opts out via
`android.builtInKotlin=false` and `android.newDsl=false` in `gradle.properties` so it uses
the standard, widely-documented `org.jetbrains.kotlin.*` plugins and the classic `android {}`
DSL. Those compatibility flags are supported throughout AGP 9.x.
