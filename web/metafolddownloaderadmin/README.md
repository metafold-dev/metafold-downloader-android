# MetaFold Downloader Admin

Bu klasor `https://www.metafold.net/metafolddownloaderadmin/` altinda statik panel olarak calisir.

## Firebase admin yetkisi

Panel Firebase Authentication ile giris yapar. Giriş yapan hesabin UID degeri Firestore `admins` koleksiyonunda dokuman ID olarak yoksa panel lisanslari okuyamaz/yazamaz.

1. Firebase Console > Authentication > Users alaninda admin e-postanizi bulun.
2. UID degerini kopyalayin.
3. Firestore > Data alaninda `admins` koleksiyonu olusturun.
4. Document ID olarak UID degerini girin.
5. Istege bagli alanlar:

```text
email: admin@example.com
role: owner
```

## URL

Cloudflare Pages klasorune bu dosyalar yuklendiginde panel:

```text
/metafolddownloaderadmin/
```

adresinden acilir.
