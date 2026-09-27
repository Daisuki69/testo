# ⚡ QIEZKA

<div align="center">

```
   ██████╗ ██╗███████╗███████╗██╗  ██╗ █████╗ 
  ██╔═══██╗██║██╔════╝╚══███╔╝██║ ██╔╝██╔══██╗
  ██║   ██║██║█████╗    ███╔╝ █████╔╝ ███████║
  ██║▄▄ ██║██║██╔══╝   ███╔╝  ██╔═██╗ ██╔══██║
  ╚██████╔╝██║███████╗███████╗██║  ██╗██║  ██║
   ╚══▀▀═╝ ╚═╝╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝
```

### *This app is specifically made for me, do not blame me for bricked phones*

[![Android](https://img.shields.io/badge/Platform-Android%2010%20to%2016%20(API%2029--36)-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![React](https://img.shields.io/badge/Frontend-React%2019%20%7C%20TypeScript-61DAFB?style=for-the-badge&logo=react&logoColor=black)](https://react.dev/)
[![Capacitor](https://img.shields.io/badge/Bridge-Capacitor%207-119EFF?style=for-the-badge&logo=capacitor&logoColor=white)](https://capacitorjs.com/)
[![Vite](https://img.shields.io/badge/Bundler-Vite%206-646CFF?style=for-the-badge&logo=vite&logoColor=white)](https://vitejs.dev/)
[![Tailwind](https://img.shields.io/badge/Styling-Tailwind%20CSS%204-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white)](https://tailwindcss.com/)
[![Google Gemini](https://img.shields.io/badge/AI%20Evaluation-Google%20Gemini%202.0%20%2F%202.5-8E75B2?style=for-the-badge&logo=googlegemini&logoColor=white)](https://aistudio.google.com/)
[![Security](https://img.shields.io/badge/Security-Device%20Admin%20%2B%20Accessibility-E91E63?style=for-the-badge)](https://developer.android.com/guide/topics/admin/device-admin)
[![Overlay](https://img.shields.io/badge/Overlay-Floating%20Ball%20Timer-FF5722?style=for-the-badge)](https://developer.android.com/reference/android/view/WindowManager.LayoutParams#TYPE_APPLICATION_OVERLAY)
[![API Model](https://img.shields.io/badge/Privacy-100%25%20BYOK%20Zero--Telemetry-00C853?style=for-the-badge)](#-online-architecture--offline-resources-byok)
[![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)](LICENSE)

<br/>

**[⚡ Philosophy](#-philosophy-procrastination-prevention)** • 
**[⚖️ Comparison Matrix](#%EF%B8%8F-qiezka-vs-traditional-app-blockers)** • 
**[🏗️ Architecture](#%EF%B8%8F-system-architecture)** • 
**[🧠 Local App Classifier](#-local-app-classifier-engine-on-device-5-layer-heuristics)** • 
**[🚨 Consequence Lockdown](#-consequence-lockdown--operating-hours-700-pm--300-am)** • 
**[⚡ Operating Modes: Safemode vs Hardcore](#-operating-modes-safemode-vs-hardcore-247)** • 
**[🌐 Web & Browser Protection](#-web--browser-protection-accessibility-guard--local-dns-sinkhole)** • 
**[📰 Case Studies](#-case-studies--article-declutter-system)** • 
**[🔮 Floating Ball Overlay](#-floating-assistive-timer-ball)** • 
**[📋 App Directory](#-complete-application-reference-blocked--allowed)** • 
**[🛡️ Anti-Cheat & Security](#%EF%B8%8F-security--anti-cheat-architecture)** • 
**[🚀 Setup & ADB](#-setup-guide)** • 
**[📱 OEM Guides](#-oem-rom-optimization-guide)** • 
**[📂 Codebase Tour](#-codebase-architecture--tour)** • 
**[📜 Engineering Changelog](#-engineering-changelog--architecture-evolution-log)** • 
**[🔮 Future Roadmap & Forks](#-future-roadmap--ecosystem-forks)** • 
**[❓ FAQ](#-frequently-asked-questions-faq)**

</div>

SIREN 🛡️
High-Security Offline Android Focus & Anti-Distraction Engine


SIREN is a professional-grade, open-source focus lockdown utility for Android. By utilizing Device Owner administrative privileges (similar to corporate IT policies) combined with a local Edge AI Screen Sniper, Halanoi makes it virtually impossible to bypass your focus session or sneak past your study blocks.

---


## 🚀 Key Features

### 1. 3-Stage App Classifier 🧠
*   Stage 1 — Master Veto Gate: 
Android Settings, all OEM Phone Managers (MIUI Security Center, Samsung Device Care, ColorOS SafeCenter), and hardware bloatware (Joyose, GameCenter, PalmStore, Glance) are unconditionally blocked. Noexceptions, no appeal.
*   Stage 2 — Evaluation:
Apps on the KnownSafe.kt are allowed, Apps on the KnownDistracting.kt are blocked.
Qiezka uses table in order to know if it should block an app or not + Unified Policy toggle.
If an App is Undefined Qiezka will try to Evaluate it
CATEGORY_GAME always blocked. CATEGORY_SOCIAL blocked. CATEGORY_VIDEO and CATEGORY_NEWS blocked. CATEGORY_PRODUCTIVITY allowed. Combined with Positive and Negative Signals
*   Stage 3 — Universal System Gateway:
Apps on the OEM system partition (FLAG_SYSTEM) that passed all veto gates are allowed as legitimate hardware infrastructure.
*   Layer 5 — Conservative Fallback:
Unknown user-installed app with no category, no academic signals, not system-partitioned → blocked.
### 2. 2-Stage Web and PWA/TWA Classifier 🌐
*   Stage 1 — Evaluation: 
URL on KnwonSafeWeb.kt are allowed, URK on the KnownDistracting.kt are blocked.
Qiezka uses table in order to know if it should block a URL or not + Unified Policy toggle.
If a URL is Undefined Qiezka will try to Evaluate it
Live Chromium DOM scan reads the page's accessibility tree — academic signals promote it, distraction signals block it.
*   Stage 2 — Conservative Fallback:
Unknown URL with no academic signals, with or without distraction signals are Blocked.

### 3. Anti-Bypass Stack ⚔️
*   Device Admin — blocks uninstall.
*   Accessibility Service — Follows 3-Stage App Classifier and 2-Stage Web and PWA/TWA Classifier
*   Quick Settings Collapse — QS panel collapses via StatusBar reflection the moment it's expanded during lockdown (not working)
*   Home Shell Proxy (QiezkaHomeHandlerActivity holds ROLE_HOME) — pressing Home auto-revives the enforcement process if force-stopped. Delegation target validated against Stage 1 veto to prevent spoofing.
*   Launcher Change Interception — isFastHomeRoleEvent() detects role change dialogs sub-millisecond. 4-burst retry at 0/25/60/120ms dismisses the picker before it finishes inflating.
*   Boot Persistence — BootReceiver + AlarmManager exact alarms fire on ACTION_BOOT_COMPLETED. If lockdown was active, it resumes. If timer expired while powered off, Consequence Mode activates immediately.
*   Sideloaded Apps Detection — newly installed apps detected via installer/store package monitoring are immediately classified via 3-Stage App Classifier 🧠

---

## 🛠️ Step-by-Step Setup Guide:
To run SIREN Quiz App, your device must do the following steps:
On-Device or ADB

### 📱 On-Device Setup

Grant each permission directly from within the app via in-app buttons:

| Permission | Type | Purpose |
|---|---|---|
| Accessibility Service | **Essential** | Intercepts distracting apps and Websites |
| Notifications | **Essential** | Countdown alerts, warnings |
| Unrestricted Background | **Essential** | Prevents OEM battery killers from stopping enforcement |
| Device Administrator | **Essential** | Blocks uninstalling QIEZKA during lockdown |

> On Android 13+: if Accessibility appears greyed out, tap **App Info** inside the onboarding — then tap the three-dot menu → *Allow restricted settings* — then return and grant it.

### 📱 ADB Setup (via `qiezka.bat`)

**Prerequisites:**
- PC
- Android platform-tools (ADB) installed in PC
- USB Debugging enabled on your phone (Developer Options)
- Phone connected via USB and authorized

**Run the script:**
```
Double-click qiezka.bat and at the User Account Control pop-up select yes
```

The script runs the following steps:
1. Automatic single device Detected, User controlled if multiple devices detected
2. Installs the APK (or updates it keeping all existing data)
3. Grants system permissions
4. Whitelists QIEZKA from aggressive OEM battery killers (Samsung, Xiaomi, Pixel)

After the script finishes, QIEZKA launches automatically on your phone. **Disconnect the USB — setup is complete.**

App was Tested on
Redmi Note 12 4G (tapas) running Android 16 QPR1 Evolution-X 11.5 ROM
SAMSUNG Galaxy A30 running Android 11 One UI 3.1


