# SIX CLIENT - Menu Utama (Fabric 26.2)

Mod client-side yang mengganti title screen dengan menu SIX CLIENT.
Logo = gambar 2, background = gambar 3, layout = konsep gambar 1.

## Build
1. Pasang JDK 25.
2. Generate wrapper Gradle sekali: `gradle wrapper` (pakai versi Gradle yang diminta template Fabric 26.2),
   atau buka di IntelliJ IDEA.
3. Tes: `./gradlew runClient`
4. Jar: `./gradlew build` -> `build/libs/sixclient-1.0.0.jar`, taruh di `mods` bareng Fabric API 0.161.0+26.2.

## Catatan versi
- 26.2 pakai nama class Mojang (bukan Yarn), tanpa remap, Java 25.
- Kalau versi Loom / Loader / Fabric API di `gradle.properties` ditolak, samakan dengan
  template resmi: https://fabricmc.net/develop/template/ (pilih 26.2).
- Render GUI 26.2 retained-mode: gambar background & panel ada di `extractBackground`,
  tombol di widget `extractWidgetRenderState`.

## Isi
- `mixin/GuiMixin` : mengganti TitleScreen vanilla (setScreen ada di `Gui` pada 26.2).
- `screen/SixTitleScreen`, `screen/SixInfoScreen`, `ui/SixUi`, `ui/SixButton`.
- Link Store/Partners di `SixTitleScreen.java` masih placeholder.
