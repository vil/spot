# Pörssisähkö

Secure and minimal Android app to check the current electricity price in Finland.

<a href="obtainium%3A%2F%2Fapp%2F%7B%22id%22%3A%22dev.vili.spot%22%2C%22url%22%3A%22https%3A%2F%2Fgithub.com%2Fvil%2Fspot%22%2C%22author%22%3A%22Vili%22%2C%22name%22%3A%22P%C3%B6rssis%C3%A4hk%C3%B6%22%2C%22preferredApkIndex%22%3A0%2C%22additionalSettings%22%3A%22%7B%5C%22includePrereleases%5C%22%3Afalse%2C%5C%22fallbackToOlderReleases%5C%22%3Atrue%2C%5C%22filterReleaseTitlesByRegEx%5C%22%3A%5C%22%5C%22%2C%5C%22filterReleaseNotesByRegEx%5C%22%3A%5C%22%5C%22%2C%5C%22verifyLatestTag%5C%22%3Atrue%2C%5C%22dontSortReleasesList%5C%22%3Afalse%2C%5C%22useLatestAssetDateAsReleaseDate%5C%22%3Afalse%2C%5C%22trackOnly%5C%22%3Afalse%2C%5C%22versionExtractionRegEx%5C%22%3A%5C%22%5C%22%2C%5C%22matchGroupToUse%5C%22%3A%5C%22%5C%22%2C%5C%22versionDetection%5C%22%3Atrue%2C%5C%22releaseDateAsVersion%5C%22%3Afalse%2C%5C%22useVersionCodeAsOSVersion%5C%22%3Afalse%2C%5C%22apkFilterRegEx%5C%22%3A%5C%22%5C%22%2C%5C%22invertAPKFilter%5C%22%3Afalse%2C%5C%22autoApkFilterByArch%5C%22%3Atrue%2C%5C%22appName%5C%22%3A%5C%22P%C3%B6rssis%C3%A4hk%C3%B6%5C%22%2C%5C%22exemptFromBackgroundUpdates%5C%22%3Afalse%2C%5C%22skipUpdateNotifications%5C%22%3Afalse%2C%5C%22about%5C%22%3A%5C%22Secure%20and%20minimal%20Android%20app%20to%20check%20the%20current%20electricity%20price%20in%20Finland.%5C%22%2C%5C%22appAuthor%5C%22%3A%5C%22Vili%5C%22%7D%22%7D" target="_blank">Click here to install this using Obtainium!</a>

<div style="display:flex;gap:12px;align-items:center;justify-content:center;flex-wrap:nowrap">
  <img src="./img/current.png" alt="Current screen" style="width:250px;max-width:48%;height:auto;" />
  <img src="./img/whole.png" alt="Whole day screen" style="width:250px;max-width:48%;height:auto;" />
</div>

## Signing key fingerprints

```
SHA1: EB:8F:58:90:A7:97:53:1D:83:9D:E3:ED:BC:EF:31:27:A7:62:1D:FC
SHA256: 03:65:47:32:EC:9A:B8:D0:A7:B7:6E:F6:F3:91:55:5E:59:EC:42:4D:0A:FF:4B:A8:6A:22:3F:60:EB:32:BF:A4
```

## Permissions

This app requires only one Android permission:

- `android.permission.INTERNET`

The permission is declared in the app manifest (see `app/src/main/AndroidManifest.xml`) and is used only to fetch spot price data from the public API (spot-hinta.fi). No other runtime or sensitive permissions are requested.

## Verifying the installed app (AppVerifier)

1. Install the APK on the device.
2. Open the AppVerifier app on the device.
3. In AppVerifier:
   - Select the installed app (Pörssisähkö) or point the tool to the APK file.
   - View the app's signing certificate details: the tool should display certificate fingerprints (SHA1/SHA256).
4. Compare the displayed SHA1 and SHA256 fingerprints against the "Signing key fingerprints" listed in this README. If they match, the app is signed with the expected key.

## License

This app is under the MIT License.
