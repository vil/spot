# Pörssisähkö

Secure and minimal Android app to check the current electricity price in Finland.

## Signing key fingerprints

```
SHA1: EB:8F:58:90:A7:97:53:1D:83:9D:E3:ED:BC:EF:31:27:A7:62:1D:FC
SHA256: 03:65:47:32:EC:9A:B8:D0:A7:B7:6E:F6:F3:91:55:5E:59:EC:42:4D:0A:FF:4B:A8:6A:22:3F:60:EB:32:BF:A4
```

## Verifying the installed app (AppVerifier)

1. Install the APK on the device.
2. Open the AppVerifier app on the device.
3. In AppVerifier:
   - Select the installed app (Pörssisähkö) or point the tool to the APK file.
   - View the app's signing certificate details: the tool should display certificate fingerprints (SHA1/SHA256).
4. Compare the displayed SHA1 and SHA256 fingerprints against the "Signing key fingerprints" listed in this README. If they match, the app is signed with the expected key.
