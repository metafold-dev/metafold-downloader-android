import { initializeApp } from "https://www.gstatic.com/firebasejs/10.12.5/firebase-app.js";
import {
  getAuth,
  onAuthStateChanged,
  signInWithEmailAndPassword,
  signOut,
} from "https://www.gstatic.com/firebasejs/10.12.5/firebase-auth.js";
import {
  Timestamp,
  collection,
  deleteDoc,
  doc,
  getDoc,
  getDocs,
  getFirestore,
  serverTimestamp,
  setDoc,
  updateDoc,
} from "https://www.gstatic.com/firebasejs/10.12.5/firebase-firestore.js";

const firebaseConfig = {
  apiKey: "AIzaSyDp9eEdjo-pPS76MVTR8O-cmlWtYycXSy0",
  authDomain: "metafold-downloader.firebaseapp.com",
  projectId: "metafold-downloader",
  storageBucket: "metafold-downloader.firebasestorage.app",
  messagingSenderId: "142355025543",
  appId: "1:142355025543:web:0f3f8218b4fad4acf0b222",
  measurementId: "G-4S3RT24H12",
};

const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const db = getFirestore(app);

const $ = (id) => document.getElementById(id);

const els = {
  sessionLabel: $("sessionLabel"),
  signOutBtn: $("signOutBtn"),
  loginView: $("loginView"),
  adminView: $("adminView"),
  loginEmail: $("loginEmail"),
  loginPassword: $("loginPassword"),
  loginBtn: $("loginBtn"),
  authHint: $("authHint"),
  adminNotice: $("adminNotice"),
  adminNoticeTitle: $("adminNoticeTitle"),
  adminNoticeText: $("adminNoticeText"),
  adminUidText: $("adminUidText"),
  copyUidBtn: $("copyUidBtn"),
  totalCount: $("totalCount"),
  activeCount: $("activeCount"),
  pendingCount: $("pendingCount"),
  expiredCount: $("expiredCount"),
  listStatus: $("listStatus"),
  refreshBtn: $("refreshBtn"),
  searchInput: $("searchInput"),
  statusFilter: $("statusFilter"),
  newEmail: $("newEmail"),
  createBtn: $("createBtn"),
  licenseList: $("licenseList"),
  editorTitle: $("editorTitle"),
  editorSubtitle: $("editorSubtitle"),
  statusBadge: $("statusBadge"),
  emptyEditor: $("emptyEditor"),
  editorForm: $("editorForm"),
  emailField: $("emailField"),
  statusField: $("statusField"),
  expiresField: $("expiresField"),
  ownerField: $("ownerField"),
  licenseKeyField: $("licenseKeyField"),
  requestIdText: $("requestIdText"),
  deviceText: $("deviceText"),
  deviceChangeText: $("deviceChangeText"),
  versionText: $("versionText"),
  saveBtn: $("saveBtn"),
  approveBtn: $("approveBtn"),
  deactivateBtn: $("deactivateBtn"),
  resetDeviceBtn: $("resetDeviceBtn"),
  deleteBtn: $("deleteBtn"),
  downloadsEnabledField: $("downloadsEnabledField"),
  minVersionCodeField: $("minVersionCodeField"),
  minProtocolField: $("minProtocolField"),
  runtimeMessageField: $("runtimeMessageField"),
  runtimeState: $("runtimeState"),
  saveRuntimeBtn: $("saveRuntimeBtn"),
  lockOldBtn: $("lockOldBtn"),
  toast: $("toast"),
};

const state = {
  user: null,
  admin: false,
  adminCheckPending: false,
  licenses: [],
  selectedId: "",
};

function toast(message) {
  els.toast.textContent = message;
  els.toast.classList.remove("hidden");
  window.clearTimeout(toast.timer);
  toast.timer = window.setTimeout(() => els.toast.classList.add("hidden"), 3400);
}

function setBusy(button, busy, label) {
  button.disabled = busy;
  if (label) {
    if (!button.dataset.label) button.dataset.label = button.textContent;
    button.textContent = busy ? label : button.dataset.label;
  }
}

function normalizeEmail(value) {
  return String(value || "").trim().toLowerCase();
}

async function sha256Hex(value) {
  const bytes = new TextEncoder().encode(value);
  const hash = await crypto.subtle.digest("SHA-256", bytes);
  return [...new Uint8Array(hash)].map((byte) => byte.toString(16).padStart(2, "0")).join("");
}

function requestIdForDoc(id) {
  return `REQ-${id.slice(0, 12).toUpperCase()}`;
}

function addMonthsDate(months) {
  const date = new Date();
  date.setMonth(date.getMonth() + months);
  return date.toISOString().slice(0, 10);
}

