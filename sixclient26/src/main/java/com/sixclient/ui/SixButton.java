package com.sixclient.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class SixButton extends AbstractWidget {
    public enum Style { PRIMARY, NORMAL, DANGER, ICON }

    private final Identifier icon;
    private final Style style;
    private final Runnable action;

    public SixButton(int x, int y, int w, int h, Component label, Identifier icon, Style style, Runnable action) {
        super(x, y, w, h, label);
        this.icon = icon;
        this.style = style;
        this.action = action;
    }

    @Override
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        action.run();
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        Font font = Minecraft.getInstance().font;
        boolean hot = this.isHovered();
        int x = getX(), y = getY(), w = getWidth(), h = getHeight();

        int border;
        switch (style) {
            case PRIMARY -> {
                g.fillGradient(x, y, x + w, y + h, hot ? 0xFFEE2A3C : 0xFFD01624, hot ? 0xFFB8101C : 0xFFA30D18);
                border = 0xFFFF6A75;
            }
            case DANGER -> {
                g.fill(x, y, x + w, y + h, hot ? 0xE0551018 : 0xCC3A0508);
                border = hot ? SixUi.RED_BRIGHT : SixUi.PANEL_BORDER;
            }
            default -> {
                g.fill(x, y, x + w, y + h, hot ? 0xE8300B10 : 0xD8130809);
                border = hot ? SixUi.RED_BRIGHT : 0xFF5A1A1F;
            }
        }
        g.outline(x, y, w, h, border);

        int textY = y + (h - 8) / 2;
        if (style == Style.ICON) {
            if (icon != null) {
                int s = Math.min(w, h) - 8;
                SixUi.drawIcon(g, icon, x + (w - s) / 2, y + (h - s) / 2, s);
            }
            return;
        }
        if (icon != null) {
            SixUi.drawIcon(g, icon, x + 8, y + 4, h - 8);
        }
        Component label = getMessage();
        g.text(font, label, x + (w - font.width(label)) / 2, textY, SixUi.TEXT, true);
        if (icon != null && h >= 20 && style != Style.DANGER) {
            g.text(font, ">", x + w - 12, textY, hot ? SixUi.TEXT : SixUi.RED_BRIGHT, true);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        this.defaultButtonNarrationText(output);
    }
}
