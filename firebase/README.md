# MetaFold Downloader Spark License Setup

Bu klasor MetaFold Downloader icin ayri Firebase projesinde calisacak ucretsiz Spark plan lisans sistemidir. Cloud Functions yoktur, Blaze gerekmez.

## Firebase projesi

Mevcut proje:

```text
metafold-downloader
```

## Kurulum

Firebase Console'da Firestore zaten acildi. Ayrica Firebase Authentication urununu baslatin ve Email/Password girisini acin:

```text
Firebase Console > Authentication > Get started
Firebase Console > Authentication > Sign-in method > Email/Password > Enable
```

Uygulamada `CONFIGURATION_NOT_FOUND` gorulurse bu adim tamamlanmamistir veya Android tarafindaki `FIREBASE_WEB_API_KEY` farkli Firebase projesine aittir.

Bu klasordeki kurallar sadece oturum acmis kullanicinin `pending` kayit olusturmasina ve kendi e-posta kaydini okumasina izin verir. Kullanici uygulamadan `active` yapamaz.

Lisans dokumani e-posta bazlidir: bir e-posta hesabi ayni anda sadece tek cihaza baglanir. Aktif lisans baska cihazda acilirsa uygulama, `nextDeviceChangeAt` zamani dolmadan cihaz degisimine izin vermez. Sure dolduysa cihaz otomatik yeni cihaza aktarilir ve yeni 7 gunluk kilit baslar.

Kurallari deploy etmek icin Firebase CLI varsa:

```powershell
cd C:\Users\Acer\Desktop\MetaFold_Servis\video_downloader_android\firebase
copy .firebaserc.example .firebaserc
firebase login
firebase use metafold-downloader
firebase deploy --only firestore
```

Firebase CLI kullanmak istemezseniz `firestore.rules` icindeki kurallari Firebase Console > Firestore > Rules alanina yapistirip Publish edebilirsiniz.

## Android API Key

Uygulamanin Firestore REST API'ye baglanmasi icin Firebase Web API Key gerekir.

Firebase Console:

```text
Project settings > General > Web API Key
```

Bu degeri Android tarafinda `MainActivity.java` icindeki alana yazin:

```java
private static final String FIREBASE_WEB_API_KEY = "BURAYA_WEB_API_KEY";
```

`FIREBASE_PROJECT_ID` zaten `metafold-downloader` olarak ayarlandi.

## Lisans verme akisi

1. Kullanici uygulamada e-posta ve sifre ile kayit olur.
2. Firestore'da `license_requests` koleksiyonunda e-posta bazli yeni dokuman olusur.
3. Dokumani Firebase Console'dan acin.
4. `status` alanini `pending` yerine `active` yapin.
5. Isterseniz `owner`, `expiresAt`, `licenseKey` alanlarini da doldurun.
6. Kullanici uygulamada `Onay durumunu kontrol et` dediginde uygulama acilir.

## Sureli lisans

`expiresAt` alani lisansin bitis tarihidir. Bos birakilirsa lisans suresiz kabul edilir. Tarihi string olarak `yyyy-MM-dd` biciminde girin; uygulama o gunun sonunda lisansi bitmis sayar.

2026-06-27 tarihinden baslayan ornekler:

```text
1 ay:  2026-07-27
3 ay:  2026-09-27
6 ay:  2026-12-27
1 yil: 2027-06-27
```

Kullaniciyi onaylamak icin Firestore dokumaninda en az su alanlari duzenlemeniz yeterlidir:

```text
status: active
expiresAt: 2027-06-27
```

## Beklenen dokuman alanlari

```text
collection: license_requests

email: kullanici@example.com
deviceId: android-device-id
deviceLabel: Samsung SM-...
packageName: com.metafold.videodownloader
appVersion: 3.25
status: pending | active | inactive
requestId: REQ-...
owner: Ahmet Dogan
expiresAt: 2027-12-31
licenseKey: MFD-...
createdAt: timestamp-ms
lastDeviceChangeAt: timestamp
nextDeviceChangeAt: timestamp
```

Spark planda Firestore ucretsiz kota 50 kisi icin fazlasiyla yeterlidir; video indirme Firebase uzerinden yapilmaz.