function parseExpiryMillis(value) {
  const text = String(value || "").trim();
  if (!text) return 0;
  const normalized = text.replace(/[./]/g, "-");
  const date = new Date(`${normalized}T23:59:59.999`);
  return Number.isFinite(date.getTime()) ? date.getTime() : -1;
}

function isExpired(row) {
  const expiry = parseExpiryMillis(row.expiresAt);
  return expiry > 0 && Date.now() > expiry;
}

function badgeStatus(row) {
  if (isExpired(row)) return "expired";
  return row.status || "pending";
}

function timestampLabel(value) {
  if (!value) return "-";
  if (typeof value.toDate === "function") return value.toDate().toLocaleString("tr-TR");
  if (typeof value === "string") return value;
  return "-";
}

function selectedRow() {
  return state.licenses.find((item) => item.id === state.selectedId) || null;
}

function setAdminNotice(title, text, uid) {
  const visible = Boolean(title || text || uid);
  els.adminNotice.classList.toggle("hidden", !visible);
  els.adminNoticeTitle.textContent = title || "";
  els.adminNoticeText.textContent = text || "";
  els.adminUidText.textContent = uid || "-";
}

function renderLoginButton(signedIn) {
  if (!signedIn) {
    els.loginBtn.disabled = false;
    els.loginBtn.textContent = "Giris Yap";
    return;
  }
  els.loginBtn.disabled = true;
  els.loginBtn.textContent = state.adminCheckPending ? "Yetki kontrol ediliyor..." : "Giris yapildi";
}

function renderShell() {
  const signedIn = Boolean(state.user);
  els.loginView.classList.toggle("hidden", signedIn && state.admin);
  els.adminView.classList.toggle("hidden", !signedIn || !state.admin);
  els.signOutBtn.classList.toggle("hidden", !signedIn);
  els.sessionLabel.textContent = signedIn ? state.user.email : "Giris bekleniyor";
  renderLoginButton(signedIn);
}

async function checkAdmin(user) {
  const adminSnap = await getDoc(doc(db, "admins", user.uid));
  return adminSnap.exists();
}

async function login() {
  setBusy(els.loginBtn, true, "Giris yapiliyor...");
  els.authHint.textContent = "";
  setAdminNotice("", "", "");
  try {
    await signInWithEmailAndPassword(auth, normalizeEmail(els.loginEmail.value), els.loginPassword.value);
  } catch (error) {
    els.authHint.textContent = cleanError(error);
  } finally {
    if (auth.currentUser) {
      renderShell();
    } else {
      setBusy(els.loginBtn, false, "Giris Yap");
    }
  }
}

async function loadLicenses() {
  els.listStatus.textContent = "Yukleniyor";
  const snapshot = await getDocs(collection(db, "license_requests"));
  state.licenses = snapshot.docs.map((item) => ({ id: item.id, ...item.data() }));
  state.licenses.sort((a, b) => String(a.email || "").localeCompare(String(b.email || "")));
  els.listStatus.textContent = `${state.licenses.length} kayit`;
  renderStats();
  renderLicenseList();
}

function renderStats() {
  const total = state.licenses.length;
  const active = state.licenses.filter((item) => item.status === "active" && !isExpired(item)).length;
  const pending = state.licenses.filter((item) => item.status === "pending").length;
  const expired = state.licenses.filter((item) => isExpired(item)).length;
  els.totalCount.textContent = total;
  els.activeCount.textContent = active;
  els.pendingCount.textContent = pending;
  els.expiredCount.textContent = expired;
}

function renderLicenseList() {
  const search = normalizeEmail(els.searchInput.value);
  const filter = els.statusFilter.value;
  const rows = state.licenses.filter((item) => {
    const status = badgeStatus(item);
    const searchable = [
      item.email,
      item.status,
      item.owner,
      item.deviceId,
      item.deviceLabel,
      item.requestId,
      item.appVersion,
    ].join(" ").toLowerCase();
    return (!search || searchable.includes(search)) && (!filter || status === filter);
  });

  els.licenseList.replaceChildren();
  if (!rows.length) {
    const empty = document.createElement("div");
    empty.className = "empty";
    empty.textContent = "Kayit bulunamadi.";
    els.licenseList.append(empty);
    return;
  }

  for (const row of rows) {
    const card = document.createElement("button");
    card.className = `license-card ${row.id === state.selectedId ? "active-card" : ""}`;
    card.type = "button";
    card.addEventListener("click", () => selectLicense(row.id));

    const copy = document.createElement("div");
    const title = document.createElement("strong");
    title.textContent = row.email || row.id;
    const meta = document.createElement("div");
    meta.className = "license-meta";
    meta.textContent = `${row.owner || "Sahip yok"} · ${row.expiresAt || "suresiz"} · ${row.deviceLabel || "cihaz yok"}`;
    copy.append(title, meta);

    const badge = document.createElement("span");
    const status = badgeStatus(row);
    badge.className = `badge ${status}`;
    badge.textContent = status;
    card.append(copy, badge);
    els.licenseList.append(card);
  }
}

