# MetaFold Downloader Android

MetaFold Downloader; YouTube, Facebook, Instagram, TikTok, Pinterest ve diger desteklenen platformlardan video veya ses indirmek icin gelistirilen Android uygulamasidir.

Uygulama platform secimi, format/cozunurluk secimi, playlist indirme, indirme listesi, tema/ayar ekranlari ve paylasilan linkleri yakalama akislari icin hazirlanmistir.

## Notlar

- Sadece size ait olan veya indirme izniniz bulunan herkese acik videolar icindir.
- DRM, ucretli icerik veya koruma atlatma amacli kullanilmaz.
- Cekirdek indirme motoru olarak GPL-3.0 lisansli `youtubedl-android` kullanilir.

## Build

Android Studio ile bu klasoru acip `app` konfigurasyonunu calistirin.

Komut satiri icin:

```powershell
cd C:\Users\Acer\Desktop\MetaFold_Servis\video_downloader_android
$env:JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'
.\gradlew.bat assembleDebug
```

Debug APK:

```text
app/build/outputs/apk/debug/app-arm64-v8a-debug.apk
```

`app-universal-debug.apk` iki ARM mimarisini birden icerir, fakat daha buyuktur.

## GitHub uzerinden uygulama guncellemesi

Uygulama `metafold-dev/metafold-downloader-android` reposundaki en son GitHub Release kaydini denetler.

Yeni surum yayinlamak icin:

1. `app/build.gradle` icindeki `versionCode` ve `versionName` degerlerini arttirin.
2. APK'yi build edin.
3. GitHub'da `v3.10` gibi yeni bir Release olusturun.
4. Release asset olarak APK dosyasini ekleyin.

Not: Android uygulamasi private GitHub reposuna tokensiz erisemez. Guncellemenin kullanicilarda calismasi icin release'in public erisilebilir olmasi veya public bir update JSON/API kullanilmasi gerekir. GitHub token'i APK icine gomulmemelidir.

## Lisans ve onay sistemi

Uygulamada lisans sistemi e-posta + cihaz kimligi + yonetici onayi mantigiyla hazirlandi. Kullanici e-posta ile kayit istegi gonderir; siz backend panelinde bu istegi onayladiginizda uygulama `active: true` cevabini alir ve indirme/akis ozelliklerini kullanabilir.

Gercek takip icin `MainActivity.java` icindeki su adresleri kendi API adreslerinizle doldurun:

- `LICENSE_REGISTER_URL`: e-posta ile ilk kayit istegi
- `LICENSE_VALIDATE_URL`: uygulama acilinca veya kullanici kontrol ettiginde onay/lisans durumu

Kayit isteginde uygulama sunucuya sunlari gonderir:

```json
{
  "email": "kullanici@example.com",
  "device_id": "android-device-id",
  "device_label": "Samsung SM-...",
  "package_name": "com.metafold.videodownloader",
  "app_version": "3.9"
}
```

Backend tarafinda tutulmasi mantikli alanlar:

- `email`
- `device_id`
- `license_key`
- `status`: `pending`, `active`, `inactive`
- `owner`
- `expires_at`
- `created_at`
- `approved_at`
- `approved_by`

Beklenen API cevabi:

```json
{
  "active": true,
  "status": "active",
  "message": "Lisans etkin",
  "email": "kullanici@example.com",
  "license_key": "MFD-XXXX-XXXX",
  "request_id": "REQ-1001",
  "owner": "Ahmet Dogan",
  "expires_at": "2027-12-31"
}
```

Onay bekleyen kullanici icin:

```json
{
  "active": false,
  "status": "pending",
  "message": "Onay bekleniyor",
  "email": "kullanici@example.com",
  "request_id": "REQ-1001"
}
```

Reddedilen veya suresi biten lisans icin `status: "inactive"` dondurun.
