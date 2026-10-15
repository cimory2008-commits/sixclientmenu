package com.sixclient.screen;

import com.sixclient.ui.SixButton;
import com.sixclient.ui.SixUi;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

/** Layar sederhana bergaya SIX CLIENT untuk Cosmetics, Mods, Partners, dll. */
public class SixInfoScreen extends Screen {
    private final Screen parent;
    private final String heading;
    private final List<String> lines;
    private int px, py, pw, ph;

    public SixInfoScreen(Screen parent, String heading, List<String> lines) {
        super(Component.literal(heading));
        this.parent = parent;
        this.heading = heading;
        this.lines = lines;
    }

    @Override
    protected void init() {
        pw = Math.min(340, width - 30);
        ph = Math.min(height - 30, 62 + lines.size() * 12 + 34);
        px = (width - pw) / 2;
        py = (height - ph) / 2;
        addRenderableWidget(new SixButton(width / 2 - 50, py + ph - 28, 100, 20,
                Component.literal("Back"), null, SixButton.Style.PRIMARY, this::onClose));
    }

    /** Background + panel digambar di sini supaya tampil di bawah widget. */
    @Override
    public void extractBackground(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        SixUi.drawBackground(g, width, height, mouseX, mouseY);
        SixUi.drawPanel(g, px, py, pw, ph);
        int emblemH = 22;
        int emblemW = emblemH * SixUi.EMBLEM_W / SixUi.EMBLEM_H;
        SixUi.image(g, SixUi.EMBLEM, px + 10, py + 9, emblemW, emblemH, SixUi.EMBLEM_W, SixUi.EMBLEM_H);
        g.text(font, heading.toUpperCase(), px + 16 + emblemW, py + 14, SixUi.TEXT, true);
        g.fill(px + 10, py + 38, px + pw - 10, py + 39, SixUi.RED_DARK);
        int y = py + 48;
        for (String line : lines) {
            g.text(font, line, px + 14, y, SixUi.TEXT_DIM, false);
            y += 12;
        }
    }

    @Override
    public void onClose() {
        minecraft.gui.setScreen(parent);
    }
}