function selectLicense(id) {
  state.selectedId = id;
  const row = selectedRow();
  if (!row) return;

  els.emptyEditor.classList.add("hidden");
  els.editorForm.classList.remove("hidden");
  els.editorTitle.textContent = row.email || "Lisans";
  els.editorSubtitle.textContent = row.requestId || row.id;
  const status = badgeStatus(row);
  els.statusBadge.className = `badge ${status}`;
  els.statusBadge.textContent = status;

  els.emailField.value = row.email || "";
  els.statusField.value = row.status || "pending";
  els.expiresField.value = row.expiresAt || "";
  els.ownerField.value = row.owner || "";
  els.licenseKeyField.value = row.licenseKey || "";
  els.requestIdText.textContent = row.requestId || requestIdForDoc(row.id);
  els.deviceText.textContent = `${row.deviceLabel || "-"} / ${row.deviceId || "-"}`;
  els.deviceChangeText.textContent = timestampLabel(row.nextDeviceChangeAt);
  els.versionText.textContent = row.appVersion || "-";

  renderLicenseList();
}

async function saveSelected(patch = {}) {
  const row = selectedRow();
  if (!row) return;
  const data = {
    status: els.statusField.value,
    owner: els.ownerField.value.trim(),
    expiresAt: els.expiresField.value.trim(),
    licenseKey: els.licenseKeyField.value.trim(),
    lastAdminUpdateAt: serverTimestamp(),
    lastAdminUpdateBy: state.user.email,
    ...patch,
  };
  await updateDoc(doc(db, "license_requests", row.id), data);
  toast("Lisans kaydedildi.");
  await loadLicenses();
  selectLicense(row.id);
}

async function createLicense() {
  const email = normalizeEmail(els.newEmail.value);
  if (!email || !email.includes("@")) {
    toast("Gecerli e-posta gir.");
    return;
  }
  setBusy(els.createBtn, true, "Olusturuluyor...");
  try {
    const id = await sha256Hex(email);
    const ref = doc(db, "license_requests", id);
    const snap = await getDoc(ref);
    if (snap.exists()) {
      await updateDoc(ref, {
        status: "active",
        owner: "Ahmet Dogan",
        lastAdminUpdateAt: serverTimestamp(),
        lastAdminUpdateBy: state.user.email,
      });
    } else {
      await setDoc(ref, {
        email,
        deviceId: "__unbound__",
        deviceLabel: "Unbound",
        packageName: "com.metafold.videodownloader",
        appVersion: "",
        status: "active",
        requestId: requestIdForDoc(id),
        owner: "Ahmet Dogan",
        expiresAt: addMonthsDate(12),
        licenseKey: "",
        createdAt: String(Date.now()),
        lastDeviceChangeAt: Timestamp.fromMillis(0),
        nextDeviceChangeAt: Timestamp.fromMillis(0),
        adminCreatedAt: serverTimestamp(),
        adminCreatedBy: state.user.email,
      });
    }
    els.newEmail.value = "";
    toast("Lisans olusturuldu.");
    await loadLicenses();
    selectLicense(id);
  } finally {
    setBusy(els.createBtn, false, "Lisans Olustur");
  }
}

async function resetDeviceLock() {
  const row = selectedRow();
  if (!row || !confirm("Bu lisansi yeni cihaza acmak istiyor musun?")) return;
  await saveSelected({
    deviceId: "__unbound__",
    deviceLabel: "Unbound",
    lastDeviceChangeAt: Timestamp.fromMillis(0),
    nextDeviceChangeAt: Timestamp.fromMillis(0),
  });
}

async function deleteSelected() {
  const row = selectedRow();
  if (!row || !confirm(`${row.email || row.id} lisansi silinsin mi?`)) return;
  await deleteDoc(doc(db, "license_requests", row.id));
  state.selectedId = "";
  els.editorForm.classList.add("hidden");
  els.emptyEditor.classList.remove("hidden");
  toast("Lisans silindi.");
  await loadLicenses();
}

async function loadRuntime() {
  const snap = await getDoc(doc(db, "app_config", "runtime"));
  const data = snap.exists() ? snap.data() : {};
  els.downloadsEnabledField.checked = data.downloadsEnabled !== false;
  els.minVersionCodeField.value = data.minVersionCode ?? 54;
  els.minProtocolField.value = data.minDownloadProtocol ?? 2;
  els.runtimeMessageField.value = data.message || "";
  els.runtimeState.className = `badge ${els.downloadsEnabledField.checked ? "active" : "inactive"}`;
  els.runtimeState.textContent = els.downloadsEnabledField.checked ? "acik" : "kapali";
}

