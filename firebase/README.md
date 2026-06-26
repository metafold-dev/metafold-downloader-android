# MetaFold Downloader Firebase License Backend

Bu klasor MetaFold Downloader icin ayri Firebase projesinde calisacak lisans ve onay sistemidir. Diger MetaFold uygulamalariyla ayni Firebase projesini kullanmayin.

## Onerilen ayri proje

Firebase Console'da yeni proje acin:

```text
metafold-downloader-license
```

Proje ID baska bir hesap tarafindan alinmissa benzer bir ID secin ve deploy sonrasi Android uygulamadaki URL'leri buna gore guncelleyin.

## Kurulum

Firebase CLI bu bilgisayarda kurulu degilse:

```powershell
npm install -g firebase-tools
firebase login
```

Bu klasorde:

```powershell
cd C:\Users\Acer\Desktop\MetaFold_Servis\video_downloader_android\firebase
copy .firebaserc.example .firebaserc
firebase use --add
firebase functions:secrets:set ADMIN_TOKEN
cd functions
npm install
cd ..
firebase deploy --only firestore,functions,hosting
```

`ADMIN_TOKEN`, admin panelde e-posta kayitlarini onaylamak icin kullanacaginiz gizli anahtardir. APK icine konmaz.

## Android URL'leri

Deploy sonrasi Cloud Functions URL'leri su formatta olur:

```text
https://europe-west1-PROJECT_ID.cloudfunctions.net/registerLicense
https://europe-west1-PROJECT_ID.cloudfunctions.net/validateLicense
```

Bu iki adresi Android tarafinda `MainActivity.java` icindeki alanlara yazin:

```java
private static final String LICENSE_REGISTER_URL = "https://europe-west1-PROJECT_ID.cloudfunctions.net/registerLicense";
private static final String LICENSE_VALIDATE_URL = "https://europe-west1-PROJECT_ID.cloudfunctions.net/validateLicense";
```

Sonra APK'yi yeniden build edin.

## Lisans verme akisi

1. Kullanici uygulamada e-posta ile kayit olur.
2. Kayit Firestore `licenses` koleksiyonuna `pending` olarak duser.
3. Hosting ile yayinlanan `admin.html` panelini acin.
4. Cloud Functions base URL ve `ADMIN_TOKEN` girin.
5. Kaydi secip `Onayla` butonuna basin.
6. Kullanici uygulamada `Onay durumunu kontrol et` dediginde `active: true` alir.

## API cevaplari

Onay bekleyen kullanici:

```json
{
  "active": false,
  "status": "pending",
  "message": "Onay bekleniyor",
  "email": "kullanici@example.com",
  "license_key": "MFD-ABCD-1234-EF56",
  "request_id": "REQ-..."
}
```

Onayli kullanici:

```json
{
  "active": true,
  "status": "active",
  "message": "Lisans etkin",
  "email": "kullanici@example.com",
  "license_key": "MFD-ABCD-1234-EF56",
  "request_id": "REQ-...",
  "owner": "Ahmet Dogan",
  "expires_at": "2027-12-31"
}
```
