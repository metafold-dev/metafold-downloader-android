# MetaFold Downloader Spark License Setup

Bu klasor MetaFold Downloader icin ayri Firebase projesinde calisacak ucretsiz Spark plan lisans sistemidir. Cloud Functions yoktur, Blaze gerekmez.

## Firebase projesi

Mevcut proje:

```text
metafold-downloader
```

## Kurulum

Firebase Console'da Firestore zaten acildi. Bu klasordeki kurallar sadece uygulamanin `pending` kayit olusturmasina ve kendi kaydini okumasina izin verir. Kullanici uygulamadan `active` yapamaz.

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

1. Kullanici uygulamada e-posta ile kayit olur.
2. Firestore'da `license_requests` koleksiyonunda yeni dokuman olusur.
3. Dokumani Firebase Console'dan acin.
4. `status` alanini `pending` yerine `active` yapin.
5. Isterseniz `owner`, `expiresAt`, `licenseKey` alanlarini da doldurun.
6. Kullanici uygulamada `Onay durumunu kontrol et` dediginde uygulama acilir.

## Beklenen dokuman alanlari

```text
collection: license_requests

email: kullanici@example.com
deviceId: android-device-id
deviceLabel: Samsung SM-...
packageName: com.metafold.videodownloader
appVersion: 3.9
status: pending | active | inactive
requestId: REQ-...
owner: Ahmet Dogan
expiresAt: 2027-12-31
licenseKey: MFD-...
createdAt: timestamp-ms
```

Spark planda Firestore ucretsiz kota 50 kisi icin fazlasiyla yeterlidir; video indirme Firebase uzerinden yapilmaz.
