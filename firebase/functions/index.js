"use strict";

const crypto = require("crypto");
const { onRequest } = require("firebase-functions/v2/https");
const { defineSecret } = require("firebase-functions/params");
const { initializeApp } = require("firebase-admin/app");
const { getFirestore, FieldValue } = require("firebase-admin/firestore");

initializeApp();

const db = getFirestore();
const ADMIN_TOKEN = defineSecret("ADMIN_TOKEN");
const REGION = "europe-west1";
const LICENSES = "licenses";

exports.registerLicense = onRequest({ region: REGION, cors: true }, async (req, res) => {
  if (handleCors(req, res)) return;
  if (req.method !== "POST") return jsonError(res, 405, "POST required");

  try {
    const body = parseBody(req);
    const email = normalizeEmail(body.email);
    const deviceId = clean(body.device_id);
    if (!isValidEmail(email)) return jsonError(res, 400, "Valid email required");
    if (!deviceId) return jsonError(res, 400, "device_id required");

    const ref = db.collection(LICENSES).doc(licenseDocId(email, deviceId));
    const snap = await ref.get();
    const now = FieldValue.serverTimestamp();

    if (!snap.exists) {
      const requestId = makeRequestId();
      await ref.set({
        requestId,
        email,
        deviceId,
        deviceLabel: clean(body.device_label),
        packageName: clean(body.package_name),
        appVersion: clean(body.app_version),
        licenseKey: makeLicenseKey(),
        status: "pending",
        owner: "",
        expiresAt: "",
        createdAt: now,
        updatedAt: now,
        lastSeenAt: now
      });
    } else {
      await ref.update({
        deviceLabel: clean(body.device_label),
        packageName: clean(body.package_name),
        appVersion: clean(body.app_version),
        updatedAt: now,
        lastSeenAt: now
      });
    }

    const updated = await ref.get();
    return res.status(200).json(publicPayload(updated.data()));
  } catch (error) {
    console.error(error);
    return jsonError(res, 500, "Registration failed");
  }
});

exports.validateLicense = onRequest({ region: REGION, cors: true }, async (req, res) => {
  if (handleCors(req, res)) return;
  if (req.method !== "POST") return jsonError(res, 405, "POST required");

  try {
    const body = parseBody(req);
    const email = normalizeEmail(body.email);
    const licenseKey = normalizeLicenseKey(body.license_key);
    const deviceId = clean(body.device_id);
    if (!deviceId) return jsonError(res, 400, "device_id required");

    const snap = await findLicense(email, licenseKey, deviceId);
    if (!snap) {
      return res.status(200).json({
        active: false,
        status: "pending",
        message: "Onay bekleniyor"
      });
    }

    const ref = db.collection(LICENSES).doc(snap.id);
    await ref.update({
      appVersion: clean(body.app_version),
      packageName: clean(body.package_name),
      deviceLabel: clean(body.device_label),
      lastSeenAt: FieldValue.serverTimestamp()
    });

    const data = (await ref.get()).data();
    if (data.status === "active" && isExpired(data.expiresAt)) {
      await ref.update({ status: "inactive", updatedAt: FieldValue.serverTimestamp() });
      data.status = "inactive";
    }

    return res.status(200).json(publicPayload(data));
  } catch (error) {
    console.error(error);
    return jsonError(res, 500, "Validation failed");
  }
});

exports.adminListLicenses = onRequest({ region: REGION, cors: true, secrets: [ADMIN_TOKEN] }, async (req, res) => {
  if (handleCors(req, res)) return;
  if (!requireAdmin(req, res)) return;

  const status = clean(req.query.status || "pending");
  const limit = Math.min(Math.max(parseInt(req.query.limit || "100", 10), 1), 300);
  let query = db.collection(LICENSES);
  if (status !== "all") query = query.where("status", "==", status);

  const snap = await query.limit(limit).get();
  const items = snap.docs
    .map((doc) => ({ id: doc.id, ...doc.data() }))
    .sort((a, b) => String(b.requestId || "").localeCompare(String(a.requestId || "")));

  return res.status(200).json({ items });
});

exports.adminApproveLicense = onRequest({ region: REGION, cors: true, secrets: [ADMIN_TOKEN] }, async (req, res) => {
  if (handleCors(req, res)) return;
  if (req.method !== "POST") return jsonError(res, 405, "POST required");
  if (!requireAdmin(req, res)) return;

  try {
    const body = parseBody(req);
    const ref = await licenseRefFromAdminBody(body);
    await ref.update({
      status: "active",
      owner: clean(body.owner) || "",
      expiresAt: clean(body.expires_at) || "",
      approvedBy: clean(body.approved_by) || "admin",
      approvedAt: FieldValue.serverTimestamp(),
      updatedAt: FieldValue.serverTimestamp()
    });
    const updated = await ref.get();
    return res.status(200).json(publicPayload(updated.data()));
  } catch (error) {
    console.error(error);
    return jsonError(res, 500, "Approve failed");
  }
});

