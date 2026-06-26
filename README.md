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
