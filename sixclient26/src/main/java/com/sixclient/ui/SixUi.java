package com.sixclient.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

/** Warna, tekstur, dan helper gambar yang dipakai semua layar SIX CLIENT (26.2). */
public final class SixUi {
    private SixUi() {}

    public static final int BG_W = 1584, BG_H = 993;
    public static final int LOGO_W = 900, LOGO_H = 591;
    public static final int EMBLEM_W = 512, EMBLEM_H = 413;
    public static final int HEADER_W = 830, HEADER_H = 95;

    public static final Identifier BG = tex("background.png");
    public static final Identifier LOGO = tex("logo.png");
    public static final Identifier EMBLEM = tex("logo_icon.png");
    public static final Identifier HEADER = tex("header.png");
    public static final Identifier ICON_PLAY = tex("icon_play.png");
    public static final Identifier ICON_MULTI = tex("icon_multi.png");
    public static final Identifier ICON_SHIRT = tex("icon_shirt.png");
    public static final Identifier ICON_PUZZLE = tex("icon_puzzle.png");
    public static final Identifier ICON_PARTNER = tex("icon_partner.png");
    public static final Identifier ICON_STORE = tex("icon_store.png");
    public static final Identifier ICON_POWER = tex("icon_power.png");
    public static final Identifier ICON_USER = tex("icon_user.png");
    public static final Identifier ICON_CROWN = tex("icon_crown.png");
    public static final Identifier ICON_GEAR = tex("icon_gear.png");
    public static final Identifier ICON_FOLDER = tex("icon_folder.png");
    /** Skin default vanilla (dipakai untuk kepala di panel skin). */
    public static final Identifier STEVE = Identifier.withDefaultNamespace("textures/entity/player/wide/steve.png");

    public static final int RED = 0xFFE11D2E;
    public static final int RED_BRIGHT = 0xFFFF3B4A;
    public static final int RED_DARK = 0xFF7A0C14;
    public static final int PANEL_FILL = 0xC00A0506;
    public static final int PANEL_BORDER = 0xFFA3121C;
    public static final int TEXT = 0xFFFFFFFF;
    public static final int TEXT_DIM = 0xFFB9A9AB;

    private static Identifier tex(String name) {
        return Identifier.fromNamespaceAndPath("sixclient", "textures/gui/" + name);
    }

    private static Font font() {
        return Minecraft.getInstance().font;
    }

    /** Gambar tekstur penuh ke ukuran w x h. */
    public static void image(GuiGraphicsExtractor g, Identifier tex, int x, int y, int w, int h, int texW, int texH) {
        g.blit(RenderPipelines.GUI_TEXTURED, tex, x, y, 0f, 0f, w, h, texW, texH, texW, texH);
    }

    /** Background "cover" + parallax halus mengikuti mouse + vignette gelap. */
    public static void drawBackground(GuiGraphicsExtractor g, int w, int h, int mx, int my) {
        float scale = Math.max(w / (float) BG_W, h / (float) BG_H) * 1.05f;
        int dw = (int) (BG_W * scale);
        int dh = (int) (BG_H * scale);
        float px = (mx / (float) Math.max(1, w) - 0.5f) * -8f;
        float py = (my / (float) Math.max(1, h) - 0.5f) * -5f;
        int x = (int) ((w - dw) / 2f + px);
        int y = (int) ((h - dh) / 2f + py);
        image(g, BG, x, y, dw, dh, BG_W, BG_H);
        g.fillGradient(0, 0, w, h, 0x30000000, 0xA0000000);
    }

    public static void drawPanel(GuiGraphicsExtractor g, int x, int y, int w, int h) {
        g.fill(x, y, x + w, y + h, PANEL_FILL);
        g.outline(x, y, w, h, PANEL_BORDER);
        int a = 9;
        g.fill(x, y, x + a, y + 1, RED_BRIGHT);
        g.fill(x, y, x + 1, y + a, RED_BRIGHT);
        g.fill(x + w - a, y + h - 1, x + w, y + h, RED_BRIGHT);
        g.fill(x + w - 1, y + h - a, x + w, y + h, RED_BRIGHT);
    }

    public static void drawIcon(GuiGraphicsExtractor g, Identifier icon, int x, int y, int size) {
        image(g, icon, x, y, size, size, 64, 64);
    }

    public static void drawSmall(GuiGraphicsExtractor g, String text, int x, int y, int color, float scale) {
        g.pose().pushMatrix();
        g.pose().translate((float) x, (float) y);
        g.pose().scale(scale, scale);
        g.text(font(), text, 0, 0, color, false);
        g.pose().popMatrix();
    }

    public static void drawSmallCentered(GuiGraphicsExtractor g, String text, int cx, int y, int color, float scale) {
        float tw = font().width(text) * scale;
        drawSmall(g, text, Math.round(cx - tw / 2f), y, color, scale);
    }

    public static void drawCentered(GuiGraphicsExtractor g, String text, int cx, int y, int color, boolean shadow) {
        g.text(font(), text, cx - font().width(text) / 2, y, color, shadow);
    }
}