exports.adminRejectLicense = onRequest({ region: REGION, cors: true, secrets: [ADMIN_TOKEN] }, async (req, res) => {
  if (handleCors(req, res)) return;
  if (req.method !== "POST") return jsonError(res, 405, "POST required");
  if (!requireAdmin(req, res)) return;

  try {
    const body = parseBody(req);
    const ref = await licenseRefFromAdminBody(body);
    await ref.update({
      status: "inactive",
      rejectReason: clean(body.reason),
      updatedAt: FieldValue.serverTimestamp()
    });
    const updated = await ref.get();
    return res.status(200).json(publicPayload(updated.data()));
  } catch (error) {
    console.error(error);
    return jsonError(res, 500, "Reject failed");
  }
});

async function findLicense(email, licenseKey, deviceId) {
  if (email) {
    const snap = await db.collection(LICENSES).doc(licenseDocId(email, deviceId)).get();
    if (snap.exists) return snap;
  }

  if (licenseKey) {
    const matches = await db.collection(LICENSES)
      .where("licenseKey", "==", licenseKey)
      .limit(10)
      .get();
    for (const doc of matches.docs) {
      if (doc.data().deviceId === deviceId) return doc;
    }
  }
  return null;
}

async function licenseRefFromAdminBody(body) {
  const id = clean(body.id || body.request_id);
  if (!id) throw new Error("id required");

  const direct = db.collection(LICENSES).doc(id);
  const directSnap = await direct.get();
  if (directSnap.exists) return direct;

  const byRequestId = await db.collection(LICENSES)
    .where("requestId", "==", id)
    .limit(1)
    .get();
  if (!byRequestId.empty) return byRequestId.docs[0].ref;

  throw new Error("license not found");
}

function publicPayload(data) {
  const expired = isExpired(data.expiresAt);
  const active = data.status === "active" && !expired;
  const status = active ? "active" : (data.status === "inactive" || expired ? "inactive" : "pending");
  return {
    active,
    status,
    message: active ? "Lisans etkin" : (status === "pending" ? "Onay bekleniyor" : "Lisans pasif"),
    email: data.email || "",
    license_key: data.licenseKey || "",
    request_id: data.requestId || "",
    owner: data.owner || "",
    expires_at: data.expiresAt || ""
  };
}

function parseBody(req) {
  if (typeof req.body === "object" && req.body !== null) return req.body;
  if (typeof req.body === "string" && req.body.trim()) return JSON.parse(req.body);
  return {};
}

function handleCors(req, res) {
  res.set("Access-Control-Allow-Origin", "*");
  res.set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
  res.set("Access-Control-Allow-Headers", "Content-Type, x-admin-token");
  if (req.method === "OPTIONS") {
    res.status(204).send("");
    return true;
  }
  return false;
}

function requireAdmin(req, res) {
  const expected = ADMIN_TOKEN.value();
  const provided = clean(req.get("x-admin-token"));
  if (!expected || provided !== expected) {
    jsonError(res, 401, "Admin token required");
    return false;
  }
  return true;
}

function jsonError(res, code, message) {
  return res.status(code).json({ active: false, status: "inactive", message });
}

function makeLicenseKey() {
  const part = () => crypto.randomBytes(2).toString("hex").toUpperCase();
  return `MFD-${part()}-${part()}-${part()}`;
}

function makeRequestId() {
  return `REQ-${Date.now().toString(36).toUpperCase()}-${crypto.randomBytes(2).toString("hex").toUpperCase()}`;
}

function licenseDocId(email, deviceId) {
  return crypto.createHash("sha256").update(`${email}|${deviceId}`).digest("hex").slice(0, 40);
}

function clean(value) {
  return String(value || "").trim();
}

function normalizeEmail(value) {
  return clean(value).toLowerCase();
}

function normalizeLicenseKey(value) {
  return clean(value).replace(/\s+/g, "").toUpperCase();
}

function isValidEmail(value) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value) && value.length <= 254;
}

function isExpired(expiresAt) {
  if (!expiresAt) return false;
  const time = Date.parse(`${expiresAt}T23:59:59Z`);
  return Number.isFinite(time) && Date.now() > time;
}