async function saveRuntime() {
  await setDoc(doc(db, "app_config", "runtime"), {
    downloadsEnabled: els.downloadsEnabledField.checked,
    minVersionCode: Number(els.minVersionCodeField.value || 0),
    minDownloadProtocol: Number(els.minProtocolField.value || 0),
    message: els.runtimeMessageField.value.trim(),
    updatedAt: serverTimestamp(),
    updatedBy: state.user.email,
  }, { merge: true });
  toast("Runtime kaydedildi.");
  await loadRuntime();
}

function cleanError(error) {
  const code = String(error?.code || "");
  const text = String(error?.message || error || "");
  const haystack = `${code} ${text}`;
  if (haystack.includes("permission-denied")) return "Yetki yok. Firebase Rules yayinlandi mi ve admins koleksiyonunda bu UID var mi kontrol et.";
  if (haystack.includes("auth/invalid-credential")) return "E-posta veya sifre hatali.";
  return text || "Islem tamamlanamadi.";
}

async function copyText(text) {
  const value = String(text || "").trim();
  if (!value) return;
  if (navigator.clipboard?.writeText) {
    await navigator.clipboard.writeText(value);
    return;
  }
  const textarea = document.createElement("textarea");
  textarea.value = value;
  textarea.setAttribute("readonly", "");
  textarea.style.position = "fixed";
  textarea.style.left = "-9999px";
  document.body.append(textarea);
  textarea.select();
  document.execCommand("copy");
  textarea.remove();
}

els.loginBtn.addEventListener("click", login);
els.loginPassword.addEventListener("keydown", (event) => {
  if (event.key === "Enter") login();
});
els.signOutBtn.addEventListener("click", () => signOut(auth));
els.copyUidBtn.addEventListener("click", async () => {
  await copyText(state.user?.uid || els.adminUidText.textContent);
  toast("UID kopyalandi.");
});
els.refreshBtn.addEventListener("click", loadLicenses);
els.searchInput.addEventListener("input", renderLicenseList);
els.statusFilter.addEventListener("change", renderLicenseList);
els.createBtn.addEventListener("click", createLicense);
els.saveBtn.addEventListener("click", () => saveSelected());
els.approveBtn.addEventListener("click", () => saveSelected({ status: "active" }));
els.deactivateBtn.addEventListener("click", () => saveSelected({ status: "inactive" }));
els.resetDeviceBtn.addEventListener("click", resetDeviceLock);
els.deleteBtn.addEventListener("click", deleteSelected);
els.saveRuntimeBtn.addEventListener("click", saveRuntime);
els.lockOldBtn.addEventListener("click", () => {
  els.downloadsEnabledField.checked = true;
  els.minVersionCodeField.value = 54;
  els.minProtocolField.value = 2;
  els.runtimeMessageField.value = "Bu surum artik desteklenmiyor. Lutfen uygulamayi guncelleyin.";
});

document.querySelectorAll("[data-months]").forEach((button) => {
  button.addEventListener("click", () => {
    els.expiresField.value = addMonthsDate(Number(button.dataset.months));
  });
});

document.querySelector("[data-clear-expiry]").addEventListener("click", () => {
  els.expiresField.value = "";
});

onAuthStateChanged(auth, async (user) => {
  state.user = user;
  state.admin = false;
  state.adminCheckPending = Boolean(user);
  if (!user) setAdminNotice("", "", "");
  renderShell();
  if (!user) return;
  setAdminNotice(
    "Admin yetkisi kontrol ediliyor",
    "Bu hesap icin Firestore admins kaydi okunuyor.",
    user.uid
  );
  try {
    state.admin = await checkAdmin(user);
    state.adminCheckPending = false;
    renderShell();
    if (!state.admin) {
      els.authHint.textContent = "Giris basarili, ancak admin yetkisi yok.";
      setAdminNotice(
        "Admin yetkisi yok",
        "Firestore > admins koleksiyonunda Document ID tam olarak bu UID olmali. Rules sayfasinda Publish yaptigindan da emin ol.",
        user.uid
      );
      toast("Admin yetkisi bulunamadi.");
      return;
    }
    els.authHint.textContent = "";
    setAdminNotice("", "", "");
    await Promise.all([loadLicenses(), loadRuntime()]);
  } catch (error) {
    state.adminCheckPending = false;
    renderShell();
    const message = cleanError(error);
    els.authHint.textContent = message;
    setAdminNotice("Admin kontrolu tamamlanamadi", message, user.uid);
    toast(message);
  }
});
