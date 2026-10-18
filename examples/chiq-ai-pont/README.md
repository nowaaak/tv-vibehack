# CHiQ "AI PONT" (DVB93CQ, Changhong, codename samseong)

Android TV 11 (API 30), firmware RTO2.250508.001, 1.3 GB RAM, MediaTek. The vendor layer is
built by Homwee (`com.homwee.*`), ad and content recognition by Anoki (`tv.anoki.acr.controller`,
`com.cltv.fast`).

`debloat.txt` lists every package removed from this TV. System apps were removed with
`pm uninstall --user 0`: the APK stays in the read-only system image, so `cmd package install-existing <pkg>`
or a factory reset brings them back. `com.google.android.videos` was a Play Store install and is gone for good.
Five IPTV apps (IBO Player Pro, IPTV Smarters, Smarters Pro, Smarters Lite, BoostVision) were fully uninstalled.

| Group | Packages |
|---|---|
| Telemetry, ads, recommendations | `tv.anoki.acr.controller`, `com.cltv.fast`, `com.homwee.aipont`, `com.google.android.tvrecommendations`, `android.autoinstalls.config.homwee` |
| Vendor apps | `com.homwee.store`, `com.homwee.help`, `com.homwee.upgrade` (vendor app updater) |
| Preloaded apps | `com.google.android.feedback`, `com.google.android.play.games`, `com.google.android.youtube.tvmusic`, `com.google.android.videos` |
| Services the owner does not use | `com.google.android.katniss` (Assistant), `com.google.android.apps.mediashell` (Chromecast), `com.homwee.multiscreenshare`, `com.homwee.mmp.screenshare` (Miracast/AirPlay), `com.homwee.alexacontrol` |
| Stock launcher | `com.google.android.tvlauncher` |

Do not remove `com.google.android.leanbacklauncher.partnercustomizer`: it provides the `tvlauncher.widget`
content provider, and without it `com.mediatek.tv.service` crash-loops after the next reboot, which breaks
the Input key and other remote hotkeys.

Kept on purpose: `com.homwee.dmt.upgrade` (firmware updates), `com.homwee.synckey` (remote key
service), `com.mediatek.*` (Live TV, inputs, remote pairing), `com.homwee.factory`.

## Run from Windows (Git Bash)

```bash
export TV_HOST=192.168.1.50
tools/disable.sh examples/chiq-ai-pont/debloat.txt
tools/restore.sh examples/chiq-ai-pont/debloat.txt
tools/restore-backup.sh backup/<timestamp>
```

`restore.sh` re-enables the list and the stock launcher. `restore-backup.sh` compares the TV with a
backup folder, re-enables everything that was enabled at backup time and restores the home
activity and keyboard.

## Re-enable a single app

```bash
TV_HOST=192.168.1.50 MSYS_NO_PATHCONV=1 tools/venv/Scripts/python.exe -I tools/tv.py 'cmd package install-existing com.google.android.katniss'
```

## Launcher

Dock order, hidden apps and Live TV package: `launcher/app/src/main/res/values/config.xml`.
Background colour: `windowBackground` in `launcher/app/src/main/res/values/styles.xml`.
Rebuild and install with `TV_HOST=192.168.1.50 launcher/build-and-install.sh` (set `JAVA_HOME`
and `ANDROID_HOME` first on Windows).

The stock launcher has a higher HOME priority than any third-party app, so
`cmd package set-home-activity` only takes effect after it is disabled.

HDMI and Live TV screenshots come out empty because the video layer is protected.

The Wi-Fi pill shows the network name only when Location is switched on in the TV settings
(Android 11 restriction).
