package com.sixclient.screen;

import com.sixclient.SixClientMod;
import com.sixclient.ui.SixButton;
import com.sixclient.ui.SixUi;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.SafetyScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.FormattedCharSequence;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class SixTitleScreen extends Screen {
 
    private static final String URL_STORE = "https://example.com/store";
    private static final String URL_SKIN = "https://www.minecraft.net/en-us/msaprofile/mygames/editskin";

    private static final int BTN_H = 22;
    private static final int GAP = 4;
    private static final int FOOTER_H = 14;

    private int logoX, logoY, logoW, logoH;
    private boolean sidePanels;
    private int skinX, skinY, skinW, skinH;
    private int newsX, newsY, newsW, newsH;
    private int partX, partY, partW, partH;
    private int topIconsLeft;

    public SixTitleScreen() {
        super(Component.literal("SIX CLIENT"));
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected void init() {
        Minecraft mc = Minecraft.getInstance();
        sidePanels = width >= 520 && height >= 300;

        // ---- kolom tengah: logo + tombol utama ----
        int colW = Math.max(150, Math.min(210, (int) (width * 0.26f)));
        int colX = (width - colW) / 2;
        int block = 6 * (BTN_H + GAP) + 4 + BTN_H;
        int lh = Math.max(40, Math.min((int) (height * 0.34f), Math.min(130, height - block - 46)));
        logoH = lh;
        logoW = (int) (lh * (float) SixUi.LOGO_W / SixUi.LOGO_H);
        int total = lh + 6 + block;
        int top = Math.max(30, (height - total) / 2);
        logoX = (width - logoW) / 2;
        logoY = top;

        String[] labels = {"SINGLEPLAYER", "MULTIPLAYER", "COSMETICS", "MODS", "PARTNERS", "STORE"};
        Identifier[] icons = {SixUi.ICON_PLAY, SixUi.ICON_MULTI, SixUi.ICON_SHIRT, SixUi.ICON_PUZZLE, SixUi.ICON_PARTNER, SixUi.ICON_STORE};
        Runnable[] actions = {
                () -> mc.gui.setScreen(new SelectWorldScreen(this)),
                () -> mc.gui.setScreen(mc.options.skipMultiplayerWarning ? new JoinMultiplayerScreen(this) : new SafetyScreen(this)),
                () -> mc.gui.setScreen(new SixInfoScreen(this, "Cosmetics", List.of("Cosmetics belum tersedia.", "Segera hadir di SIX CLIENT."))),
                () -> mc.gui.setScreen(new SixInfoScreen(this, "Mods", modLines())),
                this::openPartners,
                () -> openLink(URL_STORE)
        };
        int y = top + lh + 6;
        for (int i = 0; i < labels.length; i++) {
            addRenderableWidget(new SixButton(colX, y, colW, BTN_H, Component.literal(labels[i]), icons[i],
                    i == 0 ? SixButton.Style.PRIMARY : SixButton.Style.NORMAL, actions[i]));
            y += BTN_H + GAP;
        }
        addRenderableWidget(new SixButton(colX, y + 4, colW, BTN_H, Component.literal("QUIT GAME"), SixUi.ICON_POWER,
                SixButton.Style.DANGER, mc::stop));

        // ---- ikon bar atas kanan ----
        int s = 20;
        int ix = width - 8 - s;
        addRenderableWidget(new SixButton(ix, 6, s, s, Component.literal("Folder"), SixUi.ICON_FOLDER, SixButton.Style.ICON,
                () -> Util.getPlatform().openFile(mc.gameDirectory)));
        ix -= s + GAP;
        addRenderableWidget(new SixButton(ix, 6, s, s, Component.literal("Settings"), SixUi.ICON_GEAR, SixButton.Style.ICON,
                () -> mc.gui.setScreen(new OptionsScreen(this, mc.options, false))));
        ix -= s + GAP;
        addRenderableWidget(new SixButton(ix, 6, s, s, Component.literal("Rank"), SixUi.ICON_CROWN, SixButton.Style.ICON,
                () -> mc.gui.setScreen(new SixInfoScreen(this, "Rank", List.of("Fitur rank belum tersedia.", "Segera hadir di SIX CLIENT.")))));
        ix -= s + GAP;
        addRenderableWidget(new SixButton(ix, 6, s, s, Component.literal("Account"), SixUi.ICON_USER, SixButton.Style.ICON,
                () -> mc.gui.setScreen(new SixInfoScreen(this, "Account", List.of(
                        "Username: " + mc.getUser().getName(),
                        "Minecraft " + mc.getLaunchedVersion())))));
        topIconsLeft = ix;

        if (!sidePanels) return;

        skinX = 8; skinY = 48; skinW = 104; skinH = 128;
        addRenderableWidget(new SixButton(skinX + 8, skinY + skinH - 28, skinW - 16, 20, Component.literal("Change Skin"), null,
                SixButton.Style.NORMAL, () -> openLink(URL_SKIN)));

        newsW = Math.min(205, width / 3);
        newsH = 80;
        newsX = 8;
        newsY = height - FOOTER_H - 6 - newsH;
        addRenderableWidget(new SixButton(newsX + 68, newsY + newsH - 24, 62, 16, Component.literal("Read More"), null,
                SixButton.Style.PRIMARY, () -> mc.gui.setScreen(new SixInfoScreen(this, "Welcome to SIX CLIENT!", List.of(
                        "Terima kasih telah menggunakan SIX CLIENT.",
                        "Nikmati pengalaman bermain Minecraft",
                        "yang lebih seru!")))));

        partW = 160;
        partH = 70;
        partX = width - 8 - partW;
        partY = height - FOOTER_H - 6 - partH;
        addRenderableWidget(new SixButton(partX + partW - 46, partY + 4, 40, 12, Component.literal("View All"), null,
                SixButton.Style.NORMAL, this::openPartners));
    }

    // ------------------------------------------------------------------ aksi

    private void openLink(String url) {
        Minecraft mc = Minecraft.getInstance();
        mc.gui.setScreen(new ConfirmLinkScreen(confirmed -> {
            if (confirmed) Util.getPlatform().openUri(URI.create(url));
            mc.gui.setScreen(this);
        }, url, true));
    }

    private void openPartners() {
        Minecraft.getInstance().gui.setScreen(new SixInfoScreen(this, "Partners", List.of(
                "SIX STORE  -  Top Up & Digital Product",
                "VANTYSIX  -  Gaming Community",
                "Manzzx Cloud  -  Cloud Hosting")));
    }

    private List<String> modLines() {
        List<String> out = new ArrayList<>();
        List<ModContainer> mods = new ArrayList<>(FabricLoader.getInstance().getAllMods());
        mods.removeIf(m -> m.getMetadata().getId().equals("java") || m.getMetadata().getId().startsWith("fabric-"));
        out.add(mods.size() + " mod terpasang:");
        int shown = 0;
        for (ModContainer m : mods) {
            if (shown++ >= 14) { out.add("..."); break; }
            out.add("- " + m.getMetadata().getName() + " " + m.getMetadata().getVersion().getFriendlyString());
        }
        return out;
    }

    // ---------------------------------------------------------------- render
    // Semua gambar non-widget ada di extractBackground, jadi tampil di bawah tombol.

    @Override
    public void extractBackground(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        SixUi.drawBackground(g, width, height, mouseX, mouseY);

        // header bergaya konsep (emblem + SIX CLIENT + tagline + frame merah)
        if (width >= 420) {
            int hw = Math.min(340, (int) (width * 0.34f));
            int hh = hw * SixUi.HEADER_H / SixUi.HEADER_W;
            SixUi.image(g, SixUi.HEADER, 0, 0, hw, hh, SixUi.HEADER_W, SixUi.HEADER_H);
        }

        SixUi.image(g, SixUi.LOGO, logoX, logoY, logoW, logoH, SixUi.LOGO_W, SixUi.LOGO_H);

        Minecraft mc = Minecraft.getInstance();
        String username = mc.getUser().getName();

        if (sidePanels) {
            int cw = 98;
            int cx = topIconsLeft - 6 - cw;
            SixUi.drawPanel(g, cx, 5, cw, 22);
            SixUi.drawIcon(g, SixUi.ICON_USER, cx + 5, 9, 14);
            g.text(font, font.plainSubstrByWidth(username, 60), cx + 24, 8, SixUi.TEXT, false);
            SixUi.drawSmall(g, "Logged in", cx + 24, 18, SixUi.TEXT_DIM, 0.65f);

            int tw = 132;
            int tx = width - 8 - tw;
            SixUi.drawPanel(g, tx, 34, tw, 26);
            g.text(font, Component.literal("SIX CLIENT").withStyle(ChatFormatting.BOLD), tx + 10, 39, SixUi.TEXT, true);
            SixUi.drawSmall(g, "More than just a launcher.", tx + 10, 50, SixUi.TEXT_DIM, 0.75f);

            drawSkinPanel(g, username);
            drawNewsPanel(g);
            drawPartnersPanel(g);
        }

        g.fill(0, height - FOOTER_H, width, height, 0xD0000000);
        g.fill(0, height - FOOTER_H, width, height - FOOTER_H + 1, SixUi.RED_DARK);
        g.text(font, "SIX CLIENT  " + SixClientMod.VERSION + "  |  Minecraft Java Edition", 8, height - 11, SixUi.TEXT_DIM, false);
        String ver = "Minecraft " + mc.getLaunchedVersion();
        g.text(font, ver, width - 8 - font.width(ver), height - 11, SixUi.TEXT_DIM, false);
    }

    private void drawSkinPanel(GuiGraphicsExtractor g, String username) {
        SixUi.drawPanel(g, skinX, skinY, skinW, skinH);
        g.text(font, "YOUR SKIN", skinX + 8, skinY + 8, SixUi.TEXT, false);
        int size = 52;
        int hx = skinX + (skinW - size) / 2;
        int hy = skinY + 24;
        g.fill(hx - 2, hy + size + 3, hx + size + 2, hy + size + 5, SixUi.RED_DARK);
        // kepala (8,8) dan layer topi (40,8) dari skin default 64x64
        g.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, SixUi.STEVE, hx, hy, 8f, 8f, size, size, 8, 8, 64, 64);
        g.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, SixUi.STEVE, hx - 2, hy - 2, 40f, 8f, size + 4, size + 4, 8, 8, 64, 64);
        String name = font.plainSubstrByWidth(username, skinW - 12);
        g.text(font, name, skinX + (skinW - font.width(name)) / 2, hy + size + 10, SixUi.TEXT, false);
    }

    private void drawNewsPanel(GuiGraphicsExtractor g) {
        SixUi.drawPanel(g, newsX, newsY, newsW, newsH);
        int th = 54;
        g.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, SixUi.BG, newsX + 8, newsY + 8,
                1180f, 280f, th, th, 400, 400, SixUi.BG_W, SixUi.BG_H);
        g.outline(newsX + 8, newsY + 8, th, th, SixUi.RED_DARK);
        int tx = newsX + 68;
        SixUi.drawSmall(g, "LATEST UPDATE", tx, newsY + 8, SixUi.TEXT_DIM, 0.7f);
        SixUi.drawSmall(g, SixClientMod.VERSION, newsX + newsW - 8 - (int) (font.width(SixClientMod.VERSION) * 0.7f), newsY + 8, SixUi.TEXT_DIM, 0.7f);
        SixUi.drawSmall(g, "Welcome to SIX CLIENT!", tx, newsY + 19, SixUi.TEXT, 0.85f);
        int wrapW = (int) ((newsW - 76) / 0.7f);
        List<FormattedCharSequence> lines = font.split(
                FormattedText.of("Terima kasih telah menggunakan SIX CLIENT. Nikmati pengalaman bermain Minecraft yang lebih seru!"), wrapW);
        int ly = newsY + 31;
        for (int i = 0; i < Math.min(3, lines.size()); i++) {
            g.pose().pushMatrix();
            g.pose().translate((float) tx, (float) ly);
            g.pose().scale(0.7f, 0.7f);
            g.text(font, lines.get(i), 0, 0, SixUi.TEXT_DIM, false);
            g.pose().popMatrix();
            ly += 7;
        }
    }

    private void drawPartnersPanel(GuiGraphicsExtractor g) {
        SixUi.drawPanel(g, partX, partY, partW, partH);
        g.text(font, "OUR PARTNERS", partX + 8, partY + 7, SixUi.TEXT, false);
        String[] names = {"SIX STORE", "VANTYSIX", "Manzzx Cloud"};
        String[] subs = {"Top Up & Digital", "Gaming Community", "Cloud Hosting"};
        int[] colors = {0xFFE11D2E, 0xFF7B3FE4, 0xFF3FA0FF};
        String[] letters = {"S", "V", "M"};
        int cw = (partW - 16 - 8) / 3;
        int cy = partY + 22;
        for (int i = 0; i < 3; i++) {
            int cx = partX + 8 + i * (cw + 4);
            g.fill(cx, cy, cx + cw, cy + 40, 0xB0140809);
            g.outline(cx, cy, cw, 40, 0xFF3A1216);
            int mid = cx + cw / 2;
            g.fill(mid - 8, cy + 4, mid + 8, cy + 20, colors[i]);
            SixUi.drawCentered(g, letters[i], mid, cy + 8, SixUi.TEXT, true);
            SixUi.drawSmallCentered(g, names[i], mid, cy + 24, SixUi.TEXT, 0.65f);
            SixUi.drawSmallCentered(g, subs[i], mid, cy + 32, SixUi.TEXT_DIM, 0.5f);
        }
    }
}
