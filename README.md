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

Uygulamada lisans sistemi e-posta + cihaz kimligi + yonetici onayi mantigiyla hazirlandi. Ucretsiz Firebase Spark planda calismasi icin Cloud Functions kullanilmaz; uygulama Firestore'a dogrudan `pending` kayit olusturur ve sadece kendi kaydini okur.

Firebase Console'dan kaydi onaylamak icin `license_requests` koleksiyonunda ilgili dokumani acip:

```text
status: active
```

yapmaniz yeterlidir. `status: pending` onay bekler, `status: inactive` pasiftir.

Android tarafinda `MainActivity.java` icindeki su alan doldurulmalidir:

```java
private static final String FIREBASE_WEB_API_KEY = "BURAYA_WEB_API_KEY";
```

Firebase tarafini diger MetaFold projelerinden ayri tutmak amaciyla `firebase/` klasorunde Spark uyumlu Firestore kurallari ve kurulum notlari bulunur.

Kayit dokumani ornegi:

```json
{
  "email": "kullanici@example.com",
  "deviceId": "android-device-id",
  "deviceLabel": "Samsung SM-...",
  "packageName": "com.metafold.videodownloader",
  "appVersion": "3.33",
  "status": "pending",
  "requestId": "REQ-...",
  "owner": "Ahmet Dogan",
  "expiresAt": "2027-12-31",
  "licenseKey": "MFD-..."
}
```

## Uzaktan indirme kapisi

Uygulama, lisans dogrulamadan sonra Firestore `app_config/runtime` dokumanini okuyarak eski veya iptal edilmis indirme protokollerini kapatabilir. Video ve APK dosyalari Firebase'den indirilmez; Firebase sadece kucuk lisans ve runtime config dokumanlarini okur.

## Web admin paneli

`web/metafolddownloaderadmin` klasoru `metafold.net/metafolddownloaderadmin/` icin statik lisans panelidir. Panel lisanslari listeleyebilir, aktif/pasif yapabilir, sure atayabilir, cihaz kilidini sifirlayabilir ve `app_config/runtime` ayarini duzenleyebilir.

Panelin yazma yetkisi icin Firebase Auth admin hesabinin UID degeri Firestore'da `admins/{uid}` dokumani olarak eklenmelidir. Guncel Firestore rules icindeki `isAdmin()` kontrolu bu dokumani kullanir.
